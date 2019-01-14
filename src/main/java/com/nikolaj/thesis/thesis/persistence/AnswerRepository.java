package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface AnswerRepository extends CrudRepository<Answer, Long> {
}
