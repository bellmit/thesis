package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.UserType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface UserTypeRepository extends CrudRepository<UserType,Long> {
    ArrayList<UserType> findByType(UserType type);
}
