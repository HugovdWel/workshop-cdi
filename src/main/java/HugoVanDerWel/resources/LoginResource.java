package HugoVanDerWel.resources;

import HugoVanDerWel.dataTransferObjects.LoginRequestDTO;
import HugoVanDerWel.models.UserModel;
import HugoVanDerWel.services.AuthenticationService;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/login")
public class LoginResource {

    private AuthenticationService authenticationService;

    public LoginResource() {
    }

    @Inject
    public LoginResource(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequestDTO) {
        UserModel userModel = new UserModel() {{
            username = loginRequestDTO.user;
            password = loginRequestDTO.password;
        }};
        if (!authenticationService.verifyPassword(userModel)) {
            return Response.status(304).build();
        }
        return Response.status(200).entity(new LoginRequestDTO() {{
            user = userModel.username;
            token = authenticationService.generateNewTokenForUser(userModel).token;
        }}).build();
    }
}
