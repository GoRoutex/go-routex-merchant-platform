package vn.com.routex.merchant.platform.application.command.vehicletemplate;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteVehicleTemplateCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String templateId
) {
}
