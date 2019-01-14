package platform.persistence;

import platform.model.ConsentFormText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ConsentFormTextRepository extends CrudRepository<ConsentFormText, Long> {
}
