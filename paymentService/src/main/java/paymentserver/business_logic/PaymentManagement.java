package paymentserver.business_logic;

import paymentserver.models.Customer;
import paymentserver.models.Payment;
import paymentserver.models.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class PaymentManagement {
    Client client = ClientBuilder.newClient();
    WebTarget tokenServer = client.target("http://tokenserver:8181/");
    static Map<String, HashMap<String,Payment>> paymentHashMap = new HashMap<>();
    static Map<String, User> users = new HashMap<>();
    //Method used for validations before a payment is made
    public String validatePaymentInfo(Payment payment)
    {
        System.out.println("Validating payment: ");
        String customerId;
         //check if merchant and token are valid
         if (!users.containsKey(payment.getMerchantId()))
         {
             return "Merchant is not registered with DTUPay";
         }
         //find user by token
        customerId = findUserByToken(payment.getTokenId());
        if(!customerId.isEmpty() && !users.containsKey(customerId))
            return "Customer is not registered with DTUPay";
        //check token is used
        if(!validateToken(payment.getTokenId()))
            return "Token is already used";
        return "";
    }
    private boolean validateToken(String tokenId)
    {
        System.out.println("Validating Token: ");
        String response = tokenServer.path("Token/validate/"+tokenId).request().get(String.class);
        return Boolean.parseBoolean(response);
    }
    public Response consumeToken(String tokenId)
    {
        //TO DO modify to work with message queue?
        Response response = tokenServer.path("Token/ConsumedToken/"+tokenId).request().post(null);
        return response;
    }
    public String findUserByToken(String tokenId)
    {
        //TO DO modify to work with message queue?
        String response = tokenServer.path("Token/"+tokenId).request().get(String.class);
        return response;
    }
    public String getUserCPR(String userId)
    {
        if(!users.containsKey(userId))
        {
            return "";
        }
        return users.get(userId).getCprNumber();
    }
    public String removeUser(String userId)
    {
        users.remove(userId);
        if(users.containsKey(userId))
            paymentHashMap.remove(userId);
        else return "No payments";
        return "";
    }
    public void addUser(User user)
    {
        users.put(user.getUserId(),user);
    }
}
