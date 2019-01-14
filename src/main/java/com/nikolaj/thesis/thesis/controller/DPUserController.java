package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.*;

import com.nikolaj.thesis.thesis.persistence.DPUserRepository;
import com.nikolaj.thesis.thesis.persistence.ExperimentDefinitionRepository;
import com.nikolaj.thesis.thesis.persistence.ProjectRepository;
import com.nikolaj.thesis.thesis.persistence.UserTypeRepository;
import com.nikolaj.thesis.thesis.modelResults.ProjectMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Set;

@RestController
@RequestMapping(value = "dpuser")
public class DPUserController {
    private DPUserRepository repository;
    private UserTypeRepository userTypeRepository;
    private ProjectRepository projectRepository;
    private ExperimentDefinitionRepository experimentDefinitionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public DPUserController(DPUserRepository repository, ProjectRepository projectRepository,
                            ExperimentDefinitionRepository experimentDefinitionRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder,UserTypeRepository userTypeRepository) {
        this.repository = repository;
        this.userTypeRepository = userTypeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.experimentDefinitionRepository = experimentDefinitionRepository;
        this.projectRepository = projectRepository;
    }


    @RequestMapping(value = "/getall", method = { RequestMethod.GET})
    public ArrayList<DPUser> getDPUser() {
        return (ArrayList<DPUser>) repository.findAll();
    }

    @RequestMapping(value = "/getallbytypes", method = { RequestMethod.GET})
    public ArrayList<DPUser> getallDPUserByTypes(@RequestParam long type) {
     UserType userType = userTypeRepository.findById(type).orElseThrow(() -> new ResourceNotFoundException("id:" + type));

        ArrayList<DPUser> dpUsers =  repository.findAllByUsertype(userType);
        ArrayList<DPUser> actualDpusers = new ArrayList<>();
        for (DPUser user:dpUsers
             ) {
            actualDpusers.add(new DPUser(user.getUserName(),user.getGivenName(),user.getSurname()));
        }
        return actualDpusers;
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public DPUser getDPUser(@RequestParam long id) {
        DPUser DPUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));

        return DPUser;
    }

    @RequestMapping(value = "/getprojectowner", method = { RequestMethod.GET})
    public ProjectMember getProjectOwnerDPUser(@RequestParam long id) {
        DPUser dpUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));

        ProjectMember projectOwner = new ProjectMember(dpUser, "Project Owner");
        return projectOwner;
    }

    /**
     * Finds users that are members of a project.
     * @param id The projectId.
     * @return An ArrayList of users who are members of a project.
     */
    @RequestMapping(value = "/getprojectmembers", method = { RequestMethod.GET})
    public ArrayList<ProjectMember> getProjectMembers(@RequestParam long id) {
        ArrayList<DPUser> dpUsers = repository.findByProjects_Id(id);

        return transformToProjectMembersList(dpUsers);
    }

    @RequestMapping(value = "/getdatasubjectsnotinexperimentdef", method = { RequestMethod.GET})
    public ArrayList<DPUser> getDataSubjectsNotInExperimentDef(@RequestParam long experimentDefId) {
        return repository.findDataSubjectsWithoutExprDefId(experimentDefId);
    }

    private ArrayList<ProjectMember> transformToProjectMembersList(ArrayList<DPUser> dpUsers) {
        ArrayList<ProjectMember> projectMembers = new ArrayList<>();

        for(DPUser dpUser : dpUsers) {
            Set<Role> roles = dpUser.getRoles();
            String rolesString = "";
            if(roles!= null) {
                int rolesCount = roles.size();
                for(Role role : roles) {
                    rolesString += role.getName();
                    if(rolesCount > 1) {
                        rolesString += ", ";
                    }

                    rolesCount--;
                }
            }

            projectMembers.add(new ProjectMember(dpUser, rolesString));
        }

        return projectMembers;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteDPUser(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateDPUser(@PathVariable(value = "id") Long id, @Valid @RequestBody DPUser DPUserDetails) {
        DPUser DPUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));

        repository.save(DPUser);
    }

    @RequestMapping(value = "/updatewithproject", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateDPUserWithProject(@RequestBody DPUser dpUser) {
        DPUser dPUser = repository.findByGivenName(dpUser.getGivenName());

        Project project = new Project();
      //  project = projectRepository.findById() .orElseThrow(() -> new ResourceNotFoundException("id:" + projectId));
      //  ArrayList<Project> projects = (ArrayList<Project>) dPUser.getProjects();
       //      dPUser.setProjects(projects);

        repository.save(dPUser);
    }

    @RequestMapping(value = "/signup", method = {RequestMethod.POST})
    public void SignUpPDPUser(@RequestBody DPUser dPUser) {
        dPUser.setPassword(bCryptPasswordEncoder.encode(dPUser.getPassword()));
        UserType userType = new UserType();
/*
        long id = dPUser.getUsertype().getId();

       userType = userTypeRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("id:" + dPUser.getUsertype().getId()));
      dPUser.setUsertype(userType);
        logger.info(dPUser.getGivenName());
        */
        repository.save(dPUser);
    }
}

