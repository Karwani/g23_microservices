package com.gr_23.business_logic;

import com.gr_23.data_access.ITokenRepository;
import com.gr_23.data_access.TokenRepository;
import messaging.EventReceiver;
import messaging.EventSender;
import messaging.rabbitmq.RabbitMqListener;
import messaging.rabbitmq.RabbitMqSender;

//Code copied and modified from the Demo project: http://bit.ly/02267async mentioned
// in the Asynchronous communication section of Software Development of Webservices course
public class TokenManagementFactory {
    static ITokenManagement service = null;

    public ITokenManagement getService() {


        if (service != null) {
            return service;
        }

        return getiTokenManagement(new TokenRepository());
    }
    private ITokenManagement getiTokenManagement(ITokenRepository tokenRepo) {
        if (service != null) {
            return service;
        }

        EventSender b = new RabbitMqSender();
        service = new TokenManagement(tokenRepo,b);
        RabbitMqListener r = new RabbitMqListener((EventReceiver) service);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return service;
    }
}
