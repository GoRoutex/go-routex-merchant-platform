package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.department;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.department.model.Department;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.department.entity.DepartmentEntity;

@Component
public class DepartmentPersistenceMapper {
    public Department toDomain(DepartmentEntity departmentEntity) {
        if(departmentEntity == null) {
            return null;
        }

        return Department.builder()
                .id(departmentEntity.getId())
                .merchantId(departmentEntity.getMerchantId())
                .code(departmentEntity.getCode())
                .name(departmentEntity.getName())
                .type(departmentEntity.getType())
                .address(departmentEntity.getAddress())
                .wardId(departmentEntity.getWardId())
                .wardName(departmentEntity.getWardName())
                .districtId(departmentEntity.getDistrictId())
                .districtName(departmentEntity.getDistrictName())
                .provinceId(departmentEntity.getProvinceId())
                .provinceName(departmentEntity.getProvinceName())
                .timeAtDepartment(departmentEntity.getTimeAtDepartment())
                .passing(departmentEntity.isPassing())
                .isShuttleService(departmentEntity.isShuttleService())
                .note(departmentEntity.getNote())
                .pointKind(departmentEntity.getPointKind())
                .presentBeforeMinutes(departmentEntity.getPresentBeforeMinutes())
                .openingTime(departmentEntity.getOpeningTime())
                .closingTime(departmentEntity.getClosingTime())
                .onlineOpeningTime(departmentEntity.getOnlineOpeningTime())
                .onlineClosingTime(departmentEntity.getOnlineClosingTime())
                .latitude(departmentEntity.getLatitude())
                .longitude(departmentEntity.getLongitude())
                .status(departmentEntity.getStatus())
                .isActive(departmentEntity.isActive())
                .createdAt(departmentEntity.getCreatedAt())
                .createdBy(departmentEntity.getCreatedBy())
                .updatedAt(departmentEntity.getUpdatedAt())
                .updatedBy(departmentEntity.getUpdatedBy())
                .build();
    }

    public DepartmentEntity toEntity(Department department) {
        if(department == null) {
            return null;
        }

        return DepartmentEntity.builder()
                .id(department.getId())
                .merchantId(department.getMerchantId())
                .code(department.getCode())
                .name(department.getName())
                .type(department.getType())
                .address(department.getAddress())
                .wardId(department.getWardId())
                .wardName(department.getWardName())
                .districtId(department.getDistrictId())
                .districtName(department.getDistrictName())
                .provinceId(department.getProvinceId())
                .provinceName(department.getProvinceName())
                .timeAtDepartment(department.getTimeAtDepartment())
                .passing(department.isPassing())
                .isShuttleService(department.isShuttleService())
                .note(department.getNote())
                .pointKind(department.getPointKind())
                .presentBeforeMinutes(department.getPresentBeforeMinutes())
                .openingTime(department.getOpeningTime())
                .closingTime(department.getClosingTime())
                .onlineOpeningTime(department.getOnlineOpeningTime())
                .onlineClosingTime(department.getOnlineClosingTime())
                .latitude(department.getLatitude())
                .longitude(department.getLongitude())
                .status(department.getStatus())
                .isActive(department.isActive())
                .createdAt(department.getCreatedAt())
                .createdBy(department.getCreatedBy())
                .updatedAt(department.getUpdatedAt())
                .updatedBy(department.getUpdatedBy())
                .build();
    }
}
