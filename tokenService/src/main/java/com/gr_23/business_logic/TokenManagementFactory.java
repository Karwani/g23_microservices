package com.gr_23.business_logic;

import com.gr_23.data_access.ITokenRepository;
import com.gr_23.data_access.TokenRepository;
import messaging.EventReceiver;
import messaging.EventSender;
import messaging.rabbitmq.RabbitMqListener;
import messaging.rabbitmq.RabbitMqSender;

public class TokenManagementFactory {
    static ITokenManagement service = null;
/*    static ITokenManagement service = eagerInitialize();
    public  static ITokenManagement eagerInitialize()
    {
        System.out.println("Initialize happening");
        if (service != null) {
            return service;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the TokenService in tests
        // without sending actual messages to RabbitMq.
        EventSender b = new RabbitMqSender();
        service = new TokenManagement(new TokenRepository(),b);
        RabbitMqListener r = new RabbitMqListener((EventReceiver) service);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return service;
    }*/
    public ITokenManagement getService() {


        if (service != null) {
            return service;
        }
        System.out.println("Initialize happening ctor");
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a TokenService
        return getiTokenManagement(new TokenRepository());
    }
    public ITokenManagement getService(ITokenRepository tokenRepo) {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a TokenService
        return getiTokenManagement(tokenRepo);
    }
    private ITokenManagement getiTokenManagement(ITokenRepository tokenRepo) {
        if (service != null) {
            return service;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the TokenService in tests
        // without sending actual messages to RabbitMq.
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
