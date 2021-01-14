package com.gr_23;

import com.gr_23.business_logic.TokenRepository;
import com.gr_23.models.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("/Token")
public class TokenServer {
    private TokenRepository tokenRepository;

    public  TokenServer()
    {
        tokenRepository = new TokenRepository();
    }
    @POST
    @Path("/{userId}")
    public Response generateToken(@PathParam("userId") String userId) {
        if(tokenRepository.canGenerateTokensForUser(userId)) {
            tokenRepository.generateTokens(userId,5);
            return Response.ok().build();
        }
        return Response.notModified().build();
    }

    @GET
    @Path("/{tokenId}")
    public String validateToken(@PathParam("tokenId") String tokenId) {
        return tokenRepository.findUserByActiveToken(tokenId);
    }

    private void storeToken(Token token, String userId) {

    }

    @GET
    @Path("/Active/{userId}")
    public String requestActiveToken(@PathParam("userId") String userId) {
        //To DO: check if userid is a customer of DTUpay?
        generateToken(userId);
        return tokenRepository.getActiveToken(userId);
    }

    @POST
    @Path("ConsumedToken/{tokenId}")
    public boolean consumeToken(@PathParam("tokenId") String tokenId) {
       String user = tokenRepository.findUserByToken(tokenId);
       //System.out.println("Consumed user" + user);
        tokenRepository.removeTokenFromActive(tokenId, user);
        tokenRepository.addTokenToUsed(tokenId,user);
        return true;
    }

    @GET
    @Path("/isEligible/{userId}")
    public boolean isEligibleToGenerate(@PathParam("userId") String userId) {
        return tokenRepository.canGenerateTokensForUser(userId);
    }

    @DELETE
    @Path("/{userId}")
    public boolean deleteTokens(@PathParam("userId") String userId) {
        return tokenRepository.deleteTokens(userId);

    }

}
