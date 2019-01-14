package com.nikolaj.thesis.thesis.persistence;

import com.nikolaj.thesis.thesis.model.DPUser;
import com.nikolaj.thesis.thesis.model.UserType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Transactional
@RepositoryRestController
public interface DPUserRepository extends CrudRepository<DPUser, Long> {
    DPUser findByUserName(String userName);
    ArrayList<DPUser> findAllByUsertype(UserType type);
    DPUser findByGivenName(String givenName);

//    @Query(value = "SELECT givenName, surname, array_agg(role.name) from " +
//            "dpuser NATURAL JOIN project_member_project NATURAL JOIN role_dpuser NATURAL JOIN role " +
//            "WHERE project_id = 1 GROUP BY dpuser.dpuser_id", nativeQuery = true)
    ArrayList<DPUser> findByProjects_Id(long id);

    @Query(value = "SELECT * FROM dpuser WHERE dpuser_id NOT IN " +
            "(SELECT dpuser_id FROM experiment WHERE experiment_def_id = ?1) AND " +
            "dpuser_id NOT IN (SELECT DISTINCT owner FROM experiment_def JOIN project " +
            "ON project.project_id = experiment_def.project_id WHERE experiment_def_id = ?1)",
            nativeQuery = true)
//    @Query(value = "SELECT * FROM ", nativeQuery = true)
    ArrayList<DPUser> findDataSubjectsWithoutExprDefId(long experimentDefid);
}
