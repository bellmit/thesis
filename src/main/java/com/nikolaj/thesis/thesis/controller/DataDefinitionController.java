package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.DataDefinition;
import com.nikolaj.thesis.thesis.persistence.DataDefinitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("datadefinitions")
public class DataDefinitionController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataDefinitionRepository repository;

    public DataDefinitionController(DataDefinitionRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ArrayList<DataDefinition> getDataDefinition() {
        return (ArrayList<DataDefinition>) repository.findAll();
    }

    @RequestMapping(value = "/getforexperimentdefinition", method = { RequestMethod.GET})
    public ArrayList<DataDefinition> getDataDefinitionByExperimentDefinitionId(@RequestParam long experimentDefId) {
        return repository.findByExperimentDefinitionId(experimentDefId);
    }

    @RequestMapping(value = "/getforexperiment", method = { RequestMethod.GET})
    public ArrayList<DataDefinition> getDataDefinitionByExperimentId(@RequestParam long experimentId) {
        return repository.findByExperimentId(experimentId);
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public DataDefinition getDataDefinition(@RequestParam long id) {
        DataDefinition dataDefinition = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return dataDefinition;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteDataDefinition(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public DataDefinition addDataDefinition(@Valid @RequestBody DataDefinition dataDefinition) {
        boolean alreadyExists = repository.existsByExperimentDefinitionIdAndName(
                dataDefinition.getExperimentDefinitionId(),
                dataDefinition.getName());

        if(alreadyExists) {
            throw new AlreadyExistsException("Experiment with id " + dataDefinition.getExperimentDefinitionId() +
                    " already has a data definition named " + dataDefinition.getName());
        }

        return repository.save(dataDefinition);
    }
}
