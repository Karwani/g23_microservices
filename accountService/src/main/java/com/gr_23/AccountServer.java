package com.gr_23;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

public class AccountServer {
    List<User> registeredUsers;
    @POST
    @Path("User")
    public boolean createDTUPayAccount(User userOne, String userType) {

        return false;
    }

    public boolean validateBankAccount(String CPR) {
        return false;
    }

    public boolean validateDTUPayAccount(String CPR) {
        return false;
    }

    public void deleteUsers(String CPR) {
    }

    public void updateUser(User user) {
    }
}
