package schpapps.genealogie.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Provider d'un stream de plusieurs dates invalides en généalogie (futur).
 */
public class FutureLocalDateProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) {
        return Stream.of(LocalDate.now().plusYears(100),   // 100 ans dans le futur
                        LocalDate.now().plusMonths(3),            // 3 mois dans le futur
                        LocalDate.now().plusDays(2))                // 2 jours dans le futur
                .map(Arguments::of);
    }
}
