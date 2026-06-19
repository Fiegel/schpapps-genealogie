package schpapps.genealogie.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Le modèle des utilisateurs.
 */
@Entity
@Table(name = "UTILISATEUR")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class UtilisateurModel {

    @Column(name = "UTIL_ID")
    @EqualsAndHashCode.Include
    @Id
    private String id;

    @Column(name = "UTIL_NOM")
    private String nom;

    @Column(name = "UTIL_PRENOM")
    private String prenom;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(final String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(final String prenom) {
        this.prenom = prenom;
    }
}
