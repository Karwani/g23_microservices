package com.gr_23.business_logic;

import com.gr_23.models.Customer;
import com.gr_23.models.User;

public interface IAccountManagement {


    Boolean createDTUPayAccount(User user);


    User fetchUser( String userId);

    boolean validateBankAccount(String CPR);


    boolean validateDTUPayAccount( String userId);

    Boolean deleteUsers(String userId);

    Boolean updateUser(User user);
}
