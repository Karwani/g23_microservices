package paymentserver.business_logic;

import messaging.EventReceiver;
import messaging.EventSender;
import messaging.rabbitmq.RabbitMqListener;
import messaging.rabbitmq.RabbitMqSender;
import paymentserver.data_access.PaymentRepository;

public class PaymentManagementFactory {
    static IPaymentManagement service = null;

    public IPaymentManagement getService() {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a PaymentService
        if (service != null) {
            return service;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the PaymentService in tests
        // without sending actual messages to RabbitMq.
        EventSender b = new RabbitMqSender();
        service = new PaymentManagement(b,new PaymentRepository());
        RabbitMqListener r = new RabbitMqListener((EventReceiver) service);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return service;
    }
}
