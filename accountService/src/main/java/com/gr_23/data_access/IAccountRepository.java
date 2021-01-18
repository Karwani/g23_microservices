package com.gr_23.data_access;

import com.gr_23.models.Customer;
import com.gr_23.models.User;

public interface IAccountRepository {
    Boolean addUser(Customer user);

    User fetchUser(String userId);

    Boolean updateUser(User user);

    Boolean deleteUsers(String userId);
    Boolean userExist(String userId);


}
