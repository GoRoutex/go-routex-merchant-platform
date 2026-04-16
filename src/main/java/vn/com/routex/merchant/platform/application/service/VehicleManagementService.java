package vn.com.routex.merchant.platform.application.service;


import vn.com.routex.merchant.platform.application.command.vehicle.AddVehicleCommand;
import vn.com.routex.merchant.platform.application.command.vehicle.AddVehicleResult;
import vn.com.routex.merchant.platform.application.command.vehicle.DeleteVehicleCommand;
import vn.com.routex.merchant.platform.application.command.vehicle.DeleteVehicleResult;
import vn.com.routex.merchant.platform.application.command.vehicle.FetchVehicleDetailQuery;
import vn.com.routex.merchant.platform.application.command.vehicle.FetchVehicleDetailResult;
import vn.com.routex.merchant.platform.application.command.vehicle.FetchVehiclesQuery;
import vn.com.routex.merchant.platform.application.command.vehicle.FetchVehiclesResult;
import vn.com.routex.merchant.platform.application.command.vehicle.UpdateVehicleCommand;
import vn.com.routex.merchant.platform.application.command.vehicle.UpdateVehicleResult;

public interface VehicleManagementService {

    AddVehicleResult addVehicle(AddVehicleCommand command);

    UpdateVehicleResult updateVehicle(UpdateVehicleCommand command);

    DeleteVehicleResult deleteVehicle(DeleteVehicleCommand command);

    FetchVehiclesResult fetchVehicles(FetchVehiclesQuery query);

    FetchVehicleDetailResult fetchVehicleDetail(FetchVehicleDetailQuery query);
}
