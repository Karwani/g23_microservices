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

    static Map<String, User> users = new HashMap<>();
    BankService bank = new BankServiceService().getBankServicePort();
    List<User> registeredUsers = new ArrayList<>();
    List<String> accountIds = new ArrayList<>();
    User customer;
    User merhcant;
   // WebTarget baseUrl;


    public AccountServer(){
//        Client client = ClientBuilder.newClient();
//        baseUrl = client.target("http://localhost:8383/");
    }

    @PUT
    @Path("User")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDTUPayAccount(Customer user) throws Exception {
        System.out.println("hallo");
        UUID uuid = UUID.randomUUID();
        customer = new Customer(user.getFirstName(), user.getLastName(), user.getCprNumber(), user.getUserId(), false);
        users.put(customer.getUserId(), customer);
        return Response.ok().build();
    }

    // TO DO: Return userInfo based on Userid

    @GET
    @Path("Bank")
    public boolean validateBankAccount(String CPR) {
       // bank.getAccountByCprNumber(CPR);
        return false;
    }

    @GET
    @Path("DTUPay")
    public boolean validateDTUPayAccount(String CPR) {


        return users.containsKey(customer.getUserId());
    }

    @DELETE
    @Path("User/{userId}")
    public Response deleteUsers(@PathParam("userId") String userId) {
        // DTU pay
        users.remove(userId);
        return Response.ok().build();
    }

    @POST
    @Path("User")
    public void updateUser(User user) {
        //
    }

}
