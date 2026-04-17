package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.vehicletemplate.CreateVehicleTemplateCommand;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.CreateVehicleTemplateResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.DeleteVehicleTemplateCommand;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.DeleteVehicleTemplateResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplateDetailQuery;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplateDetailResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplatesQuery;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplatesResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.UpdateVehicleTemplateCommand;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.UpdateVehicleTemplateResult;

public interface VehicleTemplateManagementService {

    CreateVehicleTemplateResult createVehicleTemplate(CreateVehicleTemplateCommand command);

    UpdateVehicleTemplateResult updateVehicleTemplate(UpdateVehicleTemplateCommand command);

    DeleteVehicleTemplateResult deleteVehicleTemplate(DeleteVehicleTemplateCommand command);

    FetchVehicleTemplatesResult fetchVehicleTemplates(FetchVehicleTemplatesQuery query);

    FetchVehicleTemplateDetailResult fetchVehicleTemplateDetail(FetchVehicleTemplateDetailQuery query);
}
