package edu.ifrs;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import edu.ifrs.client.PersonagemService;
import edu.ifrs.dto.PersonagemResponse;
import edu.ifrs.model.Personagem;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/status")
public class StatusPersonagemResource {

    @Inject 
    @RestClient 
    PersonagemService personagemService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User","Admin"}) //requer que qualquer requisição aqui TENHA permissão de User ou Admin
    public Response isAlive(Personagem personagemInput) {
        PersonagemResponse personagemResponse = new PersonagemResponse();
        personagemResponse.setIsAlive(false);

        try {
            Personagem personagem = personagemService.getPersonagemById(personagemInput.getId());

            if (personagem.getStatus().equalsIgnoreCase("Alive")) {
                personagemResponse.setIsAlive(true);
                personagemResponse.setPersonagem(personagem);
            }
        } catch (WebApplicationException e) {
            Response originalResponse = e.getResponse();
            //lê o corpo retornado pela API externa
            String erroJson = originalResponse.readEntity(String.class);

            //retorna o código HTTP original da resposta e tb o conteúdo original da msg
            return Response.status(originalResponse.getStatus()).entity(erroJson).type(MediaType.APPLICATION_JSON).build();
            //return Response.status(e.getResponse().getStatus()).build(); //alternativa sem retornar o conteúdo original
        }

            return Response.ok(personagemResponse).build();
    }
}
