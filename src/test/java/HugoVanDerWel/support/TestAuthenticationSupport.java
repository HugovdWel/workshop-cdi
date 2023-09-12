package HugoVanDerWel.support;

import HugoVanDerWel.exceptions.UnauthorizedException;
import HugoVanDerWel.models.UserModel;
import HugoVanDerWel.services.AuthenticationService;
import org.mockito.Mockito;

public class TestAuthenticationSupport {
    public static AuthenticationService getSuccessfullLoginAuthenticationService() {
        AuthenticationService mock = Mockito.mock(AuthenticationService.class);
        Mockito.when(mock.verifyPassword(Mockito.any())).thenReturn(true);
        Mockito.when(mock.getUsernameForToken(Mockito.any())).thenReturn(new UserModel(){{username = "kevin";}});
        Mockito.when(mock.isTokenActive(Mockito.any())).thenReturn(true);
        return mock;
    }

    public static AuthenticationService getIncorrectLoginAuthenticationService() {
        AuthenticationService mock = Mockito.mock(AuthenticationService.class);
        Mockito.when(mock.verifyPassword(Mockito.any())).thenReturn(false);
        Mockito.when(mock.getUsernameForToken(Mockito.any())).thenThrow(UnauthorizedException.class);
        Mockito.when(mock.isTokenActive(Mockito.any())).thenReturn(false);
        return mock;
    }
}
