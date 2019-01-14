package platform.persistence;

import platform.model.FixedAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface FixedAnswerRepository extends CrudRepository<FixedAnswer, Long> {
}
