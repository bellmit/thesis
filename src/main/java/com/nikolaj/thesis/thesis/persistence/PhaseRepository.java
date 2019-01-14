package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.Phase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface PhaseRepository extends CrudRepository<Phase,Long> {
    ArrayList<Phase> findAllByProjectId(long id);
}
