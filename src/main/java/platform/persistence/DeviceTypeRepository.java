package platform.persistence;


import platform.model.DeviceType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DeviceTypeRepository extends CrudRepository<DeviceType, Long> {



}
