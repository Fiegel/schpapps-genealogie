package schpapps.genealogie.infrastructure.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import schpapps.genealogie.infrastructure.persistence.model.UtilisateurModel;

/**
 * Le repository JPA des utilisateurs.
 */
public interface PanacheUtilisateurRepository extends PanacheRepositoryBase<UtilisateurModel, String> {
}
