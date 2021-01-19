package clientside.apis;

import clientside.data_access.IAccountService;
import clientside.models.User;

public class CustomerAPI {


    IAccountService accountService;

    public CustomerAPI(IAccountService accountService) {
        this.accountService = accountService;
    }

    public boolean createAccount(User user) throws Exception {
        return accountService.register(user);
    }


}
