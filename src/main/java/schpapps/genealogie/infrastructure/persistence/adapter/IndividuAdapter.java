package schpapps.genealogie.infrastructure.persistence.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import schpapps.genealogie.domain.entite.Individu;
import schpapps.genealogie.domain.ports.outbound.IndividuRepository;
import schpapps.genealogie.infrastructure.persistence.mapper.IndividuMapper;
import schpapps.genealogie.infrastructure.persistence.model.IndividuModel;
import schpapps.genealogie.infrastructure.persistence.repository.PanacheIndividuRepository;

import java.util.Optional;

/**
 * L'adaptateur des individus.
 */
@ApplicationScoped
public class IndividuAdapter implements PanacheIndividuRepository, IndividuRepository {

    @Override
    @Transactional
    public void save(final Individu individu) {
        final IndividuModel individuToSaveModel = IndividuMapper.toModel(individu);

        this.persist(individuToSaveModel);
    }

    @Override
    public Optional<Individu> getById(final String id) {
        return this.findByIdOptional(id).map(IndividuMapper::toDomain);
    }
}
