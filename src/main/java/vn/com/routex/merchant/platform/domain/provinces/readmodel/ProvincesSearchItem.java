package vn.com.routex.merchant.platform.domain.provinces.readmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvincesSearchItem {
    private String id;
    private String name;
    private String code;
}
