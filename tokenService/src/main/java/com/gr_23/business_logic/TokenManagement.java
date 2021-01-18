package com.gr_23.business_logic;

import com.gr_23.data_access.ITokenRepository;
import com.gr_23.models.Token;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.CompletableFuture;

//@ApplicationScoped
public class TokenManagement implements ITokenManagement, EventReceiver {
    ITokenRepository tokenRepository;
    EventSender sender;
    CompletableFuture<String> result;
    public TokenManagement(ITokenRepository tokenRepository, EventSender sender) {
        this.tokenRepository = tokenRepository;
        this.sender = sender;
    }

    @Override
    public boolean validateToken(String tokenId)
    {
        return !tokenRepository.getUserIdByActiveToken(tokenId).isEmpty();
    }
    @Override
    public boolean generateTokensForUser(String userId, int n)
    {
        if(canGenerateTokensForUser(userId)) {
            generateTokens(userId,5);
            return true;
        }
        return false;
    }
    private void generateTokens(String userId,int n) {
        List<Token> tokens = new ArrayList<Token>();
        for (int i = 0; i < n; i++) {
            String uuid = UUID.randomUUID().toString();
            tokens.add(new Token(uuid, false));
        }
        tokenRepository.addActiveTokens(userId,tokens);
    }
    @Override
    public String findUserByUsedToken(String tokenId)
    {
        return tokenRepository.getUserIdByUsedToken(tokenId);
    }
    @Override
    public String findUserByActiveToken(String tokenId) {
        return tokenRepository.getUserIdByActiveToken(tokenId);
    }

    @Override
    public boolean canGenerateTokensForUser(String userId) {

        if(tokenRepository.userExistsInActive(userId))
            return tokenRepository.getActiveTokens(userId).size()<=1;
        return true;
    }

    @Override
    public  boolean consumeToken(String tokenId)
    {
        String user = tokenRepository.getUserIdByActiveToken(tokenId);
        //System.out.println("Consumed user" + user);
        tokenRepository.removeActiveToken(user, tokenId);
        tokenRepository.addUsedToken(user,tokenId);
        return true;
    }
    @Override
    public boolean deleteTokens(String userId) {
        return tokenRepository.deleteActiveTokens(userId) || tokenRepository.deleteUsedTokens(userId);
    }

    @Override
    public String getActiveToken(String userId)
    {
        generateTokensForUser(userId,5);
        return tokenRepository.getActiveTokens(userId).stream().findFirst().get().getTokenId();
    }
    public String sendRequest(String eventType,String customer) throws Exception {
        Event event = new Event(eventType,new Object[] { customer });
        result = new CompletableFuture<>();
        sender.sendEvent(event);
        return result.join();
    }
    public String answerRequest_validateToken(String eventType,String tokenId) throws Exception {
        boolean valid = validateToken(tokenId);
        Event event = new Event(eventType,new Object[] { Boolean.toString(valid) });
        sender.sendEvent(event);
        return tokenId;
    }
    @Override
    public void receiveEvent(Event event) throws Exception {

        System.out.println("Received event "+event);
        if (event.getEventType().equals("validateToken")) {
            System.out.println("TS event handled: "+event);
            String tokenId = event.getArgument(0, String.class);
            answerRequest_validateToken("validateToken_done",tokenId);
        } else {
            System.out.println("TS event ignored: "+event);
        }
    }
}
