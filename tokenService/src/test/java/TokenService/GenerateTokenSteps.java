package TokenService;


import com.gr_23.business_logic.TokenManagement;
import com.gr_23.data_access.ITokenRepository;
import com.gr_23.data_access.TokenRepository;
import com.gr_23.models.Token;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateTokenSteps {
    String userId;
    String tokenId;
    ITokenRepository tokenRepository = new TokenRepository();
    TokenManagement tokenManagement = new TokenManagement(tokenRepository,null);
    public GenerateTokenSteps() {
    }

    public void generateTestTokens(String userId, int n){
        List<String> testTokens = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            testTokens.add("token"+n);
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


    @Given("The token with id {string} does not exist")
    public void theTokenWithIdDoesNotExist(String tokenId) {
        this.tokenId = tokenId;
        assertFalse(tokenRepository.checkToken(tokenId));
    }

    @Then("When we lookup that token we do not find anything")
    public void weDoNotFindAnything() {
        assertFalse(tokenManagement.validateToken(tokenId));
    }

    @Given("that user with userid {string}")
    public void thatUserWithUseridWithId(String userId) {
        this.userId = userId;
        generateTestTokens(userId,1);
    }

    @Then("we lookup the token {string} and we find the userid")
    public void weLookupTheToken(String tokenId) {
        assertEquals(userId,tokenManagement.findUserByActiveToken(tokenId));
    }

    @Given("that the token with id {string} is active")
    public void thatTheTokenWithIdIsActive(String tokenId) {
        this.tokenId = tokenId;
        generateTestTokens(tokenId,1);
        assertTrue(tokenManagement.validateToken(tokenId));
    }

    @Then("we consume it and the token is used")
    public void weConsumeItAndTheTokenIsUsed(){
        assertTrue(tokenManagement.consumeToken(tokenId));
        assertFalse(tokenManagement.validateToken(tokenId));
    }

    @Given("the used token with id {string}")
    public void thatTheTokenWithIdIsInactive(String tokenId) {
        this.tokenId = tokenId;
        generateTestTokens(tokenId,1);
        tokenManagement.consumeToken(tokenId);
        assertFalse(tokenManagement.validateToken(tokenId));
    }

    @Then("we consume it and the token is not consumed")
    public void weConsumeItandTheTokenIsNotUsed(){
        assertFalse(tokenManagement.consumeToken(tokenId));
    }
}
