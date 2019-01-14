package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface LanguageRepository extends CrudRepository<Language, Long> {
}
