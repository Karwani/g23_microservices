package com.gr_23.business_logic;

import com.gr_23.data_access.ITokenRepository;
import com.gr_23.models.Token;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenManagement implements ITokenManagement, EventReceiver {
    ITokenRepository tokenRepository;
    EventSender sender;

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
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String uuid = UUID.randomUUID().toString();
            tokens.add(uuid);
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
        if(tokenRepository.userExistsInActive(userId)) {
            System.out.println("user exists" + userId );
            int t = tokenRepository.getActiveTokens(userId).size() ;
            return t<=1;
        }
        return true;
    }

    @Override
    public  boolean consumeToken(String tokenId)
    {
        String user = tokenRepository.getUserIdByActiveToken(tokenId);

        if(user.isEmpty()) {
            return false;
        }
        else {
            tokenRepository.removeActiveToken(user, tokenId);
            tokenRepository.addUsedToken(user, tokenId);
            return true;
        }
    }
    @Override
    public void deleteTokens(String userId) {
        tokenRepository.deleteUsedTokens(userId);
        tokenRepository.deleteActiveTokens(userId);
    }

    @Override
    public String getActiveToken(String userId)
    {
        generateTokensForUser(userId,5);
        return tokenRepository.getActiveTokens(userId).stream().findFirst().get();
    }

    public String answerRequest_validateToken(String eventType,String tokenId) throws Exception {
        boolean valid = validateToken(tokenId);
        Event event = new Event(eventType,new Object[] { Boolean.toString(valid), tokenId });
        sender.sendEvent(event);
        return tokenId;
    }

    // TODO: answer other messages

    public void answerRequest_findUserByToken(String eventType, String tokenId) throws Exception {
        String userId = findUserByActiveToken(tokenId);
        Event event = new Event(eventType, new Object[] { userId, tokenId });
        sender.sendEvent(event);
    }

    public void answerRequest_consumeToken(String eventType, String tokenId) throws Exception {
        Boolean consumed = consumeToken(tokenId);
        Event event = new Event(eventType, new Object[] { Boolean.toString(consumed), tokenId });
        sender.sendEvent(event);
    }


    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("validateToken")) {
            String tokenId = event.getArgument(0, String.class);
            answerRequest_validateToken("validateToken_done",tokenId);

        }
        if(event.getEventType().equals("findUserByToken")) {
            String tokenId = event.getArgument(0, String.class);
            answerRequest_findUserByToken("findUserByToken_done", tokenId);
        }

        if(event.getEventType().equals("consumeToken")) {
            String tokenId = event.getArgument(0,String.class);
            answerRequest_consumeToken("consumeToken_done", tokenId);
        }


    }
}
