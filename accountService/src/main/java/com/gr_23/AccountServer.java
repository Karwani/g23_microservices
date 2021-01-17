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

    private Map<String, User> users = new HashMap<>();
    BankService bank = new BankServiceService().getBankServicePort();
    List<User> registeredUsers = new ArrayList<>();
    List<String> accountIds = new ArrayList<>();
    User customer;
    User merhcant;
   // WebTarget baseUrl;
   private Map<String, User> testUsers = new HashMap<>();


    public AccountServer(){
//        Client client = ClientBuilder.newClient();
//        baseUrl = client.target("http://localhost:8383/");
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
        System.out.println("hallo");
        UUID uuid = UUID.randomUUID();
        customer = new Customer(user.getFirstName(), user.getLastName(), user.getCprNumber(), user.getUserId(), false);
        users.put(customer.getUserId(), customer);
        return Response.ok().build();
    }

    // Remove @pathParam
    @GET
    @Path("Bank/{CPR}")
    public boolean validateBankAccount(@PathParam("CPR")String CPR) throws BankServiceException_Exception {
       // String cprFromBank = bank.getAccountByCprNumber(CPR).getUser().getCprNumber();
       // System.out.println("cpr from bank " + cprFromBank);

//        for (Map.Entry<String, User> entry : users.entrySet()){
//            System.out.println("the value "+ entry.getValue().getCprNumber().contentEquals(cprFromBank));
//            // below for functional application
//           // if (entry.getValue().getCprNumber().contentEquals(cprFromBank)){
//            if (entry.getValue().getCprNumber().equals(cprFromBank)){
//                System.out.println("users map " +entry.getValue().getCprNumber() + " input string " +CPR);
//                return true;
//            }
            try {
                System.out.println("JENKINS TEST WILL BE REMOVED");
                bank.getAccountByCprNumber(CPR);
                return true;
            } catch (BankServiceException_Exception e) {
                return false;
            }
        }

    @GET
    @Path("DTUPay")
    public boolean validateDTUPayAccount(String CPR) {


        return users.containsKey(customer.getUserId());
    }

    @DELETE
    @Path("User/{userId}")
    public Response deleteUsers(@PathParam("userId") String userId) {
        Customer one = new Customer("ole", "hansen", "333323-7777", "1", false);
        testUsers.put("1", one);

        if (testUsers.containsKey(one.getUserId())) {
            testUsers.remove(userId);
            return Response.ok().build();
        }
        // DTU pay
        System.out.println(one.getUserId());
        return Response.notModified().build();
    }

    @POST
    @Path("User")
    public void updateUser(User user) {
        Customer one = new Customer("ole", "hansen", "333323-7777", "1", false);
        testUsers.put("1", one);
        System.out.println(one.getFirstName());
        one.setFirstName(user.getFirstName());
        one.setLastName(user.getLastName());
        if (!testUsers.containsKey(one.getUserId())){
            System.out.println("User with id " + one.getUserId() + " does not exist");
            System.out.println("User with id " + user.getUserId() + " does not exist");

        }
        testUsers.put(one.getUserId(), one);
        System.out.println("after update " + one.getFirstName());

    }

}
