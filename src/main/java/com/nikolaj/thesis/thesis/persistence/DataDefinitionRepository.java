package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.DataDefinition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface DataDefinitionRepository extends CrudRepository<DataDefinition, Long> {
    boolean existsByExperimentDefinitionIdAndName(long experimentDefinitionId, String name);
    ArrayList<DataDefinition> findByExperimentDefinitionId(long experimentDefinitionId);

    @Query(value = "SELECT * FROM data_def WHERE experiment_def_id IN " +
            "(SELECT experiment_def_id FROM experiment WHERE experiment_id = ?1)", nativeQuery = true)
    ArrayList<DataDefinition> findByExperimentId(long experimentId);
}
