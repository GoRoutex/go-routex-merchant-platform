package vn.com.routex.merchant.platform.application.service;


import vn.com.routex.merchant.platform.application.command.department.CreateDepartmentCommand;
import vn.com.routex.merchant.platform.application.command.department.CreateDepartmentResult;
import vn.com.routex.merchant.platform.application.command.department.DeleteDepartmentCommand;
import vn.com.routex.merchant.platform.application.command.department.DeleteDepartmentResult;
import vn.com.routex.merchant.platform.application.command.department.FetchDepartmentQuery;
import vn.com.routex.merchant.platform.application.command.department.FetchDepartmentResult;
import vn.com.routex.merchant.platform.application.command.department.GetDepartmentDetailQuery;
import vn.com.routex.merchant.platform.application.command.department.GetDepartmentDetailResult;
import vn.com.routex.merchant.platform.application.command.department.UpdateDepartmentCommand;
import vn.com.routex.merchant.platform.application.command.department.UpdateDepartmentResult;

public interface DepartmentManagementService {
    CreateDepartmentResult createDepartment(CreateDepartmentCommand command);

    UpdateDepartmentResult updateDepartment(UpdateDepartmentCommand command);

    DeleteDepartmentResult deleteDepartment(DeleteDepartmentCommand command);

    FetchDepartmentResult fetchDepartment(FetchDepartmentQuery query);

    GetDepartmentDetailResult getDepartmentDetail(GetDepartmentDetailQuery query);
}
