package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.Device;

import com.nikolaj.thesis.thesis.model.DeviceType;
import com.nikolaj.thesis.thesis.model.Role;
import com.nikolaj.thesis.thesis.persistence.DeviceTypeRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("devicetype")
public class DeviceTypeController {

    private DeviceTypeRepository repository;

    public DeviceTypeController(DeviceTypeRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ArrayList<DeviceType> getDeviceType() {
        return (ArrayList<DeviceType>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public DeviceType getDeviceType(@RequestParam long id) {
        DeviceType deviceType= repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return deviceType;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteDeviceType(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateDeviceType(@PathVariable(value = "id") Long id, @Valid @RequestBody DeviceType dt) {
        DeviceType deviceType = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));

        deviceType.setModel(dt.getModel());
        deviceType.setProducer(dt.getProducer());
        deviceType.setType(dt.getType());
        repository.save(deviceType);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public DeviceType addDeviceType(@Valid @RequestBody DeviceType deviceType) {
        return repository.save(deviceType);
    }

}
