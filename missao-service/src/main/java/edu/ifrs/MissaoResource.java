package edu.ifrs;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import edu.ifrs.client.StatusPersonagemService;
import edu.ifrs.data.Missoes;
import edu.ifrs.dto.PersonagemResponse;
import edu.ifrs.model.Missao;
import edu.ifrs.model.Personagem;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/missoes")
public class MissaoResource {

    @Inject
    Missoes missoes;

    @Inject
    @RestClient
    StatusPersonagemService statusPersonagemService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON) //entra JSON
    @Produces(MediaType.APPLICATION_JSON) //sai JSON
    @RolesAllowed({"User","Admin"}) //requer que qualquer requisição aqui TENHA permissão de User ou Admin
    public Response adicionarMissao(Missao missao) {
        missoes.cadastrarMissao(missao);
        // retorna HTTP 201, recurso criado com sucesso
        return Response.status(Response.Status.CREATED).entity(missao).build();
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON) //sai JSON
    @PermitAll //permite acesso p/ todos
    public Response listarMissoesCadastradas(@QueryParam("id") Long id) {
        if (id==null) {
            List<Missao> lista = missoes.listarTodas();
            return Response.ok(lista).build(); // retorna HTTP 200 com lista
        } else {
            Missao missao = missoes.consultarMissao(id);
            if (missao==null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(missao).build(); // retorna HTTP 200 com missão específica
            }
        }

    }


    @PUT  
    @Path("/{id}/tripulantes") 
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) //entra dados de formulário
    @Produces(MediaType.APPLICATION_JSON) //sai JSON
    @RolesAllowed({"User","Admin"}) //requer que qualquer requisição aqui TENHA permissão de User ou Admin
    public Response atualizarTripulantesMissao(@PathParam("id") Long idMissao, @FormParam("idPersonagem") List<Integer> idsPersonagens) {
        Set<Personagem> tripulacao = new HashSet<>();
        List<Missao> listaMissoes = missoes.listarTodas();
        for (Missao missao : listaMissoes) {
            if (missao.getId().equals(idMissao)) { //encontrou missão
                //consultar status de cada tripulante <chama o outro serviço>
                for (int id : idsPersonagens) {
                    Response responsePersonagemService = null;

                    try {
                        responsePersonagemService = statusPersonagemService.isAlive(Map.of("id",id));
                    } catch (WebApplicationException e) {
                        Response originalResponse = e.getResponse();

                        if (originalResponse != null && originalResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                            // retorna HTTP 404, não encontrado
                            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro","Personagem com id "+id+" não encontrado")).build();
                        } else {
                            //lê o corpo retornado pela API externa
                            String erroJson = originalResponse.readEntity(String.class);
                            //retorna o código HTTP original da resposta e tb o conteúdo original da msg
                            return Response.status(originalResponse.getStatus()).entity(erroJson).type(MediaType.APPLICATION_JSON).build();
                        }
                    }

                    PersonagemResponse personagemResponse = responsePersonagemService.readEntity(PersonagemResponse.class); //converte response em PersonagemResponse
                    if (!personagemResponse.getIsAlive()) { //responde com HTTP 409 (Conflict) e erro explicativo se algum não estiver vivo
                        return Response.status(Response.Status.CONFLICT).entity(Map.of("erro","Personagem com id "+id+" não está vivo")).build();
                    } else { //está vivo, gravar dados do personagem na lista provisória
                        tripulacao.add(personagemResponse.getPersonagem());
                    }
                }
                //passou por todos os personagens listados e todos estão vivos
                //adiciona tripulação à missão
                missao.setTripulacao(tripulacao);
                //responde com HTTP 201 + json-da-missão se todos vivos e adicionados À missão [recurso (missão) atualizado com sucesso]
                return Response.status(Response.Status.CREATED).entity(missao).build();
            }
        } 

        String missaoNaoEncontrada = "Missão com id "+idMissao+" não encontrada";
        return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro",missaoNaoEncontrada)).build(); //não encontrou missão
    }


    @DELETE 
    @Path("/{id}")
    @RolesAllowed({"User","Admin"}) //requer que qualquer requisição aqui TENHA permissão de User ou Admin
    public Response removerMissao(@PathParam("id") Long id) {
        Boolean removida = missoes.removerMissao(id);
        if (!removida) { //não encontrada
            return Response.status(Response.Status.NOT_FOUND).build();
        } else { // retorna HTTP 204, recurso removido com sucesso
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
