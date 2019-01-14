package platform.persistence;

import platform.model.Phase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface PhaseRepository extends CrudRepository<Phase,Long> {
    ArrayList<Phase> findAllByProjectId(long id);
}
