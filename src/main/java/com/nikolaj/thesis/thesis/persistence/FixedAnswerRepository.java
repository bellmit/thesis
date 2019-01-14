package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.FixedAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface FixedAnswerRepository extends CrudRepository<FixedAnswer, Long> {
}
