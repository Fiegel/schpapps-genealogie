package schpapps.genealogie.infrastructure.entrypoints.handler;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import schpapps.genealogie.infrastructure.entrypoints.dto.ErrorResponse;

import java.io.IOException;

/**
 * Gestion des réponses en cas d'exceptions.
 */
public class GlobalExceptionHandler {

    /**
     * Gère les erreurs de validation génériques des arguments dans les commandes reçues par le métier.
     *
     * @param ex L'exception levée en cas d'argument illégal.
     * @return La réponse en code 400.
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException ex) {
        var error = ErrorResponse.of(Response.Status.BAD_REQUEST.getStatusCode(),
                "Bad Request",
                "Argument illégal : " + ex.getMessage());

        return RestResponse.status(Response.Status.BAD_REQUEST, error);
    }

    /**
     * Intercepte les erreurs liées aux fichiers (upload GEDCOM corrompu, etc.).
     *
     * @param ex L'exception levée en cas d'erreur de traitement de fichier.
     * @return La réponse en code 400.
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleIOException(final IOException ex) {
        var error = ErrorResponse.of(Response.Status.BAD_REQUEST.getStatusCode(),
                "Unprocessable Entity",
                "Erreur lors du traitement du fichier : " + ex.getMessage());

        return RestResponse.status(Response.Status.BAD_REQUEST, error);
    }

    /**
     * Fallback pour toutes les autres exceptions non gérées (500).
     *
     * @param ex L'exception levée.
     * @return La réponse en code 500.
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleGenericException(final Throwable ex) {
        var error = ErrorResponse.of(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Internal Server Error",
                "Une erreur inattendue est survenue.");

        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, error);
    }
}
