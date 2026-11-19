package schpapps.genealogie.infrastructure.entrypoints.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import schpapps.genealogie.infrastructure.entrypoints.valueobject.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class GedcomParserAdapterTest {

    private GedcomParserAdapter gedcomParserAdapter;

    @BeforeEach
    void setUp() {
        gedcomParserAdapter = new GedcomParserAdapter();
    }

    // =========================================================================
    // 1. CAS NOMINAUX & ENCODAGE
    // =========================================================================

    @Nested
    @DisplayName("Cas nominaux et Encodage")
    class CasNominauxTest {

        @Test
        @DisplayName("Doit parser avec succès un fichier UTF-8 minimal et cohérent sans erreur")
        void succes_parsing_fichier_utf8_minimal_valide() throws IOException {
            // Given
            final String filenameProvided = "arbre_utf8.ged";
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    1 NAME Jean /DUPONT/
                    1 SEX M
                    1 FAMS @F1@
                    0 @I2@ INDI
                    1 NAME Marie-Jeanne /DUPONT/
                    1 SEX F
                    1 FAMC @F1@
                    0 @F1@ FAM
                    1 HUSB @I1@
                    1 CHIL @I2@
                    1 NCHI 1
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, filenameProvided);

            // Then
            assertThat(gedcomParsedActual).isNotNull();
            assertThat(gedcomParsedActual.filename()).isEqualTo(filenameProvided);
            assertThat(gedcomParsedActual.header()).isNotNull();
            assertThat(gedcomParsedActual.rapportDiagnosticList()).isEmpty();
            assertThat(gedcomParsedActual.individuList()).hasSize(2);
            assertThat(gedcomParsedActual.familleList()).hasSize(1);

            final IndividuParsed individuParentParsedActual = gedcomParsedActual.individuList().getFirst();
            assertThat(individuParentParsedActual.id()).isEqualTo("@I1@");
            assertThat(individuParentParsedActual.lineNumber()).isEqualTo(3);
            assertThat(individuParentParsedActual.nomComplet()).isEqualTo("Jean /DUPONT/");
            assertThat(individuParentParsedActual.idFamilleEnfantList()).isEmpty();
            assertThat(individuParentParsedActual.idFamilleParentList()).containsExactly("@F1@");
            assertThat(individuParentParsedActual.informationComplementaireList()).hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("lineNumber", 5)
                    .hasFieldOrPropertyWithValue("tag", "SEX")
                    .hasFieldOrPropertyWithValue("valeur", "M");

            final IndividuParsed individuEnfantParsedActual = gedcomParsedActual.individuList().getLast();
            assertThat(individuEnfantParsedActual.id()).isEqualTo("@I2@");
            assertThat(individuEnfantParsedActual.lineNumber()).isEqualTo(7);
            assertThat(individuEnfantParsedActual.nomComplet()).isEqualTo("Marie-Jeanne /DUPONT/");
            assertThat(individuEnfantParsedActual.idFamilleEnfantList()).containsExactly("@F1@");
            assertThat(individuEnfantParsedActual.idFamilleParentList()).isEmpty();
            assertThat(individuEnfantParsedActual.informationComplementaireList()).hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("lineNumber", 9)
                    .hasFieldOrPropertyWithValue("tag", "SEX")
                    .hasFieldOrPropertyWithValue("valeur", "F");

            final FamilleParsed familleParsedActual = gedcomParsedActual.familleList().getFirst();
            assertThat(familleParsedActual.id()).isEqualTo("@F1@");
            assertThat(familleParsedActual.lineNumber()).isEqualTo(11);
            assertThat(familleParsedActual.idIndividuParent1()).isEqualTo("@I1@");
            assertThat(familleParsedActual.idIndividuParent2()).isNull();
            assertThat(familleParsedActual.idIndividuEnfantList()).containsExactly("@I2@");
            assertThat(familleParsedActual.informationComplementaireList()).hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("lineNumber", 14)
                    .hasFieldOrPropertyWithValue("tag", "NCHI")
                    .hasFieldOrPropertyWithValue("valeur", "1");
        }

        @Test
        @DisplayName("Doit décoder correctement les caractères spécifiques de l'encodage ANSEL")
        void succes_parsing_encodage_ansel() throws IOException {
            // Given
            // En ANSEL, l'octet 0xE2 représente l'accent aigu (placé avant la lettre)
            // 0xE2 + 'e' doit donner 'é'
            final byte[] bytesAnselProvided = new byte[]{
                    '0', ' ', 'H', 'E', 'A', 'D', '\n',
                    '1', ' ', 'C', 'H', 'A', 'R', ' ', 'A', 'N', 'S', 'E', 'L', '\n',
                    '0', ' ', '@', 'I', '1', '@', ' ', 'I', 'N', 'D', 'I', '\n',
                    '1', ' ', 'N', 'A', 'M', 'E', ' ', 'R', 'e', 'n', (byte) 0xE2, 'e', 'e', ' ', '/', 'D', 'U', 'P', 'O', 'N', 'T', '/', '\n',
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

            final String nomFormateActual = gedcomParsedActual.individuList().getFirst().nomComplet();
            assertThat(nomFormateActual).isEqualTo("Renée /DUPONT/");
        }
    }

    // =========================================================================
    // 2. STRUCTURE GLOBALE (HEAD & TRLR)
    // =========================================================================

    @Nested
    @DisplayName("Validation de la structure globale (HEAD et TRLR)")
    class StructureGlobaleTest {

        @Test
        @DisplayName("Doit échouer proprement si le fichier est complètement vide")
        void echec_parsing_fichier_vide() throws IOException {
            // When
            final GedcomParsed gedcomParsedActual = parse("", "vide.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().severite()).isEqualTo(SeveriteRapportDiagnostic.SEVERE);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().message()).contains("Fichier vide invalide");
        }

        @Test
        @DisplayName("Doit échouer si le tag HEAD est absent en première ligne non vide")
        void echec_parsing_header_manquant() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "sans_header.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().severite()).isEqualTo(SeveriteRapportDiagnostic.SEVERE);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().message()).contains("En-tête invalide");
            assertThat(gedcomParsedActual.header()).isNull();
        }

        @Test
        @DisplayName("Doit réussir si le HEAD est précédé de lignes vides")
        void succes_parsing_header_avec_lignes_vides_au_debut() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    
                    
                    0 HEAD
                    1 CHAR UTF-8
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "header_espaces.ged");

            // Then
            assertThat(gedcomParsedActual.header()).isNotNull();
            assertThat(gedcomParsedActual.rapportDiagnosticList()).isEmpty();
        }

        @Test
        @DisplayName("Doit échouer si le tag TRLR est absent en fin de fichier")
        void echec_parsing_trailer_manquant() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "sans_trailer.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().severite()).isEqualTo(SeveriteRapportDiagnostic.SEVERE);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().message()).contains("Trailer invalide");
        }

        @Test
        @DisplayName("Doit réussir si le TRLR est suivi de lignes vides en fin de fichier")
        void succes_parsing_trailer_avec_lignes_vides_a_la_fin() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 TRLR
                    
                    
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "trailer_espaces.ged");

            // Then
            assertThat(gedcomParsedActual.header()).isNotNull();
            assertThat(gedcomParsedActual.rapportDiagnosticList()).isEmpty();
        }
    }

    // =========================================================================
    // 3. HIÉRARCHIE ET SAUTS DE NIVEAU
    // =========================================================================

    @Nested
    @DisplayName("Gestion de la hiérarchie et de l'arbre")
    class ArbreEtNiveauxTest {

        @Test
        @DisplayName("Doit émettre un SEVERE si un tag de niveau 0 est totalement inconnu")
        void echec_tag_racine_inconnu() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @X1@ TAG_INVENTE
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "tag_inconnu.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().severite()).isEqualTo(SeveriteRapportDiagnostic.SEVERE);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().message()).contains("inconnu pour une entité de niveau 0");
        }

        @Test
        @DisplayName("Doit émettre une INFO pour un tag de niveau 0 non supporté")
        void info_tag_racine_non_traite() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @S1@ SOUR
                    1 TITL Registre paroissial
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "sour_niveau_0.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);

            final RapportDiagnostic rapportDiagnosticActual = gedcomParsedActual.rapportDiagnosticList().getFirst();
            assertThat(rapportDiagnosticActual.severite()).isEqualTo(SeveriteRapportDiagnostic.INFO);
            assertThat(rapportDiagnosticActual.message()).contains("non traité actuellement");
        }

        @Test
        @DisplayName("Doit émettre un WARNING lors d'un saut de niveau et rattacher au parent valide")
        void avertissement_saut_de_niveau_dans_arbre() throws IOException {
            // Given
            // Passage direct du niveau 1 au niveau 3 sans passer par le niveau 2
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    1 NAME Jean /DUPONT/
                    3 BIRT
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "saut_niveau.ged");

            // Then
            assertThat(gedcomParsedActual.individuList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);

            final RapportDiagnostic rapportDiagnosticActual = gedcomParsedActual.rapportDiagnosticList().getFirst();
            assertThat(rapportDiagnosticActual.severite()).isEqualTo(SeveriteRapportDiagnostic.WARNING);
            assertThat(rapportDiagnosticActual.message()).contains("Saut de niveau détecté");
        }

        @Test
        @DisplayName("Doit émettre un WARNING et rattacher à la racine si aucun niveau parent valide n'existe")
        void avertissement_saut_de_niveau_sans_parent_valide() throws IOException {
            // Given
            // Passage direct de 0 INDI à un niveau 2
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    2 PLAC Paris
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "saut_niveau_racine.ged");

            // Then
            assertThat(gedcomParsedActual.individuList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);

            final RapportDiagnostic rapportDiagnosticActual = gedcomParsedActual.rapportDiagnosticList().getFirst();
            assertThat(rapportDiagnosticActual.severite()).isEqualTo(SeveriteRapportDiagnostic.WARNING);
            assertThat(rapportDiagnosticActual.message()).contains("Saut de niveau détecté");

            final IndividuParsed individuParsedActual = gedcomParsedActual.individuList().getFirst();
            assertThat(individuParsedActual.id()).isEqualTo("@I1@");
            assertThat(individuParsedActual.informationComplementaireList()).hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("lineNumber", 4)
                    .hasFieldOrPropertyWithValue("tag", "PLAC")
                    .hasFieldOrPropertyWithValue("valeur", "Paris");
        }

        @ParameterizedTest(name = "Doit émettre un SEVERE si un {0} est dupliqué")
        @MethodSource("fournirCasEntiteNiveau0Dupliquee")
        void echec_entite_niveau_0_dupliquee(final String tagEntiteProvided, final String gedcomContentProvided, final String messageExpected) throws IOException {
            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "doublon.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);

            final RapportDiagnostic rapportDiagnosticActual = gedcomParsedActual.rapportDiagnosticList().getFirst();
            assertThat(rapportDiagnosticActual.severite()).isEqualTo(SeveriteRapportDiagnostic.SEVERE);
            assertThat(rapportDiagnosticActual.message()).contains(messageExpected);
        }

        @Test
        @DisplayName("Doit concaténer les lignes CONC et continuer les lignes CONT avec des sauts de ligne")
        void succes_traitement_conc_et_cont() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    1 NAME Jean /DUPONT/
                    1 NOTE Ceci est le début d'un texte très long
                    2 CONC  qui a été découpé.
                    2 CONT Et voici un second paragraphe.
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "conc_cont.ged");

            // Then

            assertThat(gedcomParsedActual.individuList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList()).isEmpty();

            final IndividuParsed individuParsedActual = gedcomParsedActual.individuList().getFirst();
            assertThat(individuParsedActual.id()).isEqualTo("@I1@");
            assertThat(individuParsedActual.informationComplementaireList()).hasSize(1)
                    .first()
                    .hasFieldOrPropertyWithValue("lineNumber", 5)
                    .hasFieldOrPropertyWithValue("tag", "NOTE")
                    .hasFieldOrPropertyWithValue("valeur", "Ceci est le début d'un texte très long qui a été découpé.\nEt voici un second paragraphe.");
        }

        @Test
        @DisplayName("Doit émettre un WARNING si un tag CONT est orphelin sans parent")
        void avertissement_cont_orphelin() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    2 CONT Ligne orpheline
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "cont_orphelin.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().severite()).isEqualTo(SeveriteRapportDiagnostic.WARNING);
            assertThat(gedcomParsedActual.rapportDiagnosticList().getFirst().message()).contains("orphelin");
        }

        private static Stream<Arguments> fournirCasEntiteNiveau0Dupliquee() {
            return Stream.of(Arguments.of("INDI",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @I1@ INDI
                                    1 NAME Jean /DUPONT/
                                    0 @I1@ INDI
                                    1 NAME Pierre /DUPONT/
                                    0 TRLR
                                    """,
                            "Identifiant d'individu @I1@ déjà utilisé"),
                    Arguments.of("FAM",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @I1@ INDI
                                    0 @F1@ FAM
                                    1 HUSB @I1@
                                    0 @F1@ FAM
                                    1 HUSB @I1@
                                    0 TRLR
                                    """,
                            "Identifiant de famille @F1@ déjà utilisé"),
                    Arguments.of("OBJE",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @O1@ OBJE
                                    1 TITL Photo 1
                                    0 @O1@ OBJE
                                    1 TITL Photo 2
                                    0 TRLR
                                    """,
                            "Identifiant d'objet média @O1@ déjà utilisé"));
        }
    }

    // =========================================================================
    // 4. VALIDATION DES RÉFÉRENCES CROISÉES (RÈGLES MÉTIER)
    // =========================================================================

    @Nested
    @DisplayName("Validation des références croisées INDI <-> FAM")
    class ReferencesCroiseesTest {

        @ParameterizedTest(name = "{0}")
        @MethodSource("fournirCasIncoherencesReferences")
        @DisplayName("Doit détecter les incohérences de références croisées avec la sévérité appropriée")
        void validation_references_croisees(final String description,
                final String gedcomContentProvided,
                final SeveriteRapportDiagnostic severiteExpected,
                final String messageExpected) throws IOException {

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "test_ref.ged");

            // Then
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);

            final RapportDiagnostic rapportDiagnosticActual = gedcomParsedActual.rapportDiagnosticList().getFirst();
            assertThat(rapportDiagnosticActual.severite()).isEqualTo(severiteExpected);
            assertThat(rapportDiagnosticActual.message()).contains(messageExpected);
        }

        private static Stream<Arguments> fournirCasIncoherencesReferences() {
            return Stream.of(Arguments.of("Famille sans aucun membre",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @F1@ FAM
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.SEVERE,
                            "La famille @F1@ n'a aucun individu"),
                    Arguments.of("Famille avec un parent HUSB inexistant",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @F1@ FAM
                                    1 HUSB @I999@
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.SEVERE,
                            "référence un HUSB inexistant : @I999@"),
                    Arguments.of("Famille avec un parent WIFE inexistant",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @F1@ FAM
                                    1 WIFE @I999@
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.SEVERE,
                            "référence un WIFE inexistant : @I999@"),
                    Arguments.of("Famille avec enfant CHIL inexistant",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @F1@ FAM
                                    1 CHIL @I999@
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.SEVERE,
                            "référence un CHIL inexistant : @I999@"),
                    Arguments.of("Individu avec FAMC inexistante",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @I1@ INDI
                                    1 FAMC @F999@
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.SEVERE,
                            "référence une FAMC inexistante : @F999@"
                    ),
                    Arguments.of("Individu avec FAMS inexistante",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @I1@ INDI
                                    1 FAMS @F999@
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.SEVERE,
                            "référence une FAMS inexistante : @F999@"
                    ),
                    Arguments.of(
                            "Individu avec FAMC sans réciprocité CHIL",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @I1@ INDI
                                    1 FAMC @F1@
                                    0 @I2@ INDI
                                    1 FAMS @F1@
                                    0 @F1@ FAM
                                    1 HUSB @I2@
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.WARNING,
                            "indique appartenir à la FAMC @F1@, mais n'est pas dans ses CHIL"),
                    Arguments.of("Individu avec FAMS sans réciprocité HUSB/WIFE",
                            """
                                    0 HEAD
                                    1 CHAR UTF-8
                                    0 @I1@ INDI
                                    1 FAMS @F1@
                                    0 @I2@ INDI
                                    1 FAMC @F1@
                                    0 @F1@ FAM
                                    1 CHIL @I2@
                                    0 TRLR
                                    """,
                            SeveriteRapportDiagnostic.WARNING,
                            "indique appartenir à la FAMS @F1@, mais n'est ni HUSB, ni WIFE"));
        }
    }

    // =========================================================================
    // 5. OBJETS MEDIA
    // =========================================================================

    @Nested
    @DisplayName("Gestion des objets multimédias (OBJE)")
    class ObjetMediaTest {

        @Test
        @DisplayName("Doit extraire un OBJE interne sans référence et lui attribuer un identifiant généré")
        void succes_extraction_objet_media_inline() throws IOException {
            // Given
            final String gedcomContentProvided = """
                    0 HEAD
                    1 CHAR UTF-8
                    0 @I1@ INDI
                    1 NAME Jean /DUPONT/
                    1 OBJE
                    2 FILE photos/jean.jpg
                    2 FORM jpg
                    2 TITL Portrait de Jean
                    0 TRLR
                    """;

            // When
            final GedcomParsed gedcomParsedActual = parse(gedcomContentProvided, "media_inline.ged");

            // Then
            assertThat(gedcomParsedActual.individuList()).hasSize(1);
            assertThat(gedcomParsedActual.objetMediaList()).hasSize(1);
            assertThat(gedcomParsedActual.rapportDiagnosticList()).hasSize(1);

            final ObjetMediaParsed objetMediaParsedActual = gedcomParsedActual.objetMediaList().getFirst();
            assertThat(objetMediaParsedActual.id()).isEqualTo("@SCHPAPPS_OBJE_1@");
            assertThat(objetMediaParsedActual.titre()).isEqualTo("Portrait de Jean");
            assertThat(objetMediaParsedActual.cheminFichier()).isEqualTo("photos/jean.jpg");
            assertThat(objetMediaParsedActual.format()).isEqualTo("jpg");

            final IndividuParsed individuParsedActual = gedcomParsedActual.individuList().getFirst();
            assertThat(individuParsedActual.id()).isEqualTo("@I1@");
            ;
            assertThat(individuParsedActual.idObjetMediaList()).hasSize(1);
            assertThat(individuParsedActual.idObjetMediaList().getFirst()).isEqualTo("@SCHPAPPS_OBJE_1@");

            final RapportDiagnostic rapportDiagnosticActual = gedcomParsedActual.rapportDiagnosticList().getFirst();
            assertThat(rapportDiagnosticActual.severite()).isEqualTo(SeveriteRapportDiagnostic.INFO);
            assertThat(rapportDiagnosticActual.message()).contains("Objet média interne détecté");
        }
    }

    // =========================================================================
    // UTILITAIRE
    // =========================================================================

    private GedcomParsed parse(final String content, final String filename) throws IOException {
        try (final InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.ISO_8859_1))) {
            return gedcomParserAdapter.parse(is, filename);
        }
    }
}