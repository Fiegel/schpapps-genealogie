package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Les différents tags traités dans un fichier Gedcom.
 */
public enum GedcomTag {

    // Entités (tags level 0 ; attention certaines peuvent aussi être level 1)
    HEADER("HEAD"),
    TRAILER("TRLR"),
    INDIVIDUAL("INDI"),
    FAMILY("FAM"),
    SOURCE("SOUR"),
    REPOSITORY("REPO"),
    NOTE("NOTE"),
    OBJECT("OBJE"),
    SUBMITTER("SUBM"),

    // Attributs
    ENCODING("CHAR");

    private final String tagName;

    private static final Map<String, GedcomTag> GEDCOM_TAG_BY_NAME_MAP = Arrays.stream(GedcomTag.values()).collect(Collectors.toUnmodifiableMap(g -> g.tagName, Function.identity()));
    private static final Map<String, GedcomTag> GEDCOM_TAG_ENTITE_BY_NAME_MAP = Set.of(HEADER, TRAILER, INDIVIDUAL, FAMILY, SOURCE, REPOSITORY, NOTE, OBJECT, SUBMITTER).stream()
            .collect(Collectors.toUnmodifiableMap(g -> g.tagName, Function.identity()));

    /**
     * Constructeur.
     *
     * @param tagName Le nom du tag.
     */
    GedcomTag(final String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    /**
     * Retourne le tag à partir de son nom.
     *
     * @param tagName Le nom du tag recherché.
     * @return Le tag trouvé.
     */
    public static GedcomTag getByTagName(final String tagName) {
        return GEDCOM_TAG_BY_NAME_MAP.get(tagName);
    }

    /**
     * Retourne le tag à partir de son nom, s'il fait partie des tags de niveau 0 (entités).
     *
     * @param tagName Le nom du tag recherché.
     * @return Le tag trouvé.
     */
    public static GedcomTag getByEntiteTagName(final String tagName) {
        return GEDCOM_TAG_ENTITE_BY_NAME_MAP.get(tagName);
    }
}
