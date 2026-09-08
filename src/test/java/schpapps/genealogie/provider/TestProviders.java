package schpapps.genealogie.provider;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Classe utilitaire fournissant des données pour les tests unitaires paramétrés.
 */
public final class TestProviders {

    private TestProviders() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Fournit un flux de chaînes de caractères vides ou contenant uniquement des espaces blancs.
     *
     * @return Un flux de chaînes de caractères vides ou contenant uniquement des espaces blancs.
     */
    public static Stream<String> blankStrings() {
        return Stream.of("",    // Chaîne vide
                " ",            // Espace classique (U+0020)
                "\t",           // Tabulation (U+0009)
                "\n",           // Saut de ligne (U+000A)
                "\r",           // Retour chariot (U+000D)
                " \t \n \r ",   // Mélange
                "\u2000");       // Espace Unicode
    }

    /**
     * Fournit un flux de LocalDate représentant des dates dans le futur.
     *
     * @return Un flux de LocalDate représentant des dates dans le futur.
     */
    public static Stream<LocalDate> futureLocalDates() {
        return Stream.of(LocalDate.now().plusYears(100),   // 100 ans dans le futur
                LocalDate.now().plusMonths(3),            // 3 mois dans le futur
                LocalDate.now().plusDays(2));               // 2 jours dans le futur
    }
}
