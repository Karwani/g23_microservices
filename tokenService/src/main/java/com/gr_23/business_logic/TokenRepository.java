package com.gr_23.business_logic;

import com.gr_23.models.Token;

import java.util.*;
import java.util.stream.Collectors;

public class TokenRepository {

    private static Map<String, List<Token>> activeTokens = new HashMap<String, List<Token>>();
    private static Map<String, List<Token>> usedTokens = new HashMap<String, List<Token>>();

    public TokenRepository() {
    }

    public void generateTokens(String userId,int n) {
        List<Token> tokens = new ArrayList<Token>();
        for (int i = 0; i < n; i++) {
            String uuid = UUID.randomUUID().toString();
            tokens.add(new Token(uuid, false));
        }
        if (activeTokens.get(userId) == null) {
            activeTokens.put(userId, tokens);
        } else {
            activeTokens.get(userId).addAll(tokens);
        }
    }
    public String findUserByUsedToken(String tokenId)
    {
        for( Map.Entry<String, List<Token>> pair : usedTokens.entrySet()) {
            Boolean holder = pair.getValue().stream().map(token -> token.getTokenId()).filter(t -> t.equals(tokenId)).findFirst().isPresent();
            if (holder) {
                return pair.getKey();
            }
        }
        return "";
    }
    public String findUserByActiveToken(String tokenId) {
        for( Map.Entry<String, List<Token>> pair : activeTokens.entrySet()) {
            Boolean holder = pair.getValue().stream().map(token -> token.getTokenId()).filter(t -> t.equals(tokenId)).findFirst().isPresent();
            if (holder) {
                return pair.getKey();
            }
        }
        return "";
    }
    public String findUserByToken(String tokenId)
    {
        String active = findUserByActiveToken(tokenId);
        if(!active.isEmpty())
            return active;
        String used = findUserByUsedToken(tokenId);
        if(!used.isEmpty())
            return "INVALID";
        return "";
    }
    public boolean canGenerateTokensForUser(String userId) {
        if(activeTokens.get(userId)!=null)
            return activeTokens.get(userId).size()<=1;
        return true;
    }

    public boolean deleteTokens(String userId) {
        boolean deleted = false;
        if(activeTokens.get(userId)!=null) {
            activeTokens.remove(userId);
            deleted = true;
        }
        if (usedTokens.get(userId) != null) {
            usedTokens.remove(userId);
            deleted = true;
        }
        return deleted;
    }

    public String getActiveToken(String userId)
    {
        return activeTokens.get(userId).stream().findFirst().get().getTokenId();
    }

    public void removeTokenFromActive(String tokenId, String user) {
        List<Token> temp = activeTokens.get(user).stream().filter(token -> !token.getTokenId().equals(tokenId)).collect(Collectors.toList());
        //System.out.println("temporary deleted" + temp.toString());
        activeTokens.replace(user,temp);
    }

    public void addTokenToUsed(String tokenId, String user) {
        if(!usedTokens.containsKey(user)) {
            usedTokens.put(user,new ArrayList<Token>());
        }
        usedTokens.get(user).add(new Token(tokenId, true));
    }
}
