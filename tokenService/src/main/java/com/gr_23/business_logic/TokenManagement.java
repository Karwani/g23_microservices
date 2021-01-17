package com.gr_23.business_logic;

import com.gr_23.data_access.ITokenRepository;
import com.gr_23.models.Token;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;

@ApplicationScoped
public class TokenManagement implements ITokenManagement {
    ITokenRepository tokenRepository;

    public TokenManagement(ITokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
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

}
