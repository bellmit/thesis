package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.ConsentForm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ConsentFormRepository extends CrudRepository<ConsentForm, Long> {
}
