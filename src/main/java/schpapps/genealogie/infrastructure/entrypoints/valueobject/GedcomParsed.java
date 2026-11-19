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
 * @param objetMediaList La liste des entités "Objet Média".
 * @param rapportDiagnosticList La liste des erreurs détectées dans le fichier.
 */
public record GedcomParsed(String filename,
        LocalDateTime dateUpload,
        HeaderParsed header,
        List<IndividuParsed> individuList,
        List<FamilleParsed> familleList,
        List<ObjetMediaParsed> objetMediaList,
        List<RapportDiagnostic> rapportDiagnosticList) {

    public GedcomParsed {
        individuList = individuList == null ? List.of() : List.copyOf(individuList);
        familleList = familleList == null ? List.of() : List.copyOf(familleList);
        objetMediaList = objetMediaList == null ? List.of() : List.copyOf(objetMediaList);
        rapportDiagnosticList = rapportDiagnosticList == null ? List.of() : List.copyOf(rapportDiagnosticList);
    }
}
