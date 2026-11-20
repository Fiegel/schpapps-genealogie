package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * L'entité HEAD (header) lue dans un fichier Gedcom.
 *
 * @param source Le logiciel utilisé pour créer le fichier Gedcom.
 * @param nomFichier Le nom du fichier original.
 * @param informationComplementaireList L'ensemble des attributs de l'entité.
 */
public record HeaderParsed(
        String source,
        String nomFichier,
        List<InformationComplementaire> informationComplementaireList
) {
    public HeaderParsed {
        informationComplementaireList = informationComplementaireList == null ? List.of() : List.copyOf(informationComplementaireList);
    }
}
