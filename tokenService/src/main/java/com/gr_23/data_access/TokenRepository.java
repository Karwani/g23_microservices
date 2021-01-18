package com.gr_23.data_access;

import com.gr_23.models.Token;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class TokenRepository implements  ITokenRepository {
//    private static  TokenRepository instance = null;
//
//    private TokenRepository(){
//    }
//
//    public static TokenRepository getInstance(){
//        if(instance==null)
//            instance = new TokenRepository();
//        return instance;
//    }

    private Map<String, List<Token>> activeTokens = new HashMap<String, List<Token>>();
    private Map<String, List<Token>> usedTokens = new HashMap<String, List<Token>>();

    @Override
    public boolean checkToken(String tokenId) {
        boolean found = false;

        for(Map.Entry<String, List<Token>> pair : activeTokens.entrySet()) {
            found = pair.getValue().stream().map(token -> token.getTokenId())
                    .filter(t -> t.equals(tokenId))
                    .findFirst()
                    .isPresent();
        }

        for(Map.Entry<String, List<Token>> pair : activeTokens.entrySet())
        {
            found = pair.getValue().stream().map(token -> token.getTokenId())
                    .filter(t -> t.equals(tokenId))
                    .findFirst()
                    .isPresent();
        }

        return found;
    }

    @Override
    public void addActiveTokens(String userId, List<Token> tokens)
    {
        if (activeTokens.get(userId) == null) {
            activeTokens.put(userId, tokens);
        } else {
            activeTokens.get(userId).addAll(tokens);
        }
    }

    @Override
    public void removeActiveToken(String userId, String tokenId) {
        List<Token> temp = activeTokens.get(userId).
                stream()
                .filter(token -> !token.getTokenId().equals(tokenId)).collect(Collectors.toList());
        //System.out.println("temporary deleted" + temp.toString());
        activeTokens.replace(userId,temp);
    }

    @Override
    public void addUsedToken(String userId, String tokenId) {
        if(!usedTokens.containsKey(userId)) {
            usedTokens.put(userId,new ArrayList<Token>());
        }
        usedTokens.get(userId).add(new Token(tokenId, true));
    }

    @Override
    public Boolean deleteActiveTokens(String userId) {
        if(activeTokens.get(userId)!=null) {
            activeTokens.remove(userId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteUsedTokens(String userId) {
        if(usedTokens.get(userId)!=null) {
            usedTokens.remove(userId);
            return true;
        }
        return false;
    }

    @Override
    public String getUserIdByUsedToken(String tokenId) {
        for( Map.Entry<String, List<Token>> pair : usedTokens.entrySet()) {
            Boolean holder = pair.getValue().stream().map(token -> token.getTokenId()).filter(t -> t.equals(tokenId)).findFirst().isPresent();
            if (holder) {
                return pair.getKey();
            }
        }
        return "";
    }

    @Override
    public String getUserIdByActiveToken(String tokenId) {
        for( Map.Entry<String, List<Token>> pair : activeTokens.entrySet()) {
            Boolean holder = pair.getValue().stream().map(token -> token.getTokenId()).filter(t -> t.equals(tokenId)).findFirst().isPresent();
            if (holder) {
                return pair.getKey();
            }
        }
        return "";
    }

    @Override
    public List<Token> getActiveTokens(String userId) {
        return activeTokens.get(userId);
    }

    @Override
    public Boolean userExistsInActive(String userId) {
        return activeTokens.get(userId)!=null;
    }
    public String findUserByToken(String tokenId)
    {
        String active = getUserIdByActiveToken(tokenId);
        if(!active.isEmpty())
            return active;
        String used = getUserIdByUsedToken(tokenId);
        return "";
    }
}
