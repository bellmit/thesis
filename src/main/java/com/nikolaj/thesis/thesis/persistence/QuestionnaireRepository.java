package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.Questionnaire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface QuestionnaireRepository extends CrudRepository<Questionnaire, Long> {
    Questionnaire findFirstByQuestionnaireDefinitionIdOrderByTimestampDesc(long questionnaireDefinitionId);
    ArrayList<Questionnaire> findByExperimentId(long experimentId);
}
