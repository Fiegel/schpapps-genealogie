package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * Les entités FAM (familles) lues dans un fichier Gedcom.
 *
 * @param id L'identifiant (xref).
 * @param informationComplementaireList L'ensemble des attributs de l'entité.
 */
public record FamilleParsed(String id, List<InformationComplementaire> informationComplementaireList) {

    public FamilleParsed {
        informationComplementaireList = informationComplementaireList == null ? List.of() : List.copyOf(informationComplementaireList);
    }
}
