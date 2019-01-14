package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.QuestionnaireDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface QuestionnaireDefinitionRepository extends CrudRepository<QuestionnaireDefinition, Long> {
}
