package platform.persistence;

import platform.modelResults.DatasetFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.ArrayList;

@RepositoryRestController
public interface DatasetFileOverviewRepository extends CrudRepository<DatasetFile, Long> {
    @Query(value = "SELECT * FROM dataset_file_overview WHERE experiment_id = ?1", nativeQuery = true)
    ArrayList<DatasetFile> getDatasetFileOverview(long experimentId);
}
