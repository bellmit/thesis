package platform.controller;

import platform.model.Project;
import platform.persistence.DPUserRepository;
import platform.persistence.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "project")
public class ProjectController {


    private ProjectRepository repository;
    private platform.persistence.DPUserRepository DPUserRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public ProjectController(ProjectRepository repository, DPUserRepository DPUserRepository) {
        this.repository = repository;
        this.DPUserRepository = DPUserRepository;
    }


    @RequestMapping(value = "/getall", method = { RequestMethod.GET})
    public ArrayList<Project> getProjectsge() {
        return (ArrayList<Project>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public Project getProject(@RequestParam long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return project;
    }
    @RequestMapping(value = "/getbyname", method = { RequestMethod.GET})
    public Project getProjectByName(@RequestParam(required = false) String name) {
        Project project = repository.findByName(name);

        return project;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteProject(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateProject(@PathVariable(value = "id") Long id, @Valid @RequestBody Project projectDetails) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
      //  project.setOwner(projectDetails.getOwner());
        repository.save(project);
    }



    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public void addDProject(@Valid @RequestBody Project project) {
        logger.info(project.toString());


         repository.save(project);
    }
}



