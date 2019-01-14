package platform.controller;

import platform.model.DataSet;
import platform.persistence.DataSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("datasets")
public class DataSetController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataSetRepository repository;

    public DataSetController(DataSetRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ArrayList<DataSet> getDataSet() {
        return (ArrayList<DataSet>) repository.findAll();
    }

    @RequestMapping(value = "/getone", method = { RequestMethod.GET})
    public DataSet getDataSet(@RequestParam long id) {
        DataSet dataSet = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id:" + id));
        return dataSet;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteDataSet(@RequestParam long id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public DataSet addDataSet(@Valid @RequestBody DataSet dataSet) {
        return repository.save(dataSet);
    }
}
