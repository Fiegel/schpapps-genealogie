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

    /**
     * Génère un rapport pour une détection de fichier invalide (vide).
     *
     * @return Un rapport pour un diagnostic de fichier invalide.
     */
    public static RapportDiagnostic genererRapportFichierInvalide() {
        return new RapportDiagnostic(0, SeveriteRapportDiagnostic.SEVERE, "Fichier vide invalide.");
    }

    /**
     * Génère un rapport pour une détection d'un bloc HEADER (HEAD) invalide.
     *
     * @param firstLineNotBlankNumber Le numéro de la 1ère ligne non-vide, où doit se trouver le 0 HEAD.
     * @param headerLine Le modèle de header attendu.
     * @return Un rapport SEVERE pour un diagnostic de header invalide.
     */
    public static RapportDiagnostic genererRapportHeaderInvalide(final int firstLineNotBlankNumber, final String headerLine) {
        return new RapportDiagnostic(firstLineNotBlankNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + firstLineNotBlankNumber + LIGNE_SEPARATEUR
                        + "En-tête invalide. Doit être : " + headerLine);
    }

    /**
     * Génère un rapport pour une détection d'un bloc TRAILER (TRLR) invalide.
     *
     * @param lastLineNotBlankNumber Le numéro de la dernière ligne non-vide, où doit se trouver le 0 TRLR.
     * @param trailerLine Le modèle de trailer attendu.
     * @return Un rapport SEVERE pour un diagnostic de trail invalide.
     */
    public static RapportDiagnostic genererRapportTrailerInvalide(final int lastLineNotBlankNumber, final String trailerLine) {
        return new RapportDiagnostic(lastLineNotBlankNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lastLineNotBlankNumber + LIGNE_SEPARATEUR
                        + "Trailer invalide. Doit être : " + trailerLine);
    }

    /**
     * Génère un rapport de détection d'un identifiant d'entité de niveau 0 en double (ex : 0 @I1@ INDI x 2).
     *
     * @param lineNumber Le numéro de la ligne où se situe le doublon.
     * @param tag Le tag de l'entité (INDI, FAM, OBJE...).
     * @param id L'identifiant concerné.
     * @return Un rapport SEVERE de diagnostic d'un doublon d'identifiant.
     */
    public static RapportDiagnostic genererRapportIdentifiantDoublon(final int lineNumber, final GedcomTag tag, final String id) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Identifiant " + tag.getTagName() + " (" + tag.name() + ") " + id + " déjà utilisé");
    }

    /**
     * Génère un rapport de détection d'un tag inconnu.
     *
     * @param lineNumber Le numéro de la ligne où se situe le tag inconnu.
     * @param tagIconnu Le tag inconnu.
     * @return Un rapport SEVERE de diagnostic pour un tag inconnu.
     */
    public static RapportDiagnostic genererRapportTagInconnu(final int lineNumber, final String tagIconnu) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Tag " + tagIconnu + " inconnu pour une entité de niveau 0");
    }

    /**
     * Génère un rapport de détection de la présence d'un tag qui n'est (actuellement) pas traité à part dans le projet.
     *
     * @param lineNumber Le numéro de la ligne où se situe le tag.
     * @param tagNonTraite Le tag non traité.
     * @return Un rapport INFO de diagnostic pour un tag non traité.
     */
    public static RapportDiagnostic genererRapportTagNonTraite(final int lineNumber, final String tagNonTraite) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.INFO,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Tag " + tagNonTraite + " non traité actuellement");
    }

    /**
     * Génère un rapport de détection d'un tag orphelin (saut de niveau après une entité de niveau 0 par exemple).
     *
     * @param lineNumber Le numéro de la ligne contenant le tag isolé.
     * @param tagOrphelin Le tag isolé.
     * @return Un rapport WARNING de diagnostic pour un tag isolé.
     */
    public static RapportDiagnostic genererRapportTagOrphelin(final int lineNumber, final String tagOrphelin) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Tag " + tagOrphelin + " orphelin (aucun noeud parent trouvé)");
    }

    /**
     * Génère un rapport de détection d'un saut de niveau dans la hiérarchie des entités / attributs (> +1 par rapport à l'entité parente).
     *
     * @param lineNumber Le numéro de la ligne qui a un saut de niveau.
     * @param tag Le tag de la ligne qui a un saut de niveau.
     * @param lineNumberParent Le numéro de la ligne de l'entité parente.
     * @param tagParent Le tag de l'entité parente.
     * @param isParentEntiteRacine True si l'entité parente est de niveau 0 (rattachement par défaut).
     * @return Un rapport WARNING de diagnostic d'un saut de niveau.
     */
    public static RapportDiagnostic genererRapportSautDeNiveau(final int lineNumber, final String tag, final int lineNumberParent, final String tagParent, final boolean isParentEntiteRacine) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Saut de niveau détecté. Attribut " + tag + (isParentEntiteRacine ? " ajouté à l'entité racine " : " ajouté au noeud ") + tagParent + " à la ligne " + lineNumberParent);
    }

    /**
     * Génère un rapport de détection d'une entité qui possède référence inexistante (identifiant inconnu).
     *
     * @param lineNumber Le numéro de la ligne de la référence inconnue.
     * @param tagEntiteSource Le tag de l'entité qui possède la référence inconnue.
     * @param idEntiteSource L'identifiant de l'entité qui possède la référence inconnue.
     * @param tagEntiteCible Le tag de l'entité inconnue.
     * @param idEntiteCible La référence inconnue.
     * @return Un rapport SEVERE de diagnostic pour une référence inconnue.
     */
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

    /**
     * Génère un rapport de détection d'un individu qui déclare être enfant d'une famille, sans apparaître dans les enfants de cette famille (FAMC présent, CHIL absent).
     *
     * @param lineNumber Le numéro de la ligne de déclaration de l'individu.
     * @param idIndividu L'identifiant de l'individu.
     * @param idFamilleEnfant L'identifiant de la famille dans laquelle l'individu aurait dû être enfant.
     * @return Un rapport WARNING de diagnostic d'une référence non réciproque entre individu et enfant d'une famille.
     */
    public static RapportDiagnostic genererRapportReferenceNonReciproqueEnfant(final int lineNumber, final String idIndividu, final String idFamilleEnfant) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "L'individu " + idIndividu + " indique appartenir à la FAMC " + idFamilleEnfant + ", mais n'est pas dans ses CHIL");
    }

    /**
     * Génère un rapport de détection d'un individu qui déclare être parent d'une famille, sans y apparaître en tant que parent 1 (HUSB) ou parent 2 (WIFE).
     *
     * @param lineNumber Le numéro de la ligne de déclaration de l'individu.
     * @param idIndividu L'identifiant de l'individu.
     * @param idFamilleParent L'identifiant de la famille dans laquelle l'individu aurait dû être parent.
     * @return Un rapport WARNING de diagnostic d'une référence non réciproque entre individu et parent d'une famille.
     */
    public static RapportDiagnostic genererRapportReferenceNonReciproqueParent(final int lineNumber, final String idIndividu, final String idFamilleParent) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.WARNING,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "L'individu " + idIndividu + " indique appartenir à la FAMS " + idFamilleParent + ", mais n'est ni HUSB, ni WIFE");
    }

    /**
     * Génère un rapport de détection d'une famille vide.
     *
     * @param lineNumber Le numéro de ligne de déclaration de la famille.
     * @param idFamille L'identifiant de la famille vide.
     * @return Un rapport SEVERE de diagnostic de famille vide.
     */
    public static RapportDiagnostic genererFamilleMalformee(final int lineNumber, final String idFamille) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.SEVERE,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "La famille " + idFamille + " n'a aucun individu");
    }

    /**
     * Génère un rapport de détection d'une extraction automatique d'un objet média interne à une famille ou un individu.
     *
     * @param lineNumber Le numéro de la ligne de l'entité qui possédait l'objet média en interne.
     * @param idObjetInterne Le nouvel identifiant généré de l'objet média.
     * @return Un rapport INFO de diagnostic d'une extraction d'un objet média interne.
     */
    public static RapportDiagnostic genererRapportExtractionObjetInterne(final int lineNumber, final String idObjetInterne) {
        return new RapportDiagnostic(lineNumber,
                SeveriteRapportDiagnostic.INFO,
                PREFIXE_LIGNE + lineNumber + LIGNE_SEPARATEUR
                        + "Objet média interne détecté. Extraction avec nouvel identifiant : " + idObjetInterne);
    }
}
