package edu.ifrs.client;

import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//anotação q faz a interface funcionar com o REST Client
@RegisterRestClient (baseUri = "https://localhost:8444/status")
@AccessToken
public interface StatusPersonagemService {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isAlive(Map<String, Integer> id);
}
