package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * Les entités OBJE (objets média) lues dans un fichier GedCom.
 * Pour les versions GedCom inférieures à 7, les OBJE sans référence contenus dans les FAM et INDI sont traités comme des entités de level 0.
 *
 * @param id L'identifiant (xref).
 * @param lineNumber Le numéro de la ligne dans le fichier GedCom où apparait cet individu.
 * @param titre Le titre (tag TITL).
 * @param cheminFichier Le chemin indiqué du fichier (tag FILE).
 * @param format Le format du fichier (tag FORM).
 * @param informationComplementaireList L'ensemble des autres attributs de l'entité.
 */
public record ObjetMediaParsed(
        String id,
        int lineNumber,
        String titre,
        String cheminFichier,
        String format,
        List<InformationComplementaire> informationComplementaireList) {

    public ObjetMediaParsed {
        informationComplementaireList = informationComplementaireList == null ? List.of() : List.copyOf(informationComplementaireList);
    }
}
