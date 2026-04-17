package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.maintenance.CreateMaintenancePlanCommand;
import vn.com.routex.merchant.platform.application.command.maintenance.CreateMaintenancePlanResult;
import vn.com.routex.merchant.platform.application.command.maintenance.DeleteMaintenancePlanCommand;
import vn.com.routex.merchant.platform.application.command.maintenance.DeleteMaintenancePlanResult;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlanDetailQuery;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlanDetailResult;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlansQuery;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlansResult;
import vn.com.routex.merchant.platform.application.command.maintenance.UpdateMaintenancePlanCommand;
import vn.com.routex.merchant.platform.application.command.maintenance.UpdateMaintenancePlanResult;

public interface MaintenancePlanManagementService {
    CreateMaintenancePlanResult createMaintenancePlan(CreateMaintenancePlanCommand command);

    UpdateMaintenancePlanResult updateMaintenancePlan(UpdateMaintenancePlanCommand command);

    DeleteMaintenancePlanResult deleteMaintenancePlan(DeleteMaintenancePlanCommand command);

    FetchMaintenancePlansResult fetchMaintenancePlans(FetchMaintenancePlansQuery query);

    FetchMaintenancePlanDetailResult fetchMaintenancePlanDetail(FetchMaintenancePlanDetailQuery query);
}
