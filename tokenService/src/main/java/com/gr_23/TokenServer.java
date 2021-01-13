package com.gr_23;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.Map;
import java.util.List;

@Path("/Token")
public class TokenServer {

    private Map<String, List<Token>> activeTokens;

    private Map<String, List<Token>> usedTokens;

    @POST
    @Path("/{userId}")
    public void generateToken(@PathParam("userId") String userId) {

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
        return null;
    }

    @POST
    @Path("ConsumedToken/{tokenId}")
    public boolean consumeToken(@PathParam("tokenId") String tokenId) {
        return true;
    }



}
