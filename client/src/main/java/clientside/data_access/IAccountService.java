package clientside.data_access;

import clientside.models.User;

public interface IAccountService {

    boolean register(User user) throws Exception;

    void deregister(User user);

    boolean validateDTUPayAccount(String userId);
    }
