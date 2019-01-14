package platform.persistence;

import platform.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface RoleRepository extends CrudRepository<Role,Long> {

    ArrayList<Role> findAllByProject_id(long id);
}
