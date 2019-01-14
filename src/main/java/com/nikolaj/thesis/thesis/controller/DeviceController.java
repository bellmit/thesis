package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.DPUser;
import com.nikolaj.thesis.thesis.model.Device;
import com.nikolaj.thesis.thesis.persistence.DeviceRepository;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("devices")
public class DeviceController {
    private DeviceRepository repository;

    public DeviceController(DeviceRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.POST, RequestMethod.GET})
    public ArrayList<Device> getDevices() {
        return (ArrayList<Device>) repository.findAll();
    }

    @RequestMapping(value = "/getallbydatasubjectid", method = {RequestMethod.POST, RequestMethod.GET})
    public ArrayList<Device> getDevicesByDatasubjectsId(@RequestParam DPUser id) {
        return (ArrayList<Device>) repository.findAllByDataSubjectdevice(id);
    }

    @RequestMapping(value = "/getone", method = {RequestMethod.POST, RequestMethod.GET})
    public Device getDevice(@RequestParam long id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return device;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public void deleteDevice(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateDevice( @Valid @RequestBody Device deviceDetails ) {

        Device device = repository.findByMuid(deviceDetails.getMuid());

        device.setName(deviceDetails.getName());
        device.setBatterylevel(deviceDetails.getBatterylevel());
        device.setDeviceType(deviceDetails.getDeviceType());

        device.setLastSyncTime(deviceDetails.getLastSyncTime());
        device.setMuid(deviceDetails.getMuid());

         repository.save(device);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public Device addDevice(@Valid @RequestBody Device device){
        return repository.save(device);
    }
}
