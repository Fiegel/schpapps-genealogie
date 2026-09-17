package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * L'entité HEAD (header) lue dans un fichier Gedcom.
 *
 * @param informationComplementaireList L'ensemble des attributs de l'entité.
 */
public record HeaderParsed(List<InformationComplementaire> informationComplementaireList) {

    public HeaderParsed {
        informationComplementaireList = informationComplementaireList == null ? List.of() : List.copyOf(informationComplementaireList);
    }
}
