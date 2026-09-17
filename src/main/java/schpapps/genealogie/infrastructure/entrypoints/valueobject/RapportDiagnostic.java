package schpapps.genealogie.infrastructure.entrypoints.valueobject;

/**
 * Un rapport d'erreur, suite à une vérification technique des données d'un fichier Gedcom.
 *
 * @param lineNumber Le numéro de la ligne avec un problème.
 * @param severite La sévérité de l'erreur.
 * @param message Le message explicatif.
 */
public record RapportDiagnostic(int lineNumber, SeveriteRapportDiagnostic severite, String message) {
}
