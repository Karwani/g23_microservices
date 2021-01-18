package com.gr_23;

import com.gr_23.business_logic.ITokenManagement;
import com.gr_23.business_logic.TokenManagement;
import com.gr_23.business_logic.TokenManagementFactory;
import com.gr_23.data_access.ITokenRepository;
import com.gr_23.models.Token;
import messaging.EventReceiver;
import messaging.EventSender;
import messaging.rabbitmq.RabbitMqListener;
import messaging.rabbitmq.RabbitMqSender;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.inject.*;
@Path("/Token")
public class TokenServer {

    private ITokenManagement tokenManagement;

//    public  TokenServer(ITokenManagement tokenManagement)
//    {
//        this.tokenManagement = tokenManagement;
//
//    }
    public TokenServer()
    {
        this.tokenManagement = new TokenManagementFactory().getService();
    }
    @POST
    @Path("/{userId}")
    public Response generateToken(@PathParam("userId") String userId) {
        if(tokenManagement.generateTokensForUser(userId,5)) {
            return Response.ok().build();
        }
        return Response.notModified().build();
    }

    @GET
    @Path("/validate/{tokenId}")
    public String validateToken(@PathParam("tokenId") String tokenId)
    {
        System.out.println("Result of validateToken:" + tokenManagement.validateToken(tokenId));
        boolean bool = tokenManagement.validateToken(tokenId);
        return String.valueOf(bool);
    }

    @GET
    @Path("/{tokenId}")
    public String FindUserByActiveToken(@PathParam("tokenId") String tokenId)
    {
        return tokenManagement.findUserByActiveToken(tokenId);
    }

    @GET
    @Path("/Active/{userId}")
    public String requestActiveToken(@PathParam("userId") String userId) {
        //To DO: check if userid is a customer of DTUpay?
        return tokenManagement.getActiveToken(userId);
    }

    @POST
    @Path("ConsumedToken/{tokenId}")
    public boolean consumeToken(@PathParam("tokenId") String tokenId) {
        return tokenManagement.consumeToken(tokenId);
    }

    @GET
    @Path("/isEligible/{userId}")
    public boolean isEligibleToGenerate(@PathParam("userId") String userId) {
        return tokenManagement.canGenerateTokensForUser(userId);
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteTokens(@PathParam("userId") String userId) {
         tokenManagement.deleteTokens(userId);
         return Response.ok().build();

    }

}
