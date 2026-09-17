package schpapps.genealogie.infrastructure.entrypoints;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import schpapps.genealogie.infrastructure.entrypoints.adapter.GedcomParserAdapter;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.GedcomParsed;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.RapportDiagnostic;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.SeveriteRapportDiagnostic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires des endpoints REST sur les fichiers GEDCOM.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("resource")
class GedcomResourceTest {

    @Mock
    private FileUpload fileUpload;

    @Mock
    private GedcomParserAdapter gedcomParserAdapter;

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
    void echec_importerGedcom_diagnostic_severe(@TempDir final Path tempDir) throws IOException {
        // Given
        final String fileNameProvided = "arbre.ged";
        final Path fichierGedcomProvided = Files.createFile(tempDir.resolve(fileNameProvided));

        final List<RapportDiagnostic> rapportDiagnosticListExpected = List.of(new RapportDiagnostic(1,
                SeveriteRapportDiagnostic.SEVERE,
                "Ligne 1 : En-tête invalide."));
        final GedcomParsed gedcomParsedExpected = new GedcomParsed(fileNameProvided,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                Collections.emptyList(),
                Collections.emptyList(),
                rapportDiagnosticListExpected);

        when(fileUpload.filePath()).thenReturn(fichierGedcomProvided);
        when(fileUpload.fileName()).thenReturn(fileNameProvided);
        when(gedcomParserAdapter.parse(any(InputStream.class), eq(fileNameProvided))).thenReturn(gedcomParsedExpected);

        // When
        final Response responseActual = gedcomResource.importerGedcom(fileUpload);

        // Then
        assertThat(responseActual.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(responseActual.getEntity()).isEqualTo(rapportDiagnosticListExpected);
    }

    @Test
    void succes_importerGedcom_retour_200(@TempDir final Path tempDir) throws IOException {
        // Given
        final String fileNameProvided = "arbre.ged";
        final Path fichierGedcomProvided = Files.createFile(tempDir.resolve(fileNameProvided));

        final GedcomParsed gedcomParsedExpected = new GedcomParsed(fileNameProvided,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        when(fileUpload.filePath()).thenReturn(fichierGedcomProvided);
        when(fileUpload.fileName()).thenReturn(fileNameProvided);
        when(gedcomParserAdapter.parse(any(InputStream.class), eq(fileNameProvided))).thenReturn(gedcomParsedExpected);

        // When
        final Response responseActual = gedcomResource.importerGedcom(fileUpload);

        // Then
        assertThat(responseActual.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(responseActual.getEntity()).isEqualTo(gedcomParsedExpected);
    }
}
