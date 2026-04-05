package vn.com.routex.merchant.platform.domain.route.port;


import vn.com.routex.merchant.platform.domain.route.model.ProvincesCodePair;

public interface RouteProvincesLookupPort {
    ProvincesCodePair getCodes(String origin, String destination);
}
