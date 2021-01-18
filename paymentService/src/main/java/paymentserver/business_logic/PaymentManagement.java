package paymentserver.business_logic;

import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;
import paymentserver.models.Payment;
import paymentserver.models.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PaymentManagement implements EventReceiver {
    CompletableFuture<String> result;
    EventSender sender;
    Client client = ClientBuilder.newClient();
    WebTarget tokenServer = client.target("http://tokenserver:8181/");
    static Map<String, HashMap<String,Payment>> paymentHashMap = new HashMap<>();
    static Map<String, User> users = new HashMap<>();

    public PaymentManagement(EventSender b) {
        sender = b;
    }

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

        System.out.println("Sending message.");
        String result_S;
        try {
            result_S = makeRequest("validateToken",tokenId);
            System.out.println("Message sent.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Validating Token: ");
        String response = null;
        try {
            response = result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //String response = tokenServer.path("Token/validate/"+tokenId).request().get(String.class);
        return Boolean.parseBoolean(response);
    }
    public Response consumeToken(String tokenId)
    {

        //TODO modify to work with message queue?
        //
        Response response = tokenServer.path("Token/ConsumedToken/"+tokenId).request().post(null);
        return response;
    }


    public String findUserByToken(String tokenId)
    {
        //TODO modify to work with message queue?
        // Georg
        String message;
        System.out.println("findUserByToken message");

        try {
            System.out.println("Entered try: Sending FindUserbyToken");
            message = makeRequest("findUserByToken",tokenId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = null;

        try {

            response = result.get();

        } catch(Exception e) {
            e.printStackTrace();
        }

         //response = tokenServer.path("Token/"+tokenId).request().get(String.class);

        return response;
    }
    public String getUserCPR(String userId)
    {
        // TODO: use account service + message queue

        if(!users.containsKey(userId))
        {
            return "";
        }
        return users.get(userId).getCprNumber();
    }
    public String removeUser(String userId)
    {
        // TODO: migrate to account server
        users.remove(userId);
        if(users.containsKey(userId))
            paymentHashMap.remove(userId);
        else return "No payments";
        return "";
    }
    public void addUser(User user)
    {
        // TODO: migrate to account server
        users.put(user.getUserId(),user);
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("validateToken_done")) {
            System.out.println("PS event handled: "+event);
            result.complete(event.getArgument(0, String.class));

        } else {
            System.out.println("PS event ignored: "+event);
        }

        if(event.getEventType().equals("findUserByToken_done")) {
            System.out.println("PS findUserByToken_done event handled:" + event);
            result.complete(event.getArgument(0, String.class));
        }
    }

    public String makeRequest(String eventType, Object arg) throws Exception {
        Event event = new Event(eventType,new Object[] { arg });
        result = new CompletableFuture<>();
        sender.sendEvent(event);
        return result.join();
    }
    public void answerRequest(String eventType, Object arg) throws Exception {
        Event event = new Event(eventType,new Object[] { arg });
        sender.sendEvent(event);
    }

}
