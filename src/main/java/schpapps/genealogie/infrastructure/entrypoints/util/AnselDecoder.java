package schpapps.genealogie.infrastructure.entrypoints.util;

import java.text.Normalizer;

public final class AnselDecoder {

    private AnselDecoder() {
    }

    public static String decoderLigneAnsel(final String ligne) {
        if (ligne == null || ligne.isBlank()) {
            return ligne;
        }

        if (!hasAnselDiacritics(ligne)) {
            return ligne;
        }

        final StringBuilder sb = new StringBuilder(ligne.length());
        final int length = ligne.length();

        for (int i = 0; i < length; i++) {
            final char c = ligne.charAt(i);
            final String combiningMark = getCombiningMark(c);

            if (combiningMark != null) {
                if (i + 1 < length) {
                    sb.append(ligne.charAt(i + 1)); // Lettre de base
                    sb.append(combiningMark);       // Accent combinatoire Unicode
                    i++;                            // Consommation de la lettre
                } else {
                    sb.append(combiningMark);
                }
            } else {
                sb.append(c);
            }
        }

        return Normalizer.normalize(sb.toString(), Normalizer.Form.NFC);
    }

    // Balayage des octets ANSEL (plage 0xE1 à 0xF0)
    private static boolean hasAnselDiacritics(String line) {
        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);

            if (c >= 0xE1 && c <= 0xF0) {
                return true;
            }
        }
        return false;
    }

    // Mapping instantané sans allocation d'objets Map
    private static String getCombiningMark(char c) {
        return switch (c) {
            case 0xE1 -> "\u0300"; // Accent grave  (à, è, ù)
            case 0xE2 -> "\u0301"; // Accent aigu   (é)
            case 0xE3 -> "\u0302"; // Circonflexe  (â, ê, î, ô, û)
            case 0xE4 -> "\u0303"; // Tilde        (ñ)
            case 0xE8 -> "\u0308"; // Tréma        (ë, ï, ü)
            case 0xEA -> "\u030A"; // Rond en chef (å)
            case 0xF0 -> "\u0327"; // Cédille      (ç)
            default -> null;
        };
    }
}
