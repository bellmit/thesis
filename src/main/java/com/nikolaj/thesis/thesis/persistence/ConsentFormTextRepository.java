package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.ConsentFormText;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ConsentFormTextRepository extends CrudRepository<ConsentFormText, Long> {
}
