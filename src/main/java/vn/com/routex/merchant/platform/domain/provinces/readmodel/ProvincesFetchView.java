package vn.com.routex.merchant.platform.domain.provinces.readmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProvincesFetchView {
    private String id;
    private String name;
    private String code;
}
