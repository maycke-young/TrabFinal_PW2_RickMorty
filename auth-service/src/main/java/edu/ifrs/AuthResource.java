package edu.ifrs;

import java.util.Arrays;
import java.util.HashSet;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
public class AuthResource {

    private static final String ISSUER = "users";

    @POST
    @Path("/token")
    @PermitAll
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String generate(@FormParam("user") String user, @FormParam("password")  String password) {
        return Jwt.issuer(ISSUER) //ISSUER é uma string, pode usar string direto ou variável
                .upn(user)
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                //.claim(Claims.full_name, fullName)
                .sign();
    }
}
