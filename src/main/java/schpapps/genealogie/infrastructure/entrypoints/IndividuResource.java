package schpapps.genealogie.infrastructure.entrypoints;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuScenario;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;
import schpapps.genealogie.infrastructure.entrypoints.dto.CreerIndividuRequest;
import schpapps.genealogie.infrastructure.entrypoints.dto.CreerIndividuResponse;

/**
 * Le controller des individus.
 */
@Path("/api/individus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IndividuResource {

    private final CreerIndividuScenario creerIndividuScenario;

    /**
     * Constructeur valué.
     *
     * @param creerIndividuScenario Le scénario de création d'un individu.
     */
    public IndividuResource(final CreerIndividuScenario creerIndividuScenario) {
        this.creerIndividuScenario = creerIndividuScenario;
    }

    /**
     * Crée un nouvel individu.
     *
     * @param request La requête contenant les données pour la création.
     * @return La response contenant les données créées.
     */
    @POST
    @APIResponse(responseCode = "201",
            description = "L'individu a été créé avec succès",
            content = @Content(schema = @Schema(implementation = CreerIndividuResponse.class)))
    public Response creerIndividu(final CreerIndividuRequest request) {
        final CreerIndividuCommande commande = new CreerIndividuCommande(request.nom(),
                request.prenom(),
                request.sexe(),
                request.dateNaissance());

        final Individu individu = creerIndividuScenario.executer(commande);

        final CreerIndividuResponse response = new CreerIndividuResponse(individu.id,
                individu.nom,
                individu.prenom,
                individu.sexe.name(),
                individu.dateNaissance);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
