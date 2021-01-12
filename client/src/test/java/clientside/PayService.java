package clientside;

import dtu.ws.fastmoney.BankService;
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

    public PayService(BankService bank) {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public void register(UserInfo user) {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("cprNumber",user.getCprNumber());
        body.addProperty("firstName",user.getFirstName());
        body.addProperty("lastName",user.getLastName());
        Response response = baseUrl.path("users").request()
                .post(Entity.entity(gson.toJson(body),MediaType.APPLICATION_JSON));
    }

    public void deregister(UserInfo user) {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("cprNumber",user.getCprNumber());
        body.addProperty("firstName",user.getFirstName());
        body.addProperty("lastName",user.getLastName());
        Response response = baseUrl.path("users/"+user.getCprNumber()).request()
                .delete();
    }

    public boolean pay(Payment payment) throws Exception {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("amount", payment.getAmount());
        body.addProperty("customerCpr", payment.getCustomerCpr());
        body.addProperty("merchantCpr", payment.getMerchantCpr());
        Response response = baseUrl.path("payments").request().
                post(Entity.entity(gson.toJson(body), MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new Exception(response.readEntity(String.class));
        }
        return response.getStatus() == Response.Status.OK.getStatusCode();
    }

//    public List<Payment> getPayments() {
//        return baseUrl.path("payments").request()
//                .get(new GenericType<List<Payment>>(){});
//    }
}
