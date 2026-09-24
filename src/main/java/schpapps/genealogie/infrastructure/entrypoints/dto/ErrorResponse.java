package schpapps.genealogie.infrastructure.entrypoints.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

/**
 * DTO de réponse d'erreur standard.
 *
 * @param status Le code de statut HTTP.
 * @param error Le message d'erreur.
 * @param message Le message détaillé.
 * @param timestamp Le timestamp de l'erreur.
 */
@Schema(description = "Modèle de données pour la réponse d'erreur standard")
public record ErrorResponse(
        @Schema(description = "Code de statut HTTP")
        int status,

        @Schema(description = "Message d'erreur")
        String error,

        @Schema(description = "Message détaillé")
        String message,

        @Schema(description = "Timestamp de l'erreur")
        Instant timestamp) {

    /**
     * Crée une instance d'ErrorResponse avec le timestamp actuel.
     *
     * @param status Le code de statut HTTP.
     * @param error Le message d'erreur.
     * @param message Le message détaillé.
     * @return Une instance d'ErrorResponse.
     */
    public static ErrorResponse of(final int status, final String error, final String message) {
        return new ErrorResponse(status, error, message, Instant.now());
    }
}