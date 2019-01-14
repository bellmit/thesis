package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.DataType;
import com.nikolaj.thesis.thesis.model.Device;

import com.nikolaj.thesis.thesis.model.DeviceType;
import com.nikolaj.thesis.thesis.model.Role;
import com.nikolaj.thesis.thesis.persistence.DataTypeRepository;
import com.nikolaj.thesis.thesis.persistence.DeviceTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("datatype")
public class DataTypeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataTypeRepository repository;

    public DataTypeController(DataTypeRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ArrayList<DataType> getDataType() {
        return (ArrayList<DataType>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public DataType getDataType(@RequestParam long id) {
        DataType dataType = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return dataType;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteDataType(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateDataType(@PathVariable(value = "id") Long id, @Valid @RequestBody DataType dt) {
        DataType dataType= repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        /*
        deviceType.setModel(dt.getModel());
        deviceType.setProducer(dt.getProducer());
        deviceType.setType(dt.getType());
        repository.save(deviceType);
        */
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public DataType addDataType(@Valid @RequestBody DataType dataType) {
        return repository.save(dataType);
    }

}
