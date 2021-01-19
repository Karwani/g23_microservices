package paymentserver.data_access;

import paymentserver.models.Payment;

import java.util.List;

public interface IPaymentRepository {

    void addPayment(Payment payment);
    List<Payment> getPaymentList(String userId);
    void deletePayments(String userId);
}
