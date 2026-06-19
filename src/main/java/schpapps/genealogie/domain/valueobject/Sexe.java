package schpapps.genealogie.domain.valueobject;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Le sexe d'un individu.
 */
public enum Sexe {

    FEMME,
    HOMME,
    INCONNU;

    private static final Map<String, Sexe> SEXE_BY_NAME_MAP = Arrays.stream(Sexe.values()).collect(Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    /**
     * Retourne l'enum Sexe correspondant à l'enum name donné. INCONNU si non trouvé.
     *
     * @param name L'enum name.
     * @return L'enum correspondante.
     */
    public static Sexe getByName(final String name) {
        return SEXE_BY_NAME_MAP.getOrDefault(name, INCONNU);
    }
}
