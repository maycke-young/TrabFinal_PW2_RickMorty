package edu.ifrs.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import edu.ifrs.model.Personagem;

//anotação q faz a interface funcionar com o REST Client
@RegisterRestClient (baseUri = "https://rickandmortyapi.com/api/character/")
public interface PersonagemService {
    
    @GET 
    @Path("/{id}")
    public Personagem getPersonagemById(@PathParam("id") int id);

}
