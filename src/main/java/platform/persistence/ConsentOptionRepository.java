package platform.persistence;

import platform.model.ConsentOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ConsentOptionRepository extends CrudRepository<ConsentOption, Long> {
}
