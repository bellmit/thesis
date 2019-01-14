package platform.persistence;

import platform.model.ConsentOptionText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ConsentOptionTextRepository extends CrudRepository<ConsentOptionText, Long> {
}