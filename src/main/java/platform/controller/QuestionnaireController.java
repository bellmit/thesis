package platform.controller;


import platform.model.Questionnaire;
import platform.persistence.QuestionnaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "questionnaire")
public class QuestionnaireController {
    private QuestionnaireRepository repository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public QuestionnaireController(QuestionnaireRepository repository) {
        this.repository = repository;
    }


    @RequestMapping(value = "/getall", method = { RequestMethod.GET})
    public ArrayList<Questionnaire> getQuestionaires() {
        return (ArrayList<Questionnaire>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public Questionnaire getQuestionaire(@RequestParam long id) {
        Questionnaire questionnaire = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return questionnaire;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteQuestionaire(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public void updateQuestionaire(@PathVariable(value = "id") Long id, @Valid @RequestBody Questionnaire questionnaireDetails) {
        Questionnaire questionnaire = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        repository.save(questionnaire);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public void addQuestionaire(@Valid @RequestBody Questionnaire questionnaire) {
        logger.info(questionnaire.toString());

        repository.save(questionnaire);
    }
}
