package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.driver.CreateDriverCommand;
import vn.com.routex.merchant.platform.application.command.driver.CreateDriverResult;
import vn.com.routex.merchant.platform.application.command.driver.DeleteDriverCommand;
import vn.com.routex.merchant.platform.application.command.driver.DeleteDriverResult;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriverDetailQuery;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriverDetailResult;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriversQuery;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriversResult;
import vn.com.routex.merchant.platform.application.command.driver.UpdateDriverCommand;
import vn.com.routex.merchant.platform.application.command.driver.UpdateDriverResult;

public interface DriverManagementService {
    CreateDriverResult createDriver(CreateDriverCommand command);

    UpdateDriverResult updateDriver(UpdateDriverCommand command);

    DeleteDriverResult deleteDriver(DeleteDriverCommand command);

    FetchDriversResult fetchDrivers(FetchDriversQuery query);

    FetchDriverDetailResult fetchDriverDetail(FetchDriverDetailQuery query);
}
