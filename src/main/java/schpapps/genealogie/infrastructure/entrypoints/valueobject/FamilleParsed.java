package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * Les entités FAM (familles) lues dans un fichier GedCom.
 *
 * @param id L'identifiant (xref).
 * @param lineNumber Le numéro de la ligne dans le fichier GedCom où apparait cette famille.
 * @param idIndividuParent1 La référence du premier individu parent (père).
 * @param idIndividuParent2 La référence du second individu parent (mère).
 * @param idIndividuEnfantList La liste des références des individus enfants de la famille.
 * @param idObjetMediaList La liste des références des objets média rattachés à cette famille.
 * @param informationComplementaireList L'ensemble des autres attributs de l'entité.
 */
public record FamilleParsed(
        String id,
        int lineNumber,
        String idIndividuParent1,
        String idIndividuParent2,
        List<String> idIndividuEnfantList,
        List<String> idObjetMediaList,
        List<InformationComplementaire> informationComplementaireList) {

    public FamilleParsed {
        idIndividuEnfantList = idIndividuEnfantList == null ? List.of() : List.copyOf(idIndividuEnfantList);
        idObjetMediaList = idObjetMediaList == null ? List.of() : List.copyOf(idObjetMediaList);
        informationComplementaireList = informationComplementaireList == null ? List.of() : List.copyOf(informationComplementaireList);
    }
}
