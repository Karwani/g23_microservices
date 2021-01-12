package paymentserver;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("")
public class PaymentServer {
    List<UserInfo> users = new ArrayList<>();
    BankService bank = new BankServiceService().getBankServicePort();

    @POST @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPayment(Payment payment) {
        if (users.stream().noneMatch(user -> user.getCprNumber().equals(payment.getMerchantCpr()))){
            return Response.status(Response.Status.BAD_REQUEST).entity("Merchant is not registered with DTUPay").build();
        }
        try {
            String merchantCpr = payment.getMerchantCpr();
            String creditor = bank.getAccountByCprNumber(merchantCpr).getId();
            String customerCpr = payment.getCustomerCpr();
            String debtor = bank.getAccountByCprNumber(customerCpr).getId();
            bank.transferMoneyFromTo(debtor,creditor,payment.getAmount(),"Testing is fun");
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

    @POST @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserInfo user) {
        users.add(user);
        return Response.ok().build();
    }

}
