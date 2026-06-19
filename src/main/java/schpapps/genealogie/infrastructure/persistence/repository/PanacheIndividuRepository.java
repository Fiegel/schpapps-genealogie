package schpapps.genealogie.infrastructure.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import schpapps.genealogie.infrastructure.persistence.model.IndividuModel;

/**
 * Le repository JPA des individus.
 */
public interface PanacheIndividuRepository extends PanacheRepositoryBase<IndividuModel, String> {
}
