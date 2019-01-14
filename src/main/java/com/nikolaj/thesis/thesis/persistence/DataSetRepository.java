package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.DataSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface DataSetRepository extends CrudRepository<DataSet, Long> {
}
