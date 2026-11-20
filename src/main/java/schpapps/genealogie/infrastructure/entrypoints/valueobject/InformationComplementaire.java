package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.List;

/**
 * Un attribut d'une entité lue dans un fichier Gedcom (provisoirement stocké sous cette forme générique).
 *
 * @param lineNumber Le numéro de la ligne de l'attribut dans le fichier d'origine.
 * @param tag Le type.
 * @param valeur La valeur (xref ou donnée).
 * @param sousInformationList Les sous-attributs éventuels (lignes avec niveau supérieur).
 */
public record InformationComplementaire(int lineNumber, String tag, String valeur, List<InformationComplementaire> sousInformationList) {

    public InformationComplementaire {
        sousInformationList = sousInformationList == null ? List.of() : List.copyOf(sousInformationList);
    }
}
