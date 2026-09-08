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
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import schpapps.genealogie.infrastructure.entrypoints.dto.ErrorResponse;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Le controller pour l'importation de fichiers GEDCOM.
 */
@Path("/api/gedcom")
@Tag(name = "GEDCOM", description = "Importation et traitement de fichiers GEDCOM")
public class GedcomResource {

    /**
     * Importe un fichier GEDCOM et lance son parsing.
     *
     * @param file Le fichier GEDCOM à importer.
     * @return La réponse indiquant le succès ou l'échec de l'importation.
     * @throws IOException En cas d'erreur de lecture du fichier.
     */
    @POST
    @Path("/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Importer un fichier GEDCOM", description = "Lecture d'un fichier .ged et lance son parsing.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Le fichier GEDCOM a été importé et traité avec succès."),
            @APIResponse(responseCode = "400",
                    description = "Fichier manquant ou illisible (IOException).",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500",
                    description = "Erreur inattendue lors de l'importation.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response importerGedcom(@RestForm("file") final FileUpload file) throws IOException {
        if (file == null || file.filePath() == null) {
            throw new IllegalArgumentException("Un fichier GEDCOM est requis.");
        }

        if (!file.fileName().toLowerCase().endsWith(".ged")) {
            throw new IllegalArgumentException("Format de fichier non supporté. Le fichier doit être un fichier .ged");
        }

        try (var inputStream = Files.newInputStream(file.filePath())) {

            // Prochaine étape : appeler le cas d'usage métier
            // var resultat = importerGedcomUseCase.executer(inputStream);

            return Response.ok().entity("Fichier reçu : " + file.fileName()).build();
        }
    }
}
