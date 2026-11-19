package schpapps.genealogie.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Le modèle des utilisateurs.
 */
@Entity
@Table(name = "P_UTILISATEUR")
public class UtilisateurModel {

    @Column(name = "UTIL_ID")
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

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final UtilisateurModel that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UtilisateurModel {"
                + "id='" + id + '\''
                + ", nom='" + nom + '\''
                + ", prenom='" + prenom + '\''
                + '}';
    }
}
