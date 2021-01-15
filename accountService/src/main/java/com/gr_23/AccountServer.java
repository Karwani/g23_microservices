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


@Path("Account/")
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
    public Response createDTUPayAccount(UserInfo userInfo) throws Exception {
        UUID uuid = UUID.randomUUID();
        //String firstName = userInfo


        customer = new Customer(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getCprNumber(), uuid.toString(), false);
        System.out.println(customer.getCprNumber());
        dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setCprNumber(userInfo.getCprNumber());
        System.out.println(customer.getCprNumber());


        try {
            String accountId = bank.createAccountWithBalance(user, new BigDecimal(1000));
            accountIds.add(accountId);
            //register(customer, "customer");
            registeredUsers.add(customer);
            System.out.println(customer.getCprNumber());
            if (!accountIds.contains(accountId)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("the customer did not get registered")
                        .build();
            }
            // add registration

        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            throw new Exception();
        }

        return Response.ok().build();
    }

    @GET
    @Path("Bank")
    public boolean validateBankAccount(String CPR) {
        return false;
    }

    @GET
    @Path("DTUPay")
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
