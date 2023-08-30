package HugoVanDerWel.services;

import HugoVanDerWel.Models.UserModel;

public interface IAuthenticationService {
    boolean verifyPassword(UserModel userModel);
    boolean verifyToken(UserModel userModel);
    UserModel generateNewTokenForUser(UserModel userModel);
}
