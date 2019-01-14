package platform.controller;

import platform.model.ExperimentDefinition;
import platform.persistence.ExperimentDefinitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("experimentdefinitions")
public class ExperimentDefinitionController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ExperimentDefinitionRepository repository;

    public ExperimentDefinitionController(ExperimentDefinitionRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ArrayList<ExperimentDefinition> getExperimentDefinition() {
        return (ArrayList<ExperimentDefinition>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public ExperimentDefinition getExperimentDefinition(@RequestParam long id) {
        ExperimentDefinition experimentDefinition = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return experimentDefinition;
    }

    @RequestMapping(value = "/getforproject", method = { RequestMethod.GET})
    public ArrayList<ExperimentDefinition> getExperimentDefinitionsOfProject(@RequestParam long id) {
        ArrayList<ExperimentDefinition> experimentDefinitions = repository.findByProjectId(id);

        return experimentDefinitions;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteExperimentDefinition(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public ExperimentDefinition addExperimentDefinition(@Valid @RequestBody ExperimentDefinition experimentDefinition) {
        return repository.save(experimentDefinition);
    }
}
