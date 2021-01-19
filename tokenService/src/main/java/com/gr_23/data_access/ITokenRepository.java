package com.gr_23.data_access;

import com.gr_23.models.Token;

import java.util.List;

public interface ITokenRepository {
    void addActiveTokens(String userId, List<String> tokens);

    void removeActiveToken(String userId, String tokenId);

    void addUsedToken(String userId, String tokenId);

    Boolean deleteActiveTokens(String userId);

    Boolean deleteUsedTokens(String userId);

    String getUserIdByUsedToken(String tokenId);

    String getUserIdByActiveToken(String tokenId);

    List<String> getActiveTokens(String userId);

    Boolean userExistsInActive(String userId);

    boolean checkToken(String tokenId);
}
