package platform.persistence;

import platform.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface QuestionRepository extends CrudRepository<Question, Long> {
     ArrayList<Question> findAllByQuestionnaireDefinitionIdOrderByOrderNoAsc(long questionnaireDefinitionId);
}
