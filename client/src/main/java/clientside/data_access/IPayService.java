package clientside.data_access;

public interface IPayService {

    boolean pay(String merchantId, String customerId, String tokenId, int amount) throws Exception;
    }
