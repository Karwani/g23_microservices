package com.gr_23.business_logic;

import com.gr_23.data_access.AccountRepository;
import com.gr_23.data_access.IAccountRepository;
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
        IAccountRepository accountRepository = new AccountRepository();

        EventSender sender = new RabbitMqSender();
        service = new AccountManagement(sender, accountRepository);
        RabbitMqListener r = new RabbitMqListener((EventReceiver) service);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return service;
    }
}
