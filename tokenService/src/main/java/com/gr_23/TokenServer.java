package com.gr_23;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.*;
import java.util.List;

@Path("/Token")
public class TokenServer {

    private static Map<String, List<Token>> activeTokens = new HashMap<String, List<Token>>();

    private static Map<String, List<Token>> usedTokens = new HashMap<String, List<Token>>();


    @POST
    @Path("/{userId}")
    public Response generateToken(@PathParam("userId") String userId) {
        List<Token> tokens = new ArrayList<Token>();
        if(isEligibleToGenerate(userId)) {
            for (int i = 0; i < 5; i++) {
                String uuid = UUID.randomUUID().toString();
                tokens.add(new Token(uuid, false));
            }
            if (activeTokens.get(userId) == null) {
                activeTokens.put(userId, tokens);
            } else {
                activeTokens.get(userId).addAll(tokens);
            }
            return Response.ok().build();
        }
        return Response.notModified().build();
    }

    @GET
    @Path("/{tokenId}")
    public String validateToken(@PathParam("tokenId") String tokenId) {
        return null;
    }

    private void storeToken(Token token, String userId) {

    }

    @GET
    @Path("/Active/{userId}")
    public String requestActiveToken(@PathParam("userId") String userId) {
        //To DO: check if userid is a customer of DTUpay?
        generateToken(userId);
        return activeTokens.get(userId).stream().findFirst().get().getTokenId();
    }

    @POST
    @Path("ConsumedToken/{tokenId}")
    public boolean consumeToken(@PathParam("tokenId") String tokenId) {
        return true;
    }

    @GET
    @Path("/isEligible/{userId}")
    public boolean isEligibleToGenerate(@PathParam("userId") String userId) {
        if(activeTokens.get(userId)!=null)
            return activeTokens.get(userId).size()<=1;
        return true;
    }

    @DELETE
    @Path("/{userId}")
    public boolean deleteTokens(@PathParam("userId") String userId) {
        if(activeTokens.get(userId)!=null)
        {
         activeTokens.remove(userId);
         return true;
        }
        return false;
    }

}
