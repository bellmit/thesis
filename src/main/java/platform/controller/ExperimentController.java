package platform.controller;

import platform.model.Experiment;
import platform.persistence.ExperimentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("experiments")
public class ExperimentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ExperimentRepository repository;

    public ExperimentController(ExperimentRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ArrayList<Experiment> getExperiment() {
        return (ArrayList<Experiment>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public Experiment getExperiment(@RequestParam long id) {
        Experiment experiment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return experiment;
    }

    @RequestMapping(value = "/getforexperimentdefinition", method = { RequestMethod.GET })
    public ArrayList<Experiment> getDataDefinitionByExperimentDefinitionId(@RequestParam long experimentDefId) {
        return repository.findByExperimentDefinitionId(experimentDefId);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteExperiment(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public Experiment addExperiment(@Valid @RequestBody Experiment experiment) {
        return repository.save(experiment);
    }
}
