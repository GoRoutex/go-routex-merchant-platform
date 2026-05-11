package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;

@Builder
public record RoutePointCommand(
        String operationOrder,
        String note,
        String departmentId,
        String stopName,
        String stopAddress,
        String stopCity,
        Double stopLatitude,
        Double stopLongitude,
        Integer timeAtDepartment
) {
}
