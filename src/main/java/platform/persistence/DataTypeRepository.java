package platform.persistence;
import platform.model.DataType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DataTypeRepository extends CrudRepository<DataType, Long> {

}

