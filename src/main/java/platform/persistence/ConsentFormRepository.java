package platform.persistence;

import platform.model.ConsentForm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ConsentFormRepository extends CrudRepository<ConsentForm, Long> {
}
