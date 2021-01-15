package clientside;

import java.util.Random;

public  class TokenInfo {

    public static String tokenId;
    public static String userId;
    public static String generateRandomCPR()
    {
        int p1 = 100000 + new Random().nextInt(900000);

        int p2 = 1000 + new Random().nextInt(9000);
        return p1 + "-" + p2;
    }

}
