package com.gr_23.accountTest;

import com.gr_23.User;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AccountService {
    WebTarget baseUrl;

    public AccountService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8383/");
    }

    //calls the update function in accountServer
    public boolean register(User user, String userType) throws Exception {
        String path = "";
        if (userType.equals("customer")){
            path = "User"; // should be changed to "User"
        }
        if (userType.equals("merchant")){
            path = "merchants";
        }
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("firstName",user.getFirstName());
        body.addProperty("lastName",user.getLastName());
        body.addProperty("cprNumber",user.getCprNumber());
        body.addProperty("userId",user.getUserId());
        System.out.println(user.getUserId());
        body.addProperty("admin",user.isAdmin());
        Response response = baseUrl.path("Account/" + path).request()
                .put(Entity.entity(gson.toJson(body), MediaType.APPLICATION_JSON));

        System.out.println("from register function" + response.getStatus());
        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String read = response.readEntity(String.class);
            System.out.println(read);
            throw new Exception(read);
        }

        return response.getStatus() == Response.Status.OK.getStatusCode();
    }

    public boolean validateAccount(String cpr) {
        Boolean response = baseUrl.path("Account/Bank/" + cpr).request()
                .get(Boolean.TYPE);

        if (response != true){
            System.out.println("Response was wrong " + response);
        }
        return response;
    }

    public boolean validateDTUPayAccount(String userId) {
        Boolean response = baseUrl.path("Account/DTUPay/" + userId).request()
                .get(Boolean.TYPE);
        if (response != true){
            System.out.println("Response was wrong " + response);
        }
        return response;
    }

    public void updateUser(User user) {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("firstName",user.getFirstName());
        body.addProperty("lastName",user.getLastName());
        body.addProperty("cprNumber",user.getCprNumber());
        body.addProperty("userId",user.getUserId());
        body.addProperty("admin",user.isAdmin());
        Response response = baseUrl.path("Account/User").request()
                .post(Entity.entity(gson.toJson(body), MediaType.APPLICATION_JSON));
    }

    public void deleteuser(User user) {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("firstName",user.getFirstName());
        body.addProperty("lastName",user.getLastName());
        body.addProperty("cprNumber",user.getCprNumber());
        body.addProperty("userId",user.getUserId());
        body.addProperty("admin",user.isAdmin());
        Response response = baseUrl.path("Account/User/" + user.getUserId()).request()
                .delete();
        System.out.println("Response from delete function " + response);
    }


    public void deregister(User user) {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("firstName",user.getFirstName());
        body.addProperty("lastName",user.getLastName());
        body.addProperty("cprNumber",user.getCprNumber());
        body.addProperty("userId",user.getUserId());
        body.addProperty("admin",user.isAdmin());
        Response response = baseUrl.path("Accounts/user/"+user.getUserId()).request()
                .delete();
    }
}
