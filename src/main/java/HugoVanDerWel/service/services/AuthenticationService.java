package HugoVanDerWel.service.services;

import HugoVanDerWel.service.exceptions.UnauthorizedException;
import HugoVanDerWel.service.exceptions.UserNotFoundException;
import HugoVanDerWel.service.models.UserModel;
import HugoVanDerWel.data.persistence.UserPersistence;
import jakarta.inject.Inject;
import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

public class AuthenticationService {
    private UserPersistence userPersistence;

    @Inject
    public AuthenticationService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public boolean verifyPassword(UserModel userModel) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(userModel.password.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();

            return userPersistence.getPasswordForUser(userModel).password.equals(hash);
        } catch (UserNotFoundException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyToken(UserModel userModel) {
        try {
            return Objects.equals(userPersistence.getTokenForUser(userModel).token, userModel.token);
        } catch (UnauthorizedException | UserNotFoundException e) {
            return false;
        }
    }

    public UserModel generateNewTokenForUser(UserModel userModel) {
        return userPersistence.setTokenForUser(userModel, this.generateToken());
    }

    public UserModel getUsernameForToken(String inputToken) {
        try {
            return this.userPersistence.getUsernameForToken(new UserModel() {{
                token = inputToken;
            }});
        } catch (RuntimeException e) {
            throw new UnauthorizedException();
        }
    }

    public boolean isTokenActive(String inputToken) {
        try {
            this.userPersistence.getUsernameForToken(new UserModel() {{
                token = inputToken;
            }});
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private String generateToken() {
        Random random = new Random();
        int token = random.nextInt(100000, 999999);
        return String.valueOf(token);
    }
}
