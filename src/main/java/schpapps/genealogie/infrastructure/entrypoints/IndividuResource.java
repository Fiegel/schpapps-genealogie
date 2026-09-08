package schpapps.genealogie.infrastructure.entrypoints;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.inbound.CreerIndividuUseCase;
import schpapps.genealogie.domain.ports.inbound.commande.CreerIndividuCommande;
import schpapps.genealogie.infrastructure.entrypoints.dto.CreerIndividuRequest;
import schpapps.genealogie.infrastructure.entrypoints.dto.CreerIndividuResponse;
import schpapps.genealogie.infrastructure.entrypoints.dto.ErrorResponse;

/**
 * Le controller des individus.
 */
@Path("/api/individus")
@Tag(name = "Individus", description = "Gestion des individus des arbres généalogiques")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IndividuResource {

    private final CreerIndividuUseCase creerIndividuUseCase;

    /**
     * Constructeur valué.
     *
     * @param creerIndividuUseCase Le scénario de création d'un individu.
     */
    public IndividuResource(final CreerIndividuUseCase creerIndividuUseCase) {
        this.creerIndividuUseCase = creerIndividuUseCase;
    }

    /**
     * Crée un nouvel individu.
     *
     * @param request La requête contenant les données pour la création.
     * @return La response contenant les données créées.
     */
    @POST
    @Operation(summary = "Créer un nouvel individu", description = "Enregistre un individu dans le système.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201",
                    description = "L'individu a été créé avec succès",
                    content = @Content(schema = @Schema(implementation = CreerIndividuResponse.class))),
            @APIResponse(responseCode = "400",
                    description = "Données d'entrée invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500",
                    description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response creerIndividu(final CreerIndividuRequest request) {
        final CreerIndividuCommande commande = new CreerIndividuCommande(request.nom(),
                request.prenom(),
                request.sexe(),
                request.dateNaissance());

        final Individu individu = creerIndividuUseCase.executer(commande);

        final CreerIndividuResponse response = CreerIndividuResponse.from(individu);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
