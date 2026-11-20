package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * Les entités INDI (individus) lues dans un fichier GedCom.
 *
 * @param id L'identifiant (xref).
 * @param lineNumber Le numéro de la ligne dans le fichier GedCom où apparait cet individu.
 * @param nomComplet Le nom complet.
 * @param idFamilleEnfantList La liste des références des familles dans lesquelles cet individu est l'un des enfants.
 * @param idFamilleParentList La liste des références des familles dans lesquelles cet individu est l'un des parents.
 * @param idObjetMediaList La liste des références des objets média rattachés à cet individu.
 * @param informationComplementaireList L'ensemble des autres attributs de l'entité.
 */
public record IndividuParsed(
        String id,
        int lineNumber,
        String nomComplet,
        List<String> idFamilleEnfantList,
        List<String> idFamilleParentList,
        List<String> idObjetMediaList,
        List<InformationComplementaire> informationComplementaireList) {

    public IndividuParsed {
        idFamilleEnfantList = idFamilleParentList == null ? List.of() : List.copyOf(idFamilleEnfantList);
        idFamilleParentList = idFamilleParentList == null ? List.of() : List.copyOf(idFamilleParentList);
        idObjetMediaList = idObjetMediaList == null ? List.of() : List.copyOf(idObjetMediaList);
        informationComplementaireList = informationComplementaireList == null ? List.of() : List.copyOf(informationComplementaireList);
    }
}
