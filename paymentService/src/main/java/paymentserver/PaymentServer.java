package paymentserver;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("")
public class PaymentServer {
    List<User> users = new ArrayList<>();
    BankService bank = new BankServiceService().getBankServicePort();

    @POST @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestPayment(Payment payment) {
        String merchantId = payment.getMerchantId();
        String customerId = payment.getCustomerId();
        String tokenId = payment.getTokenId();
        int amount = payment.getAmount();
        if (users.stream().noneMatch(user -> user.getUserId().equals(merchantId))){
            return Response.status(Response.Status.BAD_REQUEST).entity("Merchant is not registered with DTUPay").build();
        }

        try {
            String merchantCpr = users.stream()
                    .filter(user -> user.getUserId().equals(merchantId))
                    .findFirst()
                    .get().getCprNumber();
            String creditor = bank.getAccountByCprNumber(merchantCpr).getId();
            String customerCpr = users.stream()
                    .filter(user -> user.getUserId().equals(customerId))
                    .findFirst()
                    .get().getCprNumber();

            String debtor = bank.getAccountByCprNumber(customerCpr).getId();
            bank.transferMoneyFromTo(debtor,creditor,new BigDecimal(amount),"Testing is not very fun");
            return Response.ok().build();
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getStackTrace()).build();
        }
    }


    @DELETE @Path("/users/{cpr}")
    public Response deleteUser(@PathParam("cpr") String cpr) {
        users = users.stream().filter(user -> !user.getCprNumber().equals(cpr))
                .collect(Collectors.toList());
        return Response.ok().build();
    }

    @POST @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Customer user) {
        users.add(user);
        return Response.ok().build();
    }

    @POST @Path("/merchants")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Merchant user) {
        users.add(user);
        return Response.ok().build();
    }

}
