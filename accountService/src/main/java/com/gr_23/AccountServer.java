package com.gr_23;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;


@Path("/Account/")
public class AccountServer {

    private Map<String, User> DTUpayUsers = new HashMap<>();
    BankService bank = new BankServiceService().getBankServicePort();
    List<User> registeredUsers = new ArrayList<>();
    List<String> accountIds = new ArrayList<>();
    User customer;
    User merhcant;
   private Map<String, User> testUsers = new HashMap<>();
   dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();


    public AccountServer(){
    }

    @PUT
    @Path("User")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDTUPayAccount(Customer user) throws Exception {
        User customer = new Customer(user.getFirstName(), user.getLastName(), user.getCprNumber(), user.getUserId(), false);
        DTUpayUsers.put(customer.getUserId(), customer);
        return Response.ok().build();
    }

    @GET
    @Path("User/{userId}")
    public User fetchUser(@PathParam("userId") String userId){

        return DTUpayUsers.get(userId);
    }

    @GET
    @Path("Bank/{CPR}")
    public boolean validateBankAccount(@PathParam("CPR") String CPR) throws BankServiceException_Exception {
            try {
                System.out.println("JENKINS TEST WILL BE REMOVED");
                bank.getAccountByCprNumber(CPR);
                return true;
            } catch (BankServiceException_Exception e) {
                return false;
            }
        }

    @GET
    @Path("DTUPay/{UserId}")
    public boolean validateDTUPayAccount(@PathParam("UserId") String userId) {

        return DTUpayUsers.containsKey(userId);
    }

    @DELETE
    @Path("User/{userId}")
    public Response deleteUsers(@PathParam("userId") String userId) {
        //customer.setUserId(userId);
        System.out.println("Delete function is called");
        if (DTUpayUsers.containsKey(userId)) {
            DTUpayUsers.remove(userId);
            System.out.println("customer user id" + userId);
            return Response.ok().build();
        }
        // DTU pay
        System.out.println("this should be empty " + userId);
        return Response.notModified().build();
    }

    @POST
    @Path("User/")
    public void updateUser(User user) {
        System.out.println("before update " + customer.getFirstName());
        customer.setFirstName(user.getFirstName());
        customer.setLastName(user.getLastName());
        if (!DTUpayUsers.containsKey(customer.getUserId())){
            System.out.println("User with id " + customer.getUserId() + " does not exist");
            System.out.println("User with id " + user.getUserId() + " does not exist");
        }
        DTUpayUsers.put(customer.getUserId(), customer);
        System.out.println("after update " + customer.getFirstName());
    }
}
