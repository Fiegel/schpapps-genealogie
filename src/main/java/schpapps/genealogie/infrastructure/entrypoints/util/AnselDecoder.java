package schpapps.genealogie.infrastructure.entrypoints.util;

import java.text.Normalizer;

/**
 * Classe utilitaire permettant de transformer les caractères ANSEL contenus dans des lignes Gedcom en caractères Unicode.
 */
public final class AnselDecoder {

    private AnselDecoder() {
    }

    /**
     * Détermine si une ligne de texte contient au moins 1 caractère Ansel et réalise les transformations nécessaires
     * (traduit le caractère en unicode et le combine au caractère suivant).
     *
     * @param ligne La ligne à transcrire.
     * @return La ligne sans caractère Ansel.
     */
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
                    i++;                            // Consommation de la lettre
                }

                sb.append(combiningMark);
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

    private static String getCombiningMark(char c) {
        return switch (c) {

            case 0xE1 -> "\u0300"; // Accent grave
            case 0xE2 -> "\u0301"; // Accent aigu
            case 0xE3 -> "\u0302"; // Circonflexe
            case 0xE4 -> "\u0303"; // Tilde
            case 0xE5 -> "\u0304"; // Macron (ā, ē...)
            case 0xE6 -> "\u0306"; // Brève (ă, ğ...)
            case 0xE7 -> "\u0307"; // Point en chef (ż...)
            case 0xE8 -> "\u0308"; // Tréma
            case 0xE9 -> "\u030C"; // Háček / Caron (š, č...)
            case 0xEA -> "\u030A"; // Rond en chef (å)
            case 0xEB -> "\u0327"; // Ogonek (ą, ę...)
            case 0xF0 -> "\u0327"; // Cédille
            default -> null;       // Retourne null si non répertorié
        };
    }
}
