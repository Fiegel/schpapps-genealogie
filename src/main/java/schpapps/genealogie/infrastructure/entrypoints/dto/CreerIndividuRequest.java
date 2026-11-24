package schpapps.genealogie.infrastructure.entrypoints.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

/**
 * La requête DTO de la création d'un individu.
 *
 * @param nom Le nom à donner.
 * @param prenom Le prénom.
 * @param sexe Le sexe.
 * @param dateNaissance La date de naissance.
 */
@Schema(description = "Modèle de données pour créer d'un individu")
public record CreerIndividuRequest(
        @Schema(description = "Nom de famille", examples = "Fiegel", required = true)
        String nom,

        @Schema(description = "Prénom", examples = "Jean", required = true)
        String prenom,

        @Schema(description = "Sexe", examples = "HOMME")
        String sexe,

        @Schema(description = "Date de naissance", examples = "1990-01-01")
        LocalDate dateNaissance) {
}
