package HugoVanDerWel.resources;

import HugoVanDerWel.presentation.dataTransferObjects.LoginRequestDTO;
import HugoVanDerWel.presentation.resources.LoginResource;
import HugoVanDerWel.service.models.UserModel;
import HugoVanDerWel.service.services.AuthenticationService;
import HugoVanDerWel.support.TestAuthenticationSupport;
import jakarta.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LoginResourceTest {
    private LoginResource sut;
    private AuthenticationService successFullAuthenticationServiceMock;
    private AuthenticationService incorrectAuthenticationServiceMock;
    private final UserModel userModelUsernamePassword = new UserModel() {{
        username = "Giel";
        password = "Bankjespuree111";
    }};
    private final UserModel userModelUsernameToken = new UserModel() {{
        username = "Giel";
        password = "Bankjespuree111";
        token = "123123123";
    }};

    @Before
    public void setUp() {
        successFullAuthenticationServiceMock = TestAuthenticationSupport.getSuccessfullLoginAuthenticationService();
        incorrectAuthenticationServiceMock = TestAuthenticationSupport.getIncorrectLoginAuthenticationService();
    }

    @Test
    public void goodLoginTest() {
        //setup
        sut = new LoginResource(successFullAuthenticationServiceMock);
        Mockito.when(successFullAuthenticationServiceMock.generateNewTokenForUser(Mockito.any())).thenReturn(userModelUsernameToken);

        //act
        Response responseEntity = sut.login(new LoginRequestDTO(){{
            user = userModelUsernamePassword.username;
            password = userModelUsernamePassword.password;
        }});
        LoginRequestDTO responseBody = (LoginRequestDTO) responseEntity.getEntity();

        //assert
        Assert.assertEquals(responseBody.user, "Giel");
        Assert.assertEquals(responseBody.token, "123123123");
    }

    @Test
    public void badLoginTest() {
        //setup
        sut = new LoginResource(incorrectAuthenticationServiceMock);

        //act
        Response responseEntity = sut.login(new LoginRequestDTO(){{
            user = userModelUsernamePassword.username;
            password = userModelUsernamePassword.password;
        }});
        int responseStatus = responseEntity.getStatus();

        //assert
        Assert.assertEquals(responseStatus, 304);
    }

    @Test
    public void emptyLoginTest() {
        //setup
        sut = new LoginResource(incorrectAuthenticationServiceMock);

        //act
        Response responseEntity = sut.login(new LoginRequestDTO());
        int responseStatus = responseEntity.getStatus();

        //assert
        Assert.assertEquals(responseStatus, 304);
    }
}
