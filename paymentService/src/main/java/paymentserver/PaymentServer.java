package paymentserver;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import paymentserver.business_logic.PaymentManagement;
import paymentserver.models.Customer;
import paymentserver.models.Merchant;
import paymentserver.models.Payment;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("")
public class PaymentServer {
    PaymentManagement paymentManagement;
    BankService bank = new BankServiceService().getBankServicePort();

    Client client = ClientBuilder.newClient();
    WebTarget baseUrl = client.target("http://tokenserver:8181/");  // <--- use when running in docker
    //WebTarget baseUrl = client.target("http://localhost:8181/");  // <---- use when testing locally
    public PaymentServer() {
        paymentManagement = new PaymentManagement();
    }

    @POST @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestPayment(Payment payment) {
        System.out.println("received payment request");
        String merchantId = payment.getMerchantId();
        String tokenId = payment.getTokenId();

        //String customerId = payment.getCustomerId(); // instead of this, need to make call to token service to get customer from the token
        //System.out.println(users.size());
        //users.forEach((key,value) -> System.out.println(key+": "+value));
        int amount = payment.getAmount();
        String error = paymentManagement.validatePaymentInfo(payment);
        if(!error.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        String customerId = paymentManagement.findUserByToken(tokenId);
        try {
            System.out.println("Received merchant id: "+payment.getMerchantId());
            System.out.println("Received customer id: "+payment.getCustomerId());
            String merchantCpr = paymentManagement.getUserCPR(merchantId);
            String customerCpr = paymentManagement.getUserCPR(customerId);
            System.out.println("Received merchant cpr: "+merchantCpr);
            System.out.println("Received customer cpr: "+customerCpr);
            //** Bank transfer method - move to transaction service
            String creditor = bank.getAccountByCprNumber(merchantCpr).getId();
            String debtor = bank.getAccountByCprNumber(customerCpr).getId();
            bank.transferMoneyFromTo(debtor,creditor,new BigDecimal(amount),"Testing is not very fun");
            //**
            return paymentManagement.consumeToken(tokenId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getStackTrace()).build();
        }
    }

    @DELETE @Path("/users/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        String error = paymentManagement.removeUser(userId);
        if(error.isEmpty())
            return Response.ok().build();
        return Response.notModified(error).build();
    }

    @POST @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Customer user) {
        paymentManagement.addUser(user);
        return Response.ok().build();
    }

    @POST @Path("/merchants")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Merchant user) {
        paymentManagement.addUser(user);
        return Response.ok().build();
    }

    @GET @Path("/users/{userId}")
    public Boolean checkUser(@PathParam("userId") String userId) {
        String userCPR = paymentManagement.getUserCPR(userId);
        return !userCPR.isEmpty();
    }


}
