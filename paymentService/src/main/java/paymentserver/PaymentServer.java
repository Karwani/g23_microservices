package paymentserver;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import paymentserver.business_logic.PaymentManagement;
import paymentserver.business_logic.PaymentManagementFactory;
import paymentserver.models.Payment;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
   // WebTarget accountserver = client.target("http://accountserver:8383/");
    public PaymentServer() {
        paymentManagement = new PaymentManagementFactory().getService();
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


}
