package paymentserver;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("")
public class PaymentServer {
    static Map<String,User> users = new HashMap<>();
    BankService bank = new BankServiceService().getBankServicePort();

    @POST @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestPayment(Payment payment) {
        System.out.println("received payment request");
        String merchantId = payment.getMerchantId();
        String customerId = payment.getCustomerId();
        String tokenId = payment.getTokenId();
        System.out.println(users.size());
        users.forEach((key,value) -> System.out.println(key+": "+value));
        int amount = payment.getAmount();
        if (!users.containsKey(merchantId)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Merchant is not registered with DTUPay").build();
        }

        try {
            System.out.println("Received merchant id: "+merchantId);
            System.out.println("Received customer id: "+customerId);
            String merchantCpr = users.get(merchantId).getCprNumber();
            String customerCpr = users.get(customerId).getCprNumber();
            System.out.println("Received merchant cpr: "+merchantCpr);
            System.out.println("Received customer cpr: "+customerCpr);
            String creditor = bank.getAccountByCprNumber(merchantCpr).getId();
            String debtor = bank.getAccountByCprNumber(customerCpr).getId();
            bank.transferMoneyFromTo(debtor,creditor,new BigDecimal(amount),"Testing is not very fun");
            return Response.ok().build();
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getStackTrace()).build();
        }
    }


    @DELETE @Path("/users/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        users.remove(userId);
        return Response.ok().build();
    }

    @POST @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Customer user) {
        users.put(user.getUserId(),user);
        return Response.ok().build();
    }

    @POST @Path("/merchants")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Merchant user) {
        users.put(user.getUserId(),user);
        return Response.ok().build();
    }

}
