package com.gr_23;

public class Token {
    private String tokenId;
    private boolean used;

    public Token() {

    }

    public Token(String tokenId, boolean used) {
        this.tokenId = tokenId;
        this.used = used;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
