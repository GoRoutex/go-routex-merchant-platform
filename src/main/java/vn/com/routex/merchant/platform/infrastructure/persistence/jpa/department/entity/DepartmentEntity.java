package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.department.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private DepartmentType type;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "WARD_ID")
    private String wardId;

    @Column(name = "WARD_NAME")
    private String wardName;

    @Column(name = "DISTRICT_ID")
    private String districtId;

    @Column(name = "DISTRICT_NAME")
    private String districtName;

    @Column(name = "PROVINCE_ID")
    private String provinceId;

    @Column(name = "PROVINCE_NAME")
    private String provinceName;

    @Column(name = "TIME_AT_DEPARTMENT")
    private Long timeAtDepartment;

    @Column(name = "PASSING")
    private boolean passing;

    @Column(name = "IS_SHUTTLE_SERVICE")
    private boolean isShuttleService;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "POINT_KIND")
    private Integer pointKind;

    @Column(name = "PRESENT_BEFORE_MINUTES")
    private Integer presentBeforeMinutes;

    @Column(name = "OPENING_TIME")
    private String openingTime;

    @Column(name = "CLOSING_TIME")
    private String closingTime;

    @Column(name = "ONLINE_OPENING_TIME")
    private String onlineOpeningTime;

    @Column(name = "ONLINE_CLOSING_TIME")
    private String onlineClosingTime;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private DepartmentStatus status;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;
}
