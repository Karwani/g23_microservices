package com.gr_23.business_logic;

public interface ITokenManagement {
    boolean validateToken(String tokenId);

    boolean generateTokensForUser(String userId, int n);

    String findUserByUsedToken(String tokenId);

    String findUserByActiveToken(String tokenId);

    boolean canGenerateTokensForUser(String userId);

    boolean consumeToken(String tokenId);

    void deleteTokens(String userId);

    String getActiveToken(String userId);
}
