package schpapps.genealogie.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provider d'un stream de plusieurs chaines de caractères "blank".
 */
public class BlankStringsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) {
        return Stream.of("",    // Chaîne vide
                        " ",            // Espace classique (U+0020)
                        "\t",           // Tabulation (U+0009)
                        "\n",           // Saut de ligne (U+000A)
                        "\r",           // Retour chariot (U+000D)
                        " \t \n \r ",   // Mélange
                        "\u2000")       // Espace Unicode
                .map(Arguments::of);
    }
}
