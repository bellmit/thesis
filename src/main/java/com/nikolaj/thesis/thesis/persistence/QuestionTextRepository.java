package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.QuestionText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface QuestionTextRepository extends CrudRepository<QuestionText, Long> {
}