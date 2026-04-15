package vn.com.routex.merchant.platform.application.service;


import vn.com.routex.merchant.platform.application.command.route.AssignRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.AssignRouteResult;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteResult;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesQuery;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesResult;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteResult;

public interface RouteManagementService {

    CreateRouteResult createRoute(CreateRouteCommand command);

    AssignRouteResult assignRoute(AssignRouteCommand command);

    UpdateRouteResult updateRoute(UpdateRouteCommand command);

    DeleteRouteResult deleteRoute(DeleteRouteCommand command);

    FetchRoutesResult fetchRoutes(FetchRoutesQuery query);
}
