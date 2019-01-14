package platform.persistence;

import platform.model.QuestionnaireDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface QuestionnaireDefinitionRepository extends CrudRepository<QuestionnaireDefinition, Long> {
}
