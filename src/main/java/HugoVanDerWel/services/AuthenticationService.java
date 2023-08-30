package HugoVanDerWel.services;

import HugoVanDerWel.exceptions.UserNotFoundException;
import HugoVanDerWel.models.UserModel;
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
        try{
            String inputPassword = userModel.password;
            return userPersistence.getPasswordForUser(userModel).password.equals(inputPassword);
        } catch (UserNotFoundException e){
            return false;
        }
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
        int token = random.nextInt(100000, 999999);
        return String.valueOf(token);
    }
}
