package vn.com.routex.merchant.platform.application.service;


import vn.com.routex.merchant.platform.application.command.operationpoint.CreateOperationPointCommand;
import vn.com.routex.merchant.platform.application.command.operationpoint.CreateOperationPointResult;
import vn.com.routex.merchant.platform.application.command.operationpoint.DeleteOperationPointCommand;
import vn.com.routex.merchant.platform.application.command.operationpoint.DeleteOperationPointResult;
import vn.com.routex.merchant.platform.application.command.operationpoint.FetchOperationPointQuery;
import vn.com.routex.merchant.platform.application.command.operationpoint.FetchOperationPointResult;
import vn.com.routex.merchant.platform.application.command.operationpoint.GetOperationPointDetailQuery;
import vn.com.routex.merchant.platform.application.command.operationpoint.GetOperationPointDetailResult;
import vn.com.routex.merchant.platform.application.command.operationpoint.UpdateOperationPointCommand;
import vn.com.routex.merchant.platform.application.command.operationpoint.UpdateOperationPointResult;

public interface OperationPointManagementService {
    CreateOperationPointResult createOperationPoint(CreateOperationPointCommand command);

    UpdateOperationPointResult updateOperationPoint(UpdateOperationPointCommand command);

    DeleteOperationPointResult deleteOperationPoint(DeleteOperationPointCommand command);

    FetchOperationPointResult fetchOperationPoint(FetchOperationPointQuery query);

    GetOperationPointDetailResult getOperationPointDetail(GetOperationPointDetailQuery query);
}
