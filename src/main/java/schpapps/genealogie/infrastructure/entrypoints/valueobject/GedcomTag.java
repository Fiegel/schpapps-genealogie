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

    ABBREVIATION("ABBR"),
    ADDRESS("ADDR"),
    ADDRESS_LINE_1("ADR1"),
    ADDRESS_LINE_2("ADR2"),
    ADDRESS_LINE_3("ADR3"),
    ADOPTION("ADOP"),
    AGE("AGE"),
    AGENCY("AGNC"),
    ALIAS("ALIA"),
    ASSESSMENT_QUALITY("QUAY"),
    ASSOCIATION("ASSO"),
    AUTHOR("AUTH"),
    BAPTISM("BAPM"),            // Baptême (générique)
    BAPTISM_MORMON("BAPL"),     // Baptême (église SDJ)
    BAR_MITZVAH("BARM"),
    BAS_MITZVAH("BASM"),
    BENEDICTION("BENE"),
    BIRTH("BIRT"),
    BURIAL("BURI"),
    CALL_NUMBER("CALL"),
    CASTE("CAST"),
    CENSUS("CENS"),             // Recensement
    CHANGE("CHAN"),
    CHILD("CHIL"),
    CHILD_NUMBER("NCHI"),
    CHRISTENING("CHR"),         // Baptême (enfance)
    CHRISTENING_ADULT("CHRA"),  // Baptême (adulte)
    CITY("CITY"),
    CONCATENATION("CONC"),
    CONFIRMATION("CONF"),
    CONFIRMATION_MORMON("CONL"),
    COPYRIGHT("COPR"),
    CORPORATION("CORP"),
    COUNTRY("CTRY"),
    CREATION("CREA"),
    CREMATION("CREM"),
    DATA("DATA"),
    DATE("DATE"),
    DEATH("DEAT"),
    DESCRIPTION("DSCR"),
    DESTINATION("DEST"),
    DIVORCE("DIV"),
    DIVORCE_FILED("DIVF"),
    EDUCATION("EDUC"),
    EMAIL("EMAIL"),
    EMIGRATION("EMIG"),
    ENCODING("CHAR"),
    ENGAGEMENT("ENGA"),         // Fiançailles
    EVENT("EVEN"),
    EVENT_CAUSE("CAUS"),
    EXTERNAL_FILE("FILE"),
    EXTERNAL_ID("EXID"),
    FACT("FACT"),
    FAMILY("FAM"),
    FAMILY_CHILD("FAMC"),
    FAMILY_SPOUSE("FAMS"),
    FAX("FAX"),
    FIRST_COMMUNION("FCOM"),
    FORMAT("FORM"),
    GEDCOM_METADATA("GEDC"),
    GIVEN_NAME("GIVN"),         // Prénom(s)
    GRADUATION("GRAD"),         // Diplôme
    HEADER("HEAD"),
    HUSBAND("HUSB"),
    IDENTITY_NUMBER("IDNO"),
    IMMIGRATION("IMMI"),
    INDIVIDUAL("INDI"),
    LANGUAGE("LANG"),
    LATITUDE("LATI"),
    LINE_CONTINUATION("CONT"),
    LONGITUDE("LONG"),
    MAP("MAP"),
    MARRIAGE("MARR"),
    MARRIAGE_ANNULMENT("ANUL"),
    MARRIAGE_BANNS("MARB"),
    MARRIAGE_CONTRACT("MARC"),
    MARRIAGE_LICENSE("MARL"),
    MARRIAGE_NUMBER("NMR"),
    MARRIAGE_SETTLEMENT("MARS"),
    MARRIED_NAME("MARNM"),
    MEDIA("MEDI"),
    MIME_TYPE("MIME"),
    NAME("NAME"),               // Nom complet
    NAME_PREFIX("NPFX"),
    NAME_SUFFIX("NSFX"),
    NATIONALITY("NATI"),
    NATURALIZATION("NATU"),
    NICKNAME("NICK"),
    NOTE("NOTE"),
    NO_EVENT("NO"),
    OBJECT("OBJE"),
    OCCUPATION("OCCU"),         // Profession
    ORDINATION("ORDN"),
    PAGE_NUMBER("PAGE"),
    PEDIGREE("PEDI"),
    PHONETIC("FONE"),
    PHONE_NUMBER("PHON"),
    PHRASE("PHRASE"),
    PLACE("PLAC"),
    POSTAL_CODE("POST"),
    PROBATE("PROB"),
    PROPERTY("PROP"),
    PUBLICATION("PUBL"),
    REFERENCE_NUMBER("REFN"),
    RELATIONSHIP("RELA"),
    RELIGION("RELI"),
    REPOSITORY("REPO"),
    RESIDENCE("RESI"),
    RETIREMENT("RETI"),         // Retraite
    ROLE("ROLE"),
    ROMANIZED_NAME("ROMN"),
    SCHEMA("SCHMA"),
    SEX("SEX"),
    SHARED_NOTE("SNOTE"),
    SOCIAL_SECURITY_NUMBER("SSN"),
    SOURCE("SOUR"),
    STATE("STAE"),
    STATUS("STAT"),
    SUBMITTER("SUBM"),
    SURNAME("SURN"),            // Nom de famille
    SURNAME_PREFIX("SPFX"),
    TEXT("TEXT"),
    TIME("TIME"),
    TITLE("TITL"),
    TRAILER("TRLR"),
    TRANSLATION("TRAN"),
    TYPE("TYPE"),
    UNIQUE_ID("UID"),
    VERSION("VERS"),
    WIFE("WIFE"),
    WILL("WILL"),               // Testament
    WWW("WWW");

    private final String tagName;

    private static final Map<String, GedcomTag> GEDCOM_TAG_BY_NAME_MAP = Arrays.stream(GedcomTag.values()).collect(Collectors.toUnmodifiableMap(GedcomTag::getTagName, Function.identity()));

    private static final Set<GedcomTag> GEDCOM_TAG_LEVEL_0_SET = Set.of(HEADER, TRAILER, INDIVIDUAL, FAMILY, SOURCE, REPOSITORY, NOTE, OBJECT, SHARED_NOTE, SUBMITTER);
    private static final Map<String, GedcomTag> GEDCOM_TAG_ENTITE_BY_NAME_MAP = GEDCOM_TAG_LEVEL_0_SET.stream().collect(Collectors.toUnmodifiableMap(GedcomTag::getTagName, Function.identity()));

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
