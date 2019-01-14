package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.Phase;
import com.nikolaj.thesis.thesis.persistence.PhaseRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("phases")
public class PhaseController {

    private PhaseRepository repository;

    public PhaseController(PhaseRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.POST, RequestMethod.GET})
    public ArrayList<Phase> getPhases() {
        return (ArrayList<Phase>) repository.findAll();
    }

    @RequestMapping(value = "/getallbyid", method = {RequestMethod.GET})
    public ArrayList<Phase> getPhasesById(@RequestParam long id) {
        return  repository.findAllByProjectId(id);
    }
    @RequestMapping(value = "/getone", method = {RequestMethod.POST, RequestMethod.GET})
    public Phase getPhase(@RequestParam long id) {
        Phase phase= repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return phase;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deletePhase(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updatePhase(@PathVariable(value = "id") Long id, @Valid @RequestBody Phase phaseDetails) {
        Phase phase = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));

        phase.setName(phaseDetails.getName());
        phase.setDescription(phaseDetails.getDescription());
        phase.setStartdate(phaseDetails.getStartdate());
        phase.setEnddate(phaseDetails.getEnddate());
        phase.setProjectId(phaseDetails.getProjectId());

        repository.save(phase);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public Phase addPhase(@Valid @RequestBody Phase phase) {
        return repository.save(phase);
    }

}
