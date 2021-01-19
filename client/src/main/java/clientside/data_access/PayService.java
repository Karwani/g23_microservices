package clientside.data_access;

import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PayService implements IPayService{

    WebTarget baseUrl;

    public PayService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    @Override
    public boolean pay(String merchantId, String customerId, String tokenId, int amount) throws Exception {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("amount", amount);
        body.addProperty("customerId", customerId);
        body.addProperty("merchantId", merchantId);
        body.addProperty("tokenId", tokenId);
        Response response = baseUrl.path("payments").request().
                post(Entity.entity(gson.toJson(body), MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String e = response.readEntity(String.class);
            System.out.println(e);
            throw new Exception(e);
        }

        return response.getStatus() == Response.Status.OK.getStatusCode();
    }

}
