package schpapps.genealogie.infrastructure.entrypoints.valueobject;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enum représentant les caractères spéciaux Ansel et leur correspondance Unicode.
 */
public enum AnselSpecialChar {

    ACCENT_GRAVE(0xE1, "\u0300"),
    ACCENT_AIGU(0xE2, "\u0301"),
    ACCENT_CIRCONFLEXE(0xE3, "\u0302"),
    TILDE(0xE4, "\u0303"),
    TREMA(0xE8, "\u0308"),
    CEDILLE(0xF0, "\u0327");

    private int anselCode;
    private String unicodeChar;

    public static final Map<Byte, AnselSpecialChar> ANSEL_CODE_MAP = Arrays.stream(AnselSpecialChar.values())
            .collect(Collectors.toUnmodifiableMap(ansel -> (byte) ansel.anselCode, Function.identity()));

    /**
     * Constructeur valué.
     *
     * @param anselCode   Le code Ansel du caractère spécial.
     * @param unicodeChar Le caractère Unicode correspondant.
     */
    AnselSpecialChar(final int anselCode, final String unicodeChar) {
        this.anselCode = anselCode;
        this.unicodeChar = unicodeChar;
    }

    /**
     * Retourne le code Ansel correspondant au byte donné.
     *
     * @param anselCode Le code Ansel à rechercher.
     * @return L'instance AnselSpecialChar correspondante, ou null si non trouvée.
     */
    public AnselSpecialChar getByAnselCode(final byte anselCode) {
        return ANSEL_CODE_MAP.get(anselCode);
    }
}
