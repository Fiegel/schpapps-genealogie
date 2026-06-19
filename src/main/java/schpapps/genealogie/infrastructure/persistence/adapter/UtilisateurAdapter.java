package schpapps.genealogie.infrastructure.persistence.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import schpapps.genealogie.domain.entite.Utilisateur;
import schpapps.genealogie.domain.ports.outbound.UtilisateurRepository;
import schpapps.genealogie.infrastructure.persistence.mapper.UtilisateurMapper;
import schpapps.genealogie.infrastructure.persistence.model.UtilisateurModel;
import schpapps.genealogie.infrastructure.persistence.repository.PanacheUtilisateurRepository;

import java.util.Optional;

/**
 * L'adaptateur des utilisateurs.
 */
@ApplicationScoped
public class UtilisateurAdapter implements PanacheUtilisateurRepository, UtilisateurRepository {

    @Override
    @Transactional
    public void save(final Utilisateur utilisateur) {
        final UtilisateurModel utilisateurToSaveModel = UtilisateurMapper.toModel(utilisateur);

        this.persist(utilisateurToSaveModel);
    }

    @Override
    public Optional<Utilisateur> getById(final String id) {
        return this.findByIdOptional(id).map(UtilisateurMapper::toDomain);
    }
}
