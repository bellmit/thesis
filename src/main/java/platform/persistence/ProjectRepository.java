package platform.persistence;

import platform.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ProjectRepository extends CrudRepository<Project,Long> {
    Project findByName(String name);
}
