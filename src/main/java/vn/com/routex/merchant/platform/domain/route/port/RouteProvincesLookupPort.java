package vn.com.routex.merchant.platform.domain.route.port;


import vn.com.routex.merchant.platform.domain.route.model.ProvincesInformationPair;

public interface RouteProvincesLookupPort {
    ProvincesInformationPair getCodes(String origin, String destination);
}
