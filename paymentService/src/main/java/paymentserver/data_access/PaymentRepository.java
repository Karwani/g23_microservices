package paymentserver.data_access;

import paymentserver.models.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentRepository implements IPaymentRepository {
    HashMap<String,List<Payment>> paymentHashMap = new HashMap<>();
    @Override
    public void addPayment(Payment payment) {
        List<Payment> list = null;
        if(!paymentHashMap.containsKey(payment.getCustomerId()))
            list = new ArrayList<>();
        else list = paymentHashMap.get(payment.getCustomerId());
        list.add(payment);
        paymentHashMap.put(payment.getCustomerId(),list);
    }

    @Override
    public List<Payment> getPaymentList(String userId) {
        if(paymentHashMap.containsKey(userId))
            return paymentHashMap.get(userId);
        return null;
    }

    @Override
    public void deletePayments(String userId) {
        paymentHashMap.remove(userId);
    }
}
