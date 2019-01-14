package com.nikolaj.thesis.thesis.controller;


import com.nikolaj.thesis.thesis.model.Question;
import com.nikolaj.thesis.thesis.persistence.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "questionaire")
public class QuestionController {
    private QuestionRepository repository;
//    private QuestionaireRepository questionaireRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public QuestionController(QuestionRepository repository) {
        this.repository = repository;
    }


//    @RequestMapping(value = "/getall", method = { RequestMethod.GET})
//    public ArrayList<Question> getQuestionaires() {
//        return (ArrayList<Question>) repository.findAll();
//    }
//
//    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
//    public Question getQuestionnaire(@RequestParam long id) {
//        Question questionaire = repository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
//        return questionaire;
//    }
//
//    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
//    public void deleteProject(@RequestParam long id) {
//        repository.deleteById(id);
//    }
//
//    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
//    public void updateProject(@PathVariable(value = "id") Long id, @Valid @RequestBody Question questionDetails) {
//        Question questionaire = repository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
////        questionaire.setName(questionDetails.getName());
////        questionaire.setDescription(questionDetails.getDescription());
//        repository.save(questionaire);
//    }
//
//    @RequestMapping(value = "/add", method = {RequestMethod.POST})
//    public void addDProject(@Valid @RequestBody Question project) {
//        logger.info(project.toString());
//
//        repository.save(project);
//    }
}
