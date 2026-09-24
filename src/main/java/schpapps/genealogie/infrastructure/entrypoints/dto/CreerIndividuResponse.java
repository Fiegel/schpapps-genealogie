package schpapps.genealogie.infrastructure.entrypoints.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import schpapps.genealogie.domain.entite.Individu;

import java.time.LocalDate;

/**
 * Response DTO de la création d'un individu.
 *
 * @param id L'identifiant technique de l'individu.
 * @param nom Le nom.
 * @param prenom Le prénom.
 * @param sexe Le sexe
 * @param dateNaissance La date de naissance.
 */
@Schema(description = "Modèle de données pour la réponse de création d'un individu")
public record CreerIndividuResponse(
        @Schema(description = "Identifiant technique", examples = "indi-123")
        String id,

        @Schema(description = "Nom de famille", examples = "Fiegel")
        String nom,

        @Schema(description = "Prénom", examples = "Jean")
        String prenom,

        @Schema(description = "Sexe", examples = "HOMME")
        String sexe,

        @Schema(description = "Date de naissance", examples = "1990-01-01")
        LocalDate dateNaissance) {

    /**
     * Convertit un individu en la réponse de sa demande de création.
     *
     * @param individu L'individu créé.
     * @return Les données de la réponse.
     */
    public static CreerIndividuResponse from(final Individu individu) {
        return new CreerIndividuResponse(individu.id,
                individu.nom,
                individu.prenom,
                individu.sexe.name(),
                individu.dateNaissance);
    }
}
