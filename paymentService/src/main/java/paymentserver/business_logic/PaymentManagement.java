package paymentserver.business_logic;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;
import paymentserver.data_access.IPaymentRepository;
import paymentserver.models.Payment;
import paymentserver.models.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PaymentManagement implements EventReceiver, IPaymentManagement {
    IPaymentRepository paymentRepository;
    BankService bank = new BankServiceService().getBankServicePort();
    HashMap<String,CompletableFuture<String>> results = new HashMap<>();
    String foundResult = "";
    EventSender sender;

    public PaymentManagement(EventSender eventSender, IPaymentRepository paymentRepository) {
        sender = eventSender;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void initiateDTUPay(Payment payment) throws ValidationException,BankServiceException_Exception
    {
        String merchantId = payment.getMerchantId();
        String tokenId = payment.getTokenId();
        int amount = payment.getAmount();
        String error = validatePaymentInfo(payment);
        if(!error.isEmpty())
            throw new ValidationException(error);
        String customerId = findUserByToken(tokenId);
        String merchantCpr = getUserCPR(merchantId);
        String customerCpr = getUserCPR(customerId);
        //** Bank transfer method - move to transaction service
        makeTransactionInBank(amount, merchantCpr, customerCpr);
        //**
        savePaymentInDTUPay(payment, customerId);
    }

    private void savePaymentInDTUPay(Payment payment, String customerId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        payment.setDescription("Order made on "+ dtf.format(now)+ " for amount " + payment.getAmount());
        payment.setCustomerId(customerId);
        paymentRepository.addPayment(payment);
    }

    private void makeTransactionInBank(int amount, String merchantCpr, String customerCpr) throws BankServiceException_Exception {
        String creditor = bank.getAccountByCprNumber(merchantCpr).getId();
        String debtor = bank.getAccountByCprNumber(customerCpr).getId();
        bank.transferMoneyFromTo(debtor,creditor,new BigDecimal(amount),"Testing is not very fun");
    }

    private boolean checkDTUPayAccount(String userId) {
        String valid = sendMsgAndWaitForAnswer(userId,"validateDTUPayAccount");
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
        String result = sendMsgAndWaitForAnswer(tokenId,"validateToken");
        return Boolean.parseBoolean(result);
    }

    @Override
    public Response consumeToken(String tokenId)
    {
        Boolean response = Boolean.parseBoolean(sendMsgAndWaitForAnswer(tokenId,"consumeToken"));
        if(response) { return Response.ok().build(); }
        else {return Response.status(Response.Status.BAD_REQUEST).build();}
    }

    @Override
    public List<Payment> getPayments(String userId) {
        return paymentRepository.getPaymentList(userId);
    }

    @Override
    public void deletePayments(String userId) {
        paymentRepository.deletePayments(userId);
    }


    public String findUserByToken(String tokenId)
    {
        return sendMsgAndWaitForAnswer(tokenId,"findUserByToken");
    }

    private String sendMsgAndWaitForAnswer(String arg, String eventType) {
        String key = getKey(eventType, arg);
        results.put(key,new CompletableFuture<>());
        new Thread(() -> {try {
            results.get(key).complete(makeRequest(eventType, arg));
        } catch (Exception e) {
            throw new Error(e);
        }}).start();

        String response = null;
        try {
            response = results.get(key).get();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getUserCPR(String userId)
    {
        String userCPR = sendMsgAndWaitForAnswer(userId,"fetchUser");
        if (userCPR != null) {

            return userCPR;
        }
        else return "";
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        if(!event.getEventType().endsWith("done"))
            return;
        String key = event.getEventType().replace("done",event.getArgument(1,String.class));
        CompletableFuture<String> result = results.get(key);
        if (event.getEventType().equals("validateToken_done")) {
            result.complete(event.getArgument(0, String.class));

        }
        if(event.getEventType().equals("findUserByToken_done")) {
            result.complete(event.getArgument(0, String.class));
        }
        if(event.getEventType().equals("consumeToken_done")) {
            result.complete(event.getArgument(0,String.class));
        }
        if(event.getEventType().equals("validateDTUPayAccount_done")) {
            result.complete(event.getArgument(0,String.class));
        }
        if(event.getEventType().equals("fetchUser_done")) {
            result.complete(event.getArgument(0,String.class));
        }
    }

    private String makeRequest(String eventType, Object arg) {
        Event event = new Event(eventType,new Object[] { arg });
        String key = getKey(eventType,arg);
        try {
            sender.sendEvent(event);
            return results.get(key).join();
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return  "";
        }
    }
    private String getKey(String eventType, Object arg)
    {
        return eventType+"_"+arg.toString();
    }
}
