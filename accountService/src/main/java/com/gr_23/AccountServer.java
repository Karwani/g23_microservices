package com.gr_23;

import javax.ws.rs.*;
import java.util.List;

@Path("Account/")
public class AccountServer {
    List<User> registeredUsers;


    @PUT
    @Path("User")
    public boolean createDTUPayAccount(String firstname, String lastname, String CPR, String userType) {

        return false;
    }

    @GET
    @Path("Bank")
    public boolean validateBankAccount(String CPR) {
        return false;
    }

    @GET
    @Path("Pay")
    public boolean validateDTUPayAccount(String CPR) {
        return false;
    }

    @DELETE
    @Path("")
    public void deleteUsers(String CPR) {
    }

    @POST
    @Path("User")
    public void updateUser(User user) {
    }
}
