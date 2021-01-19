package clientside;

import java.util.Random;

public  class TokenInfo {

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String tokenId;
    public String userId;

    public String generateRandomCPR()
    {
        int p1 = 100000 + new Random().nextInt(900000);

        int p2 = 1000 + new Random().nextInt(9000);
        return p1 + "-" + p2;
    }

}
