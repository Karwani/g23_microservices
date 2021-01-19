package clientside.apis;

import clientside.data_access.IAccountService;
import clientside.data_access.IPayService;
import clientside.models.User;

public class MerchantAPI {

    IAccountService accountService;
    IPayService payService;

    public MerchantAPI(IAccountService accountService, IPayService payService) {
        this.accountService = accountService;
        this.payService = payService;
    }

    public boolean createAccount(User user) throws Exception {
        return accountService.register(user);
    }

    public boolean pay(String merchantId, String customerId, String tokenId, int amount) throws Exception {
        return payService.pay(merchantId, customerId, tokenId, amount);
    }
}
