package vn.com.routex.merchant.platform.application.service;


import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

public interface OutBoxService {
    void generateEvent(String aggregateId, String topic, String eventName, String eventKey, Object payload, BaseRequest context);
}
