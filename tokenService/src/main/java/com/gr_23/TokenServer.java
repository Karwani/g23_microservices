package com.gr_23;

import com.gr_23.business_logic.ITokenManagement;
import com.gr_23.business_logic.TokenManagementFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
@Path("/Token")
public class TokenServer {

    private ITokenManagement tokenManagement;

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
        boolean bool = tokenManagement.validateToken(tokenId);
        return String.valueOf(bool);
    }

    @GET
    @Path("/{tokenId}")
    public String findUserByActiveToken(@PathParam("tokenId") String tokenId)
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

    @POST
    @Path("GenerateByToken/{tokenId}")
    public Response GenerateByToken(@PathParam("tokenId") String tokenId) {
        if(tokenManagement.generateTokensForUser(tokenId,1)) {
            return Response.ok().build();
        }
        return Response.notModified().build();
    }

}
