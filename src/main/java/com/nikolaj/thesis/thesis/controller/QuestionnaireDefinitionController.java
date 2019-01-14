package com.nikolaj.thesis.thesis.controller;

import com.nikolaj.thesis.thesis.model.QuestionnaireDefinition;
import com.nikolaj.thesis.thesis.persistence.QuestionnaireDefinitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "questionnairedefinitions")
public class QuestionnaireDefinitionController {
    private QuestionnaireDefinitionRepository repository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public QuestionnaireDefinitionController(QuestionnaireDefinitionRepository repository) {
        this.repository = repository;
    }


    @RequestMapping(value = "/getall", method = { RequestMethod.GET})
    public ArrayList<QuestionnaireDefinition> getQuestionaireDefs() {
        return (ArrayList<QuestionnaireDefinition>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public QuestionnaireDefinition getQuestionaireDef(@RequestParam long id) {
        QuestionnaireDefinition questionnaireDefinition = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return questionnaireDefinition;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteQuestionaireDef(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateQuestionaireDef(@PathVariable(value = "id") Long id,
                              @Valid @RequestBody QuestionnaireDefinition questionnaireDefDetails) {
        QuestionnaireDefinition questionnaire = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        repository.save(questionnaire);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public void addQuestionaireDef(@Valid @RequestBody QuestionnaireDefinition questionnaireDef) {
        logger.info(questionnaireDef.toString());

        repository.save(questionnaireDef);
    }
}
