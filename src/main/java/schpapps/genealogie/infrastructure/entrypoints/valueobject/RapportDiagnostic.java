package schpapps.genealogie.infrastructure.entrypoints.valueobject;

/**
 * Un rapport d'erreur, suite à une vérification technique des données d'un fichier Gedcom.
 *
 * @param lineNumber Le numéro de la ligne avec un problème.
 * @param severite La sévérité de l'erreur.
 * @param message Le message explicatif.
 */
public record RapportDiagnostic(int lineNumber, SeveriteRapportDiagnostic severite, String message) {

    private static final String PREFIXE_LIGNE = "Ligne ";
    private static final String LIGNE_SEPARATEUR = " : ";
    private static final String MESSAGE_IDENTIFIANT = "Identifiant ";
    private static final String MESSAGE_TAG = "Tag ";
    private static final String SUFFIXE_DOUBLON = " déjà utilisé";

    public static RapportDiagnostic genererRapportFichierInvalide() {
        return new RapportDiagnostic(0, SeveriteRapportDiagnostic.SEVERE, "Fichier vide invalide.");
    }

    public static RapportDiagnostic genererRapportHeaderInvalide(final int firstLineNotBlankNumber, final String headerLine) {
        return new RapportDiagnostic(firstLineNotBlankNumber, SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + firstLineNotBlankNumber + LIGNE_SEPARATEUR
                        + "En-tête invalide. Doit être : " + headerLine);
    }

    public static RapportDiagnostic genererRapportTrailerInvalide(final int lastLineNotBlankNumber, final String trailerLine) {
        return new RapportDiagnostic(lastLineNotBlankNumber, SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lastLineNotBlankNumber + LIGNE_SEPARATEUR
                        + "Trailer invalide. Doit être : " + trailerLine);
    }

    public static RapportDiagnostic genererRapportIdentifiantDoublon(final int lineNumber, final GedcomTag tag, final String id) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Identifiant " + tag.getTagName() + " (" + tag.name() + ") " + id + " déjà utilisé");
    }

    public static RapportDiagnostic genererRapportTagInconnu(final int lineNumber, final String tagIconnu) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Tag " + tagIconnu + " inconnu pour une entité de niveau 0");
    }

    public static RapportDiagnostic genererRapportTagNonTraite(final int lineNumber, final String tagNonTraite) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.INFO,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Tag " + tagNonTraite + " non traité actuellement");
    }

    public static RapportDiagnostic genererRapportTagOrphelin(final int lineNumber, final String tagOrphelin) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Tag " + tagOrphelin + " orphelin (aucun noeud parent trouvé)");
    }

    public static RapportDiagnostic genererRapportSautDeNiveau(final int lineNumber, final String tag, final int lineNumberParent, final String tagParent, final boolean isParentEntiteRacine) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Saut de niveau détecté. Attribut " + tag + (isParentEntiteRacine ? " ajouté à l'entité racine " : " ajouté au noeud ") + tagParent + " à la ligne " + lineNumberParent);
    }

    public static RapportDiagnostic genererReferenceInexistante(final int lineNumber,
            final String tagEntiteSource,
            final String idEntiteSource,
            final String tagEntiteCible,
            final String idEntiteCible) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + tagEntiteSource + " " + idEntiteSource + " référence un " + tagEntiteCible + " inexistant : " + idEntiteCible);
    }

    public static RapportDiagnostic genererRapportReferenceNonReciproqueEnfant(final int lineNumber, final String idIndividu, final String idFamilleEnfant) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "L'individu " + idIndividu + " indique appartenir à la FAMC " + idFamilleEnfant + ", mais n'est pas dans ses CHIL");
    }

    public static RapportDiagnostic genererRapportReferenceNonReciproqueParent(final int lineNumber, final String idIndividu, final String idFamilleParent) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "L'individu " + idIndividu + " indique appartenir à la FAMS " + idFamilleParent + ", mais n'est ni HUSB, ni WIFE");
    }

    public static RapportDiagnostic genererFamilleMalformee(final int lineNumber, final String idFamille) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "La famille " + idFamille + " n'a aucun individu");
    }

    public static RapportDiagnostic genererRapportExtractionObjetInterne(final int lineNumber, final String idObjetInterne) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.INFO,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Objet média interne détecté. Extraction avec nouvel identifiant : " + idObjetInterne);
    }
}
