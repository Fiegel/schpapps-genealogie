package schpapps.genealogie.infrastructure.entrypoints.valueobject;

/**
 * Décomposition d'une ligne de texte d'un fichier Gedcom.
 *
 * @param level Le niveau (0 = entité, 1+ = attribut).
 * @param xref L'identifiant / le pointeur de l'entité contenue dans la ligne.
 * @param tag Le type d'entité / d'attribut.
 * @param value La valeur.
 */
public record GedcomLine(int level, String xref, String tag, String value) {

    private static final String PARTS_SEPARATOR = " ";
    private static final String NO_XREF = null;
    private static final String NO_TAG = "";
    private static final String NO_VALUE = "";
    private static final String XREF_DELIMITER = "@";

    /**
     * Décompose une ligne de texte d'un fichier Gedcom selon la norme [Level Xref Tag Value], avec Xref et Value optionnels.
     *
     * @param line La ligne de texte à décomposer.
     * @return L'objet GedcomLine représentant la ligne décomposée.
     */
    public static GedcomLine parse(final String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        // Limité à 3 morceaux max : [level, tag/xref, rest]
        final String[] parts = line.trim().split(PARTS_SEPARATOR, 3);

        final int level = Integer.parseInt(parts[0]);

        if (parts.length == 1) {
            return new GedcomLine(level, NO_XREF, NO_TAG, NO_VALUE);
        }

        final String second = parts[1];

        // Gestion des pointeurs/XREF (ex: 0 @I123@ INDI)
        if (second.startsWith(XREF_DELIMITER) && second.endsWith(XREF_DELIMITER)) {
            if (parts.length > 2) {
                // Cas rencontrés uniquement dans les entités (level 0 ; comme les NOTE)
                final String[] subParts = parts[2].split(PARTS_SEPARATOR, 2);
                final String tag = subParts[0].toUpperCase();
                final String value = subParts.length > 1 ? subParts[1] : NO_VALUE;

                return new GedcomLine(level, second, tag, value);
            }

            return new GedcomLine(level, second, NO_TAG, NO_VALUE);
        }

        // Cas standard sans XREF (ex: 1 NAME Jean /DUPONT/)
        final String tag = second.toUpperCase();
        final String value = parts.length > 2 ? parts[2] : NO_VALUE;

        return new GedcomLine(level, NO_XREF, tag, value);
    }
}
