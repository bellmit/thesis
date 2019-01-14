package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.Consent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ConsentRepository extends CrudRepository<Consent, Long> {
}
