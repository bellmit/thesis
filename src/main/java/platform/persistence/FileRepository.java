package platform.persistence;

import platform.model.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface FileRepository extends CrudRepository<File, Long> {
//    @Query(value = "SELECT * FROM dataset_file_overview WHERE experiment_id = ?1", nativeQuery = true)
//    ArrayList<DatasetFile> getDatasetFileOverview(long experimentId);
}
