package platform.persistence;

import platform.model.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface AnswerRepository extends CrudRepository<Answer, Long> {
}
