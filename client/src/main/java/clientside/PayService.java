package clientside;

import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonObject;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.stream.JsonParser;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.json.bind.serializer.*;
public class PayService {

    WebTarget baseUrl;
    List<Object> list = new ArrayList<Object>();
    public PayService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }
    public List<Object> getReport(String userId)
    {
        return baseUrl.path("payments/"+userId).request().get(list.getClass());
    }
    public void deletePayments(String userId)
    {
        baseUrl.path("payments/"+userId).request().delete();
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

}
