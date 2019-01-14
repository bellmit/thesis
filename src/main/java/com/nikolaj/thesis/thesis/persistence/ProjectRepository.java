package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface ProjectRepository extends CrudRepository<Project,Long> {
    Project findByName(String name);
}
