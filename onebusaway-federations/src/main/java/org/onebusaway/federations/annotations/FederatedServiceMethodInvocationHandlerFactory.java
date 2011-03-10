package org.onebusaway.federations.annotations;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * This factory can be used to examine a Method signature and create an
 * appropriate {@link FederatedServiceMethodInvocationHandler} based on method
 * annotations. See the list of supported annotations below.
 * 
 * @author bdferris
 * @see FederatedByAgencyIdMethod
 * @see FederatedByAggregateMethod
 * @see FederatedByBoundsMethod
 * @see FederatedByCoordinateBoundsMethod
 * @see FederatedByEntityIdMethod
 * @see FederatedByEntityIdsMethod
 * @see FederatedByLocationMethod
 */
public class FederatedServiceMethodInvocationHandlerFactory {

  public FederatedServiceMethodInvocationHandler getHandlerForMethod(
      Method method) {

    FederatedByAgencyIdMethod byAgency = method.getAnnotation(FederatedByAgencyIdMethod.class);
    if (byAgency != null)
      return new FederatedByAgencyIdMethodInvocationHandlerImpl(method,
          byAgency.argument(), byAgency.propertyExpression());

    FederatedByAnyEntityIdMethod byAnyEntityId = method.getAnnotation(FederatedByAnyEntityIdMethod.class);
    if (byAnyEntityId != null)
      return new FederatedByAnyEntityIdMethodInvocationHandlerImpl(method,
          byAnyEntityId.argument(), byAnyEntityId.properties());

    FederatedByEntityIdMethod ann0 = method.getAnnotation(FederatedByEntityIdMethod.class);
    if (ann0 != null)
      return new FederatedByEntityIdMethodInvocationHandlerImpl(method,
          ann0.argument(), ann0.propertyExpression());

    FederatedByEntityIdsMethod ann1 = method.getAnnotation(FederatedByEntityIdsMethod.class);
    if (ann1 != null)
      return new FederatedByEntityIdsMethodInvocationHandlerImpl(
          ann1.argument());

    FederatedByBoundsMethod ann2 = method.getAnnotation(FederatedByBoundsMethod.class);
    if (ann2 != null)
      return new FederatedByBoundsMethodInvocationHandlerImpl(
          ann2.lat1Argument(), ann2.lon1Argument(), ann2.lat2Argument(),
          ann2.lon2Argument());

    FederatedByLocationMethod ann3 = method.getAnnotation(FederatedByLocationMethod.class);
    if (ann3 != null)
      return new FederatedByLocationMethodInvocationHandlerImpl(
          ann3.latArgument(), ann3.lonArgument());

    FederatedByAggregateMethod ann4 = method.getAnnotation(FederatedByAggregateMethod.class);
    if (ann4 != null) {
      EMethodAggregationType aggregationType = getAggregationTypeForMethod(method);
      return new FederatedByAggregateMethodInvocationHandlerImpl(
          aggregationType);
    }

    FederatedByCoordinateBoundsMethod ann5 = method.getAnnotation(FederatedByCoordinateBoundsMethod.class);
    if (ann5 != null)
      return new FederatedByCoordinateBoundsMethodInvocationHandlerImpl(method,
          ann5.argument(), ann5.propertyExpression());

    throw new IllegalArgumentException(
        "No FederatedService method annotation for method: " + method);
  }

  private EMethodAggregationType getAggregationTypeForMethod(Method method) {
    Class<?> returnType = method.getReturnType();
    if (List.class.isAssignableFrom(returnType))
      return EMethodAggregationType.LIST;
    if (Map.class.isAssignableFrom(returnType))
      return EMethodAggregationType.MAP;
    throw new IllegalArgumentException("unsupported aggregation type: "
        + returnType.getName());
  }

}
