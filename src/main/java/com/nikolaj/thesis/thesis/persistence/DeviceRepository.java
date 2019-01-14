package com.nikolaj.thesis.thesis.persistence;
import com.nikolaj.thesis.thesis.model.DPUser;
import com.nikolaj.thesis.thesis.model.Device;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface DeviceRepository extends CrudRepository<Device, Long> {

   Iterable<Device> findAllByDataSubjectdevice(DPUser id);
   Iterable<Device> findAllByDeviceType_ProducerAndDataSubjectdevice(String producer, DPUser id);
   Boolean existsByMuid(String id);
   Device findByMuid(String muid);

}

