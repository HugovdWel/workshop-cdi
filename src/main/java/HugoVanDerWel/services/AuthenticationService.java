package HugoVanDerWel.services;

import HugoVanDerWel.Models.UserModel;
import HugoVanDerWel.persistence.UserPersistence;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.Random;

public class AuthenticationService implements IAuthenticationService {
    private UserPersistence userPersistence;

    @Inject
    public AuthenticationService(UserPersistence userPersistence){
        this.userPersistence = userPersistence;
    }

    public boolean verifyPassword(UserModel userModel){
        return Objects.equals(userPersistence.getPasswordForUser(userModel).password, userModel.password);
    }

    public boolean verifyToken(UserModel userModel){
        return Objects.equals(userPersistence.getTokenForUser(userModel).token, userModel.token);
    }

    public UserModel generateNewTokenForUser(UserModel userModel){
        return userPersistence.setTokenForUser(userModel, this.generateToken());
    }

    public UserModel getUsernameForToken(String inputToken){
        return this.userPersistence.getUsernameForToken(new UserModel(){{token = inputToken;}});
    }

    private String generateToken(){
        Random random = new Random();
        int token = 0;
        for (int i = 0; i < 20; i++) {
            token += random.nextInt();
        }
        return String.valueOf(token);
    }
}
