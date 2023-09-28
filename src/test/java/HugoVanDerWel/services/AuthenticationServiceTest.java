package HugoVanDerWel.services;

import HugoVanDerWel.service.exceptions.UnauthorizedException;
import HugoVanDerWel.service.exceptions.UserNotFoundException;
import HugoVanDerWel.service.models.UserModel;
import HugoVanDerWel.data.persistence.UserPersistence;
import HugoVanDerWel.service.services.AuthenticationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;


public class AuthenticationServiceTest {
    private AuthenticationService sut;
    private UserPersistence userPersistenceMock;
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
        userPersistenceMock = Mockito.mock(UserPersistence.class);
        sut = new AuthenticationService(userPersistenceMock);
    }

    @Test
    public void verifyPasswordVerifiesCorrectPassword() {
        //Arrange
        when(userPersistenceMock.getPasswordForUser(Mockito.any(UserModel.class))).thenReturn(userModelUsernamePassword);

        //Act
        boolean response = sut.verifyPassword(userModelUsernamePassword);

        //Assert
        Assert.assertTrue(response);
    }

    @Test
    public void verifyPasswordRefusesIncorrectPassword() {
        //Arrange
        when(userPersistenceMock.getPasswordForUser(Mockito.any(UserModel.class))).thenReturn(new UserModel() {{
            username = "Giel";
            password = "Bankjespuree222FOUT";
        }});

        //Act
        boolean response = sut.verifyPassword(userModelUsernamePassword);

        //Assert
        Assert.assertFalse(response);
    }

    @Test
    public void verifyPasswordRefusesWhenUserCannotBeFound() {
        //Arrange
        when(userPersistenceMock.getPasswordForUser(Mockito.any(UserModel.class))).thenThrow(UserNotFoundException.class);
        //Act
        boolean response = sut.verifyPassword(userModelUsernamePassword);

        //Assert
        Assert.assertFalse(response);
    }

    @Test
    public void verifyTokenVerifiesCorrectToken() {
        //Arrange
        when(userPersistenceMock.getTokenForUser(Mockito.any(UserModel.class))).thenReturn(userModelUsernameToken);

        //Act
        boolean response = sut.verifyToken(userModelUsernameToken);

        //Assert
        Assert.assertTrue(response);
    }

    @Test
    public void verifyTokenRefusesIncorrectToken() {
        //Arrange
        when(userPersistenceMock.getTokenForUser(Mockito.any(UserModel.class))).thenReturn(new UserModel() {{
            username = "Giel";
            token = "token5";
        }});

        //Act
        boolean result = sut.verifyToken(userModelUsernameToken);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void verifyTokenRefusesWhenUserCannotBeFound() {
        //Arrange
        when(userPersistenceMock.getTokenForUser(Mockito.any(UserModel.class))).thenThrow(UserNotFoundException.class);

        //Act
        boolean result = sut.verifyToken(userModelUsernameToken);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void generateNewTokenForUserGeneratesUniqueToken() {
        //Arrange
        when(userPersistenceMock.setTokenForUser(Mockito.any(UserModel.class), Mockito.anyString())).thenAnswer(i -> new UserModel() {{
            username = "Doesn't matter";
            token = i.getArguments()[1].toString();
        }});

        //Act
        UserModel userModelWithToken1 = sut.generateNewTokenForUser(userModelUsernamePassword);
        UserModel userModelWithToken2 = sut.generateNewTokenForUser(userModelUsernamePassword);

        //Assert
        Assert.assertNotEquals(userModelWithToken1.token, userModelWithToken2.token);
    }

    @Test
    public void getUsernameForTokenReturnsGoodResponse() {
        //Arrange
        when(userPersistenceMock.getUsernameForToken(Mockito.any(UserModel.class))).thenAnswer(i -> new UserModel() {{
            username = "test123";
            token = i.getArguments()[0].toString();
        }});

        //Act
        UserModel response = sut.getUsernameForToken("token");

        //Assert
        Assert.assertEquals("test123", response.username);
    }

    @Test
    public void getUsernameForTokenRefusesWhenTokenIsNotFound() {
        //Arrange
        when(userPersistenceMock.getUsernameForToken(Mockito.any(UserModel.class))).thenThrow(RuntimeException.class);
        boolean threwException = false;

        //Act
        try {
            sut.getUsernameForToken("token");
        } catch (UnauthorizedException ignored) {
            threwException = true;
        }

        //Assert
        Assert.assertTrue(threwException);
    }

    @Test
    public void isTokenActiveApprovesGoodToken() {
        //Arrange
        when(userPersistenceMock.getUsernameForToken(Mockito.any(UserModel.class))).thenAnswer(i -> new UserModel() {{
            username = "test123";
            token = i.getArguments()[0].toString();
        }});

        //Act
        boolean response = sut.isTokenActive("token");

        //Assert
        Assert.assertTrue(response);
    }

    @Test
    public void isTokenActiveDeniesBadToken() {
        //Arrange
        when(userPersistenceMock.getUsernameForToken(Mockito.any(UserModel.class))).thenThrow(RuntimeException.class);

        //Act
        boolean response = sut.isTokenActive("token");

        //Assert
        Assert.assertFalse(response);
    }
}
