package TokenService;


import com.gr_23.business_logic.ITokenManagement;
import com.gr_23.business_logic.TokenManagement;
import com.gr_23.business_logic.TokenManagementFactory;
import com.gr_23.data_access.ITokenRepository;
import com.gr_23.data_access.TokenRepository;
import com.gr_23.models.Token;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateTokenSteps {
    String userId;
    ITokenRepository tokenRepository = new TokenRepository();
    TokenManagement tokenManagement = new TokenManagement(tokenRepository,null);
    public GenerateTokenSteps() {
    }

    public void generateTestTokens(String userId, int n){
        List<Token> testTokens = new ArrayList<Token>();
        for (int i = 1; i <= n; i++) {
            testTokens.add(new Token("testToken"+n, false));
        }
        tokenRepository.addActiveTokens(userId, testTokens);
    }

    @Given("that the user with userid {string} has less than two tokens")
    public void thatTheUserWithUseridHasLessThanTwoTokens(String userId) {
        this.userId = userId;
        assertTrue(tokenManagement.canGenerateTokensForUser(userId));
        }

    @Given("that the user with id {string} has two tokens or more")
    public void thatTheUserWithIdHasTwoTokensOrMore(String userId) {
        this.userId = userId;
        generateTestTokens(userId, 2);
        assertEquals(tokenRepository.getActiveTokens(userId).size(), 2);
        }

    @When("tokens are generated for the user")
    public void tokensAreGeneratedForTheUser() {
        assertTrue(tokenManagement.generateTokensForUser(userId, 5));
        }

    @When("the user tries to generate tokens")
    public void theUserTriesToGenerateTokens() {
        assertFalse(tokenManagement.generateTokensForUser(userId, 5));
        }

    @Then("the user has {int} tokens")
    public void theUserHasTokens(Integer tokenAmount) {
        assertEquals(tokenRepository.getActiveTokens(userId).size(), tokenAmount);
        }

    @Then("the user will not receive more tokens")
    public void theUserWillNotReceiveMoreTokens() {
        assertEquals(tokenRepository.getActiveTokens(userId).size(), 2);
    }

    @After
    public void cleanTokens(){
        tokenManagement.deleteTokens(userId);
    }
}
