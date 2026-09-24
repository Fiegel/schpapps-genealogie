package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Un fichier Gedcom parsé et vérifié techniquement.
 *
 * @param filename Le nom du fichier uploadé.
 * @param dateUpload La date de l'upload.
 * @param header Les informations de l'entité "Header".
 * @param individuList La liste des entités "Individu".
 * @param familleList La liste des entités "Famille".
 * @param rapportDiagnosticList La liste des erreurs détectées dans le fichier.
 */
public record GedcomParsed(String filename,
        LocalDateTime dateUpload,
        HeaderParsed header,
        List<IndividuParsed> individuList,
        List<FamilleParsed> familleList,
        List<RapportDiagnostic> rapportDiagnosticList) {

    public GedcomParsed {
        individuList = individuList == null ? List.of() : List.copyOf(individuList);
        familleList = familleList == null ? List.of() : List.copyOf(familleList);
        rapportDiagnosticList = rapportDiagnosticList == null ? List.of() : List.copyOf(rapportDiagnosticList);
    }
}
