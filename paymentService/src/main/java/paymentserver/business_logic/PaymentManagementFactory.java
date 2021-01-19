package paymentserver.business_logic;

import messaging.EventReceiver;
import messaging.EventSender;
import messaging.rabbitmq.RabbitMqListener;
import messaging.rabbitmq.RabbitMqSender;
import paymentserver.data_access.PaymentRepository;

//Code copied and modified from the Demo project: http://bit.ly/02267async mentioned
// in the Asynchronous communication section of Software Development of Webservices course
public class PaymentManagementFactory {
    static IPaymentManagement service = null;

    public IPaymentManagement getService() {
        if (service != null) {
            return service;
        }

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
