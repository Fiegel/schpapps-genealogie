package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * Les entités INDI (individus) lues dans un fichier Gedcom.
 *
 * @param id L'identifiant (xref).
 * @param informationComplementaireList L'ensemble des attributs de l'entité.
 */
public record IndividuParsed(String id, List<InformationComplementaire> informationComplementaireList) {

    public IndividuParsed {
        informationComplementaireList = informationComplementaireList == null ? List.of() : List.copyOf(informationComplementaireList);
    }
}
