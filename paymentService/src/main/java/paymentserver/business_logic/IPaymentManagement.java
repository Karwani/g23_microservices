package paymentserver.business_logic;

import dtu.ws.fastmoney.BankServiceException_Exception;
import paymentserver.models.Payment;

import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;
import java.util.List;

public interface IPaymentManagement {
    void initiateDTUPay(Payment payment) throws ValidationException, BankServiceException_Exception;

    Response consumeToken(String tokenId);
    List<Payment> getPayments(String userId);

    void deletePayments(String userId);
}
