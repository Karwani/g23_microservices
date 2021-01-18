package com.gr_23.business_logic;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.gr_23.User;
import com.gr_23.Customer;
import dtu.ws.fastmoney.BankServiceException_Exception;

public interface IAccountManagement {


    Boolean createDTUPayAccount(Customer user) throws Exception;


    User fetchUser( String userId);

    boolean validateBankAccount(String CPR) throws BankServiceException_Exception;


    boolean validateDTUPayAccount( String userId);

    Boolean deleteUsers(String userId);

    void updateUser(User user);
}
