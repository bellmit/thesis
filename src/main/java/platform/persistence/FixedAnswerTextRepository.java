package platform.persistence;

import platform.model.FixedAnswerText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface FixedAnswerTextRepository extends CrudRepository<FixedAnswerText, Long> {
}
