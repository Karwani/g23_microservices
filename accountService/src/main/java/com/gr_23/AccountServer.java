package com.gr_23;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;

import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonObject;


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
      //  Customer one = new Customer("ole", "hansen", "333323-7777", "1", false);
        Customer two = new Customer("frank", "hansen", "444444-7777", "2", false);
        Customer three = new Customer("lisa", "hansen", "555555-7777", "3", false);
       // testUsers.put("1", one);
        testUsers.put("2", two);
        testUsers.put("3", three);
    }

    @PUT
    @Path("User")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDTUPayAccount(Customer user) throws Exception {
        customer = new Customer(user.getFirstName(), user.getLastName(), user.getCprNumber(), user.getUserId(), false);
        DTUpayUsers.put(customer.getUserId(), customer);
        return Response.ok().build();
    }

    // Remove @pathParam
    @GET
    @Path("Bank/{CPR}")
    public boolean validateBankAccount(@PathParam("CPR")String CPR) throws BankServiceException_Exception {
            try {
                bank.getAccountByCprNumber(CPR);
                return true;
            } catch (BankServiceException_Exception e) {
                return false;
            }
        }

    @GET
    @Path("DTUPay/{UserId}")
    public boolean validateDTUPayAccount(@PathParam("UserId")String userId) {

        return DTUpayUsers.containsKey(userId);
    }

    @DELETE
    @Path("User/{userId}")
    public Response deleteUsers(@PathParam("userId") String userId) {
        //customer.setUserId(userId);
        System.out.println("Delete function is called");
        if (DTUpayUsers.containsKey(customer.getUserId())) {
            DTUpayUsers.remove(userId);
            System.out.println("customer user id" + customer.getUserId());
            return Response.ok().build();
        }
        // DTU pay
        System.out.println("this should be empty " + customer.getUserId());
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
