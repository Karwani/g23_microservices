package paymentserver;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import paymentserver.business_logic.IPaymentManagement;
import paymentserver.business_logic.PaymentManagement;
import paymentserver.business_logic.PaymentManagementFactory;
import paymentserver.models.Payment;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.List;

@Path("")
public class PaymentServer {
    IPaymentManagement paymentManagement;


    public PaymentServer() {
        paymentManagement = new PaymentManagementFactory().getService();
    }

    @POST @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestPayment(Payment payment) {
        try {
            paymentManagement.initiateDTUPay(payment);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getStackTrace()).build();
        } catch (ValidationException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        return paymentManagement.consumeToken(payment.getTokenId());
        //return  Response.ok().build();
    }
    @GET
    @Path("/payments/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Payment> getPaymentList(@PathParam("userId") String userId) {
        return paymentManagement.getPayments(userId);
    }
    @DELETE
    @Path("/payments/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deletePayments(@PathParam("userId") String userId) {
        paymentManagement.deletePayments(userId);
    }

}
