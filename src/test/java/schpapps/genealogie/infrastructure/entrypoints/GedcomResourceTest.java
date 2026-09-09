package schpapps.genealogie.infrastructure.entrypoints;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires des endpoints REST sur les fichiers GEDCOM.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("resource")
class GedcomResourceTest {

    @Mock
    private FileUpload fileUpload;

    @InjectMocks
    private GedcomResource gedcomResource;

    @Test
    void echec_importerGedcom_file_null() {
        assertThatThrownBy(() -> gedcomResource.importerGedcom(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fichier GEDCOM est requis");
    }

    @Test
    void echec_importerGedcom_filePath_null() {
        // Given
        when(fileUpload.filePath()).thenReturn(null);

        // When - Then
        assertThatThrownBy(() -> gedcomResource.importerGedcom(fileUpload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fichier GEDCOM est requis");
    }

    @Test
    void echec_importerGedcom_file_non_supporte() {
        // Given
        when(fileUpload.filePath()).thenReturn(Path.of("test.txt"));
        when(fileUpload.fileName()).thenReturn("test.txt");

        // When - Then
        assertThatThrownBy(() -> gedcomResource.importerGedcom(fileUpload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fichier non supporté");
    }

    @Test
    void echec_importerGedcom_filePath_inexistant() {
        // Given
        final Path pathInexistantProvided = Path.of("dossier_fictif/arbre.ged");
        when(fileUpload.filePath()).thenReturn(pathInexistantProvided);
        when(fileUpload.fileName()).thenReturn("arbre.ged");

        // When - Then
        assertThatThrownBy(() -> gedcomResource.importerGedcom(fileUpload))
                .isInstanceOf(IOException.class);
    }

    @Test
    void succes_importerGedcom_retour_200(@TempDir final Path tempDir) throws IOException {
        // Given
        final Path fichierGedcomProvided = Files.createFile(tempDir.resolve("arbre.ged"));

        when(fileUpload.filePath()).thenReturn(fichierGedcomProvided);
        when(fileUpload.fileName()).thenReturn("arbre.ged");

        // When
        final Response responseActual = gedcomResource.importerGedcom(fileUpload);

        // Then
        assertThat(responseActual.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(responseActual.getEntity()).isEqualTo("Fichier reçu : arbre.ged");
    }
}
