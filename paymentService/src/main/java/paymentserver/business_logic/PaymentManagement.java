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

    public PaymentManagement(EventSender b) {
        sender = b;
    }


    private boolean checkDTUPayAccount(String userId) {
        try {
            makeRequest("validateDTUPayAccount", userId);
        } catch (Exception e){
            e.printStackTrace();
        }
        String valid = "";
        try {
            valid = result.get();
        } catch (Exception e){
            e.printStackTrace();
        }

        return Boolean.parseBoolean(valid);
    }

    //Method used for validations before a payment is made
    //check if merchant and token are valid

    public String validatePaymentInfo(Payment payment)
    {
        String customerId;
         if (!checkDTUPayAccount(payment.getMerchantId()))
         {
             return "Merchant is not registered with DTUPay";
         }
        customerId = findUserByToken(payment.getTokenId());
        if(!customerId.isEmpty() && !checkDTUPayAccount(customerId))
            return "Customer is not registered with DTUPay";
        if(!validateToken(payment.getTokenId()))
            return "Token is already used";
        return "";
    }
    private boolean validateToken(String tokenId)
    {
        try {
            makeRequest("validateToken",tokenId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = null;
        try {
            response = result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Boolean.parseBoolean(response);
    }

    public Response consumeToken(String tokenId)
    {
        //TODO modify to work with message queue?

        System.out.println("Sending message consume token");

        try {
            makeRequest("consumeToken",tokenId);
            System.out.println("consumeTokenMessage sent");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Boolean response = null;

        try {

           response = Boolean.parseBoolean(result.get());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(response) { return Response.ok().build(); }
        else {return Response.status(Response.Status.BAD_REQUEST).build();}
    }


    public String findUserByToken(String tokenId)
    {
        System.out.println("findUserByToken message");

        try {
            System.out.println("Entered try: Sending FindUserbyToken");
            makeRequest("findUserByToken",tokenId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = null;

        try {

            response = result.get();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getUserCPR(String userId)
    {
        try {
            makeRequest("fetchUser", userId);
        } catch (Exception e){
            e.printStackTrace();
        }
        String userCPR = "";
        try {
            userCPR = result.get();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (userCPR != null) {

            return userCPR;
        }
        else return "";
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

        if(event.getEventType().equals("consumeToken_done")) {
            System.out.println("PS consumeToken_done event handled:" + event);
            result.complete(event.getArgument(0,String.class));
        }
        if(event.getEventType().equals("validateDTUPayAccount_done")) {
            System.out.println("AS validateDTUPayAccount_done event handled:" + event);
            result.complete(event.getArgument(0,String.class));
        }
        if(event.getEventType().equals("fetchUser_done")) {
            System.out.println("AS fetch user event handled:" + event);
            result.complete(event.getArgument(0,String.class));
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
