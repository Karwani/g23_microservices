package com.gr_23;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

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

        return findUserByToken(tokenId);
    }

    private String findUserByToken(String tokenId) {
        for( Map.Entry<String, List<Token>> pair : activeTokens.entrySet()) {
            Boolean holder = pair.getValue().stream().map(token -> token.getTokenId()).filter(t -> t.equals(tokenId)).findFirst().isPresent();

            if (holder) {
                return pair.getKey();
            }
        }
        return "";
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

       String user = findUserByToken(tokenId);

       System.out.println("Consumed user" + user);

       List<Token> temp = activeTokens.get(user).stream().filter(token -> !token.getTokenId().equals(tokenId)).collect(Collectors.toList());

       System.out.println("temporary deleted" + temp.toString());

       activeTokens.remove(user);
       activeTokens.put(user,temp);

       if(usedTokens.containsKey(user)) {
           usedTokens.get(user).add(new Token(tokenId, true));
       }
       else {
            usedTokens.put(user,new ArrayList<Token>());
            usedTokens.get(user).add(new Token(tokenId, true));
       }

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

         if(usedTokens.get(userId) != null)
         {
             usedTokens.remove(userId);
         }
         return true;
        }


        return false;
    }

}
