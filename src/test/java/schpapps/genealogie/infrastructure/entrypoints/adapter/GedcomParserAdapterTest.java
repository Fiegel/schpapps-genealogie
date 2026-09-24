package schpapps.genealogie.infrastructure.entrypoints.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.FamilleParsed;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.GedcomParsed;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.IndividuParsed;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.SeveriteRapportDiagnostic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class GedcomParserAdapterTest {

    private GedcomParserAdapter gedcomParserAdapter;

    @BeforeEach
    void setUp() {
        gedcomParserAdapter = new GedcomParserAdapter();
    }

    @Test
    void succes_parsing_fichier_utf8_minimal_valide() throws IOException {
        // Given
        final String filenameProvided = "arbre_utf8.ged";
        final String gedcomContentProvided = """
                0 HEAD
                1 CHAR UTF-8
                0 @I1@ INDI
                1 NAME Jean /DUPONT/
                0 @F1@ FAM
                0 TRLR
                """;

        // When
        final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, filenameProvided);

        // Then
        assertThat(gedcomParsedActual).isNotNull();
        assertThat(gedcomParsedActual.filename()).isEqualTo(filenameProvided);
        assertThat(gedcomParsedActual.rapportDiagnosticList()).isEmpty();
        assertThat(gedcomParsedActual.header()).isNotNull();
        assertThat(gedcomParsedActual.individuList()).hasSize(1);
        assertThat(gedcomParsedActual.familleList()).hasSize(1);
        assertThat(gedcomParsedActual.rapportDiagnosticList()).isEmpty();

        final IndividuParsed individuParsedActual = gedcomParsedActual.individuList().getFirst();
        assertThat(individuParsedActual.id()).isEqualTo("@I1@");
        assertThat(individuParsedActual.informationComplementaireList()).hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("lineNumber", 4)
                .hasFieldOrPropertyWithValue("tag", "NAME")
                .hasFieldOrPropertyWithValue("valeur", "Jean /DUPONT/");

        final FamilleParsed familleParsedActual = gedcomParsedActual.familleList().getFirst();
        assertThat(familleParsedActual.id()).isEqualTo("@F1@");
    }

    @Test
    void succes_parsing_encodage_ansel() throws IOException {
        // Given
        // En ANSEL, l'octet 0xE2 représente l'accent aigu (placé avant la lettre)
        // 0xE2 + 'e' doit donner 'é'
        final byte[] bytesAnselProvided = new byte[]{
                '0', ' ', 'H', 'E', 'A', 'D', '\n',
                '1', ' ', 'C', 'H', 'A', 'R', ' ', 'A', 'N', 'S', 'E', 'L', '\n',
                '0', ' ', '@', 'I', '1', '@', ' ', 'I', 'N', 'D', 'I', '\n',
                '1', ' ', 'N', 'A', 'M', 'E', ' ', 'R', (byte) 0xE2, 'e', 'n', (byte) 0xE2, 'e', ' ', '/', 'D', 'U', 'P', 'O', 'N', 'T', '/', '\n',
                '0', ' ', 'T', 'R', 'L', 'R', '\n'
        };

        // When
        final GedcomParsed gedcomParsedActual;
        try (final InputStream inputStream = new ByteArrayInputStream(bytesAnselProvided)) {
            gedcomParsedActual = gedcomParserAdapter.parse(inputStream, "arbre_ansel.ged");
        }

        // Then
        assertThat(gedcomParsedActual.header()).isNotNull();
        assertThat(gedcomParsedActual.individuList()).hasSize(1);
        assertThat(gedcomParsedActual.familleList()).isEmpty();
        assertThat(gedcomParsedActual.rapportDiagnosticList()).isEmpty();

        final String nomFormateActual = gedcomParsedActual.individuList().getFirst()
                .informationComplementaireList().getFirst().valeur();
        assertThat(nomFormateActual).isEqualTo("Renée /DUPONT/");
    }

    @Test
    void echec_parsing_header_manquant() throws IOException {
        final String gedcomContent = """
                1 CHAR UTF-8
                0 @I1@ INDI
                0 TRLR
                """;

        final GedcomParsed result = parse(gedcomContent, "sans_header.ged");

        assertThat(result.rapportDiagnosticList()).hasSize(1);
        assertThat(result.rapportDiagnosticList().getFirst().severite())
                .isEqualTo(SeveriteRapportDiagnostic.SEVERE);
        assertThat(result.rapportDiagnosticList().getFirst().message())
                .contains("En-tête invalide");
        assertThat(result.headerParsed()).isNull();
    }

    @Test
    void echec_parsing_trailer_manquant() throws IOException {
        final String gedcomContent = """
                0 HEAD
                1 CHAR UTF-8
                0 @I1@ INDI
                """;

        final GedcomParsed result = parse(gedcomContent, "sans_trailer.ged");

        assertThat(result.rapportDiagnosticList()).hasSize(1);
        assertThat(result.rapportDiagnosticList().getFirst().severite())
                .isEqualTo(SeveriteRapportDiagnostic.SEVERE);
        assertThat(result.rapportDiagnosticList().getFirst().message())
                .contains("Trailer invalide");
    }

    @Test
    void avertissement_saut_de_niveau_dans_arbre() throws IOException {
        // Passage direct du niveau 1 au niveau 3 sans passer par le niveau 2
        final String gedcomContent = """
                0 HEAD
                1 CHAR UTF-8
                0 @I1@ INDI
                1 NAME Jean /DUPONT/
                3 BIRT
                0 TRLR
                """;

        final GedcomParsed result = parse(gedcomContent, "saut_niveau.ged");

        assertThat(result.rapportDiagnosticList()).hasSize(1);
        assertThat(result.rapportDiagnosticList().getFirst().severite())
                .isEqualTo(SeveriteRapportDiagnostic.WARNING);
        assertThat(result.rapportDiagnosticList().getFirst().message())
                .contains("Saut de niveau détecté");
    }

    private GedcomParsed parse(final String content, final String filename) throws IOException {
        try (final InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.ISO_8859_1))) {
            return gedcomParserAdapter.parse(is, filename);
        }
    }
}