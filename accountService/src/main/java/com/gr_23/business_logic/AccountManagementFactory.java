package com.gr_23.business_logic;

import messaging.EventReceiver;
import messaging.EventSender;
import messaging.rabbitmq.RabbitMqListener;
import messaging.rabbitmq.RabbitMqSender;

public class AccountManagementFactory {
    static IAccountManagement service = null;

    public IAccountManagement getService() {

        if (service != null) {
            return service;
        }

        EventSender b = new RabbitMqSender();
        service = new AccountManagement(b);
        RabbitMqListener r = new RabbitMqListener((EventReceiver) service);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return service;
    }
}
