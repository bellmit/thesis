package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.Device;
import com.nikolaj.thesis.thesis.model.Project;
import com.nikolaj.thesis.thesis.model.Role;
import com.nikolaj.thesis.thesis.persistence.DeviceRepository;
import com.nikolaj.thesis.thesis.persistence.RoleRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("roles")
public class RoleController {

    private RoleRepository repository;

    public RoleController(RoleRepository repository) {
        this.repository = repository;
    }


    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ArrayList<Role> getRoles() {
        return (ArrayList<Role>) repository.findAll();
    }

    @RequestMapping(value = "/getallbyid", method = {RequestMethod.GET})
    public ArrayList<Role> getRolesById(@RequestParam long id) {
        return  repository.findAllByProject_id(id);
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public Role getRole(@RequestParam long id) {
        Role role= repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return role;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteProject(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateRole(@PathVariable(value = "id") Long id, @Valid @RequestBody Role roleDetails) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());
        role.setProject(roleDetails.getProject());
        repository.save(role);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public Role addRole(@Valid @RequestBody Role role) {
        return repository.save(role);
    }

}
