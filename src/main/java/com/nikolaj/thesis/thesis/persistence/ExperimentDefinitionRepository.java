package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.ExperimentDefinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface ExperimentDefinitionRepository extends CrudRepository<ExperimentDefinition, Long> {
    ArrayList<ExperimentDefinition> findByProjectId(long projectId);
}
