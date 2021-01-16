package clientside;

import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PayService {

    WebTarget baseUrl;

    public PayService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public void register(User user, String userType) {
        String path = "";
        if (userType.equals("customer")){
            path = "customers";
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
        body.addProperty("admin",user.isAdmin());
        Response response = baseUrl.path(path).request()
                .post(Entity.entity(gson.toJson(body),MediaType.APPLICATION_JSON));
    }

    public void deregister(User user) {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("firstName",user.getFirstName());
        body.addProperty("lastName",user.getLastName());
        body.addProperty("cprNumber",user.getCprNumber());
        body.addProperty("userId",user.getUserId());
        body.addProperty("admin",user.isAdmin());
        Response response = baseUrl.path("users/"+user.getUserId()).request()
                .delete();
    }

    public boolean pay(String merchantId, String customerId, String tokenId, int amount) throws Exception {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("amount", amount);
        body.addProperty("customerId", customerId);
        body.addProperty("merchantId", merchantId);
        body.addProperty("tokenId", tokenId);
        Response response = baseUrl.path("payments").request().
                post(Entity.entity(gson.toJson(body), MediaType.APPLICATION_JSON));

//        System.out.println(response.readEntity(String.class));
//        System.out.println("PAY: response status:" + response.getStatus());
        System.out.println("token is: "+tokenId);
        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String e = response.readEntity(String.class);
            System.out.println(e);
            throw new Exception(e);
        }

        return response.getStatus() == Response.Status.OK.getStatusCode();
    }

    public boolean checkUser(String userId)
    {
        return baseUrl.path("users/"+userId).request().get(Boolean.TYPE);
    }
//    public List<Payment> getPayments() {
//        return baseUrl.path("payments").request()
//                .get(new GenericType<List<Payment>>(){});
//    }
}
