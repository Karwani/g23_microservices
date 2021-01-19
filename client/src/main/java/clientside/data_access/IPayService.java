package clientside.data_access;

import java.util.List;

public interface IPayService {

    boolean pay(String merchantId, String customerId, String tokenId, int amount) throws Exception;
    List<Object> getReport(String userId);
    void deletePayments(String userId);

}
