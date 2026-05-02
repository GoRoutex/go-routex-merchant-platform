package vn.com.routex.merchant.platform.domain.route.readmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RouteSearchView {
    private String id;
    private String originCode;
    private String originName;
    private String destinationCode;
    private String destinationName;
}
