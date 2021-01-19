package com.gr_23;

import com.gr_23.business_logic.AccountManagementFactory;
import com.gr_23.business_logic.IAccountManagement;
import com.gr_23.models.Customer;
import com.gr_23.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/Account/")
public class AccountServer {

   private IAccountManagement accountManagement;


    public AccountServer(){
        this.accountManagement = new AccountManagementFactory().getService();
    }

    @PUT
    @Path("User")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDTUPayAccount(Customer user){
        if (accountManagement.createDTUPayAccount(user)){
            return Response.ok().build();
        }
        return Response.notModified().build();
    }

    @GET
    @Path("User/{userId}")
    public User fetchUser(@PathParam("userId") String userId){
        return accountManagement.fetchUser(userId);
    }

    @GET
    @Path("Bank/{CPR}")
    public boolean validateBankAccount(@PathParam("CPR") String CPR) {
        return  accountManagement.validateBankAccount(CPR);
    }

    @GET
    @Path("DTUPay/{UserId}")
    public boolean validateDTUPayAccount(@PathParam("UserId") String userId) {
        System.out.println("accountServer");
        return accountManagement.validateDTUPayAccount(userId);
    }

    @DELETE
    @Path("User/{userId}")
    public Response deleteUsers(@PathParam("userId") String userId) {
        if(accountManagement.deleteUsers(userId))
        {
            return Response.ok().build();
        }
        return Response.notModified().build();
    }

    @POST
    @Path("User/")
    public Response updateUser(User user) {
        if (accountManagement.updateUser(user)){
            return Response.ok().build();
        }
       return Response.notModified().build();
    }
}
