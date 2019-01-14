package platform.controller;

import platform.model.*;
import platform.model.*;
import platform.model.File;
import platform.modelResults.DatasetFile;
import platform.persistence.*;
import platform.storage.StorageException;
import platform.storage.StorageFileNotFoundException;
import platform.storage.StorageService;
import platform.util.InfluxDBConfig;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import platform.persistence.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("data")
public class DataController {
    private static final int MAX_BATCH_SIZE = 5000;
    private final StorageService storageService;
    private FileRepository fileRepository;
    private DataDefinitionRepository dataDefinitionRepository;
    private DataPointsRepository dataPointsRepository;
    private DataSetRepository dataSetRepository;
    private DatasetFileOverviewRepository datasetFileOverviewRepository;
    private DataTypeRepository dataTypeRepository;
    private ExperimentRepository experimentRepository;
    private QuestionnaireDefinitionRepository questionnaireDefinitionRepository;
    private QuestionnaireRepository questionnaireRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private enum DataBaseToStoreIn { INFLUX, POSTGRES }
    private final long TIME_SERIES = 1;
    private final long QUESTIONNAIRE = 2;
    private final long FILE = 3;

    @Autowired
    public DataController(FileRepository fileRepository, DataDefinitionRepository dataDefinitionRepository,
                          DataPointsRepository dataPointsRepository, DataSetRepository dataSetRepository,
                          DatasetFileOverviewRepository datasetFileOverviewRepository,
                          DataTypeRepository dataTypeRepository,
                          ExperimentRepository experimentRepository,
                          QuestionnaireDefinitionRepository questionnaireDefinitionRepository,
                          QuestionnaireRepository questionnaireRepository, QuestionRepository questionRepository,
                          AnswerRepository answerRepository, StorageService storageService) {
        this.fileRepository = fileRepository;
        this.dataDefinitionRepository = dataDefinitionRepository;
        this.dataPointsRepository = dataPointsRepository;
        this.dataSetRepository = dataSetRepository;
        this.datasetFileOverviewRepository = datasetFileOverviewRepository;
        this.dataTypeRepository = dataTypeRepository;
        this.experimentRepository = experimentRepository;
        this.questionnaireDefinitionRepository = questionnaireDefinitionRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.storageService = storageService;
    }

    private String dbName = "thesisDB";

    //set up influxDB
    InfluxDBConfig influxDBConfig = new InfluxDBConfig();
    InfluxDB influxDB = influxDBConfig.connectToInflux();
    BatchPoints batchPoints = BatchPoints.database("thesisDB")
            .build();


    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public List<Optional<Object[]>> getdata(@RequestParam String userName, String type){

        Query query = new Query("SELECT * FROM "+userName, dbName);
        QueryResult results = influxDB.query(query);
/*
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * FROM $username")
                .forDatabase(dbName)
                .bind("username", userName)
               //  .bind("type", type)
                .create();
        QueryResult results = influxDB.query(query);
*/
        QueryResult.Series series = results.getResults().get(0).getSeries().get(0);
        Object[] time1 = series.getValues().toArray();
        List<Optional<Object[]>> results1 = Arrays.asList(Optional.ofNullable(time1));
       // System.out.println(time1);
    //    System.out.println(results);
      //  InfluxDBResultMapper resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
       // List<JsonNode> list = resultMapper.toPOJO(results, JsonNode.class);

        return results1 ;
    }

    @RequestMapping(value = "/getabstractexperimentdata", method = {RequestMethod.GET})
    public ArrayList<DatasetFile> getAbstractExperimentData(@RequestParam long experimentId) {
        ArrayList<DatasetFile> files = datasetFileOverviewRepository.getDatasetFileOverview(experimentId);

        ArrayList<DataPoints> dataPoints = dataPointsRepository.findByExperimentId(experimentId);

        ArrayList<Questionnaire> questionnaires = questionnaireRepository.findByExperimentId(experimentId);

        ArrayList<DatasetFile> results = new ArrayList<>();
        return results;
    }

//    /**
//     * This method stores the passed file in a subdirectory (experimentId/dataSetId/) in the file system.
//     * The file is named according to the version and name of the related data definition.
//     * @param dataDefId
//     * @param file
//     * @param redirectAttributes
//     * @return
//     */
//    @RequestMapping(value = "/addFile", method = {RequestMethod.POST})
//    public String handleFileUpload(@RequestParam("dataDefinitionId") long dataDefId,
//                                   @RequestParam("experimentId") long experimentId,
//                                   @RequestParam("file") MultipartFile file,
//                                   RedirectAttributes redirectAttributes) {
//
//        DataDefinition dataDef = dataDefinitionRepository.findById(dataDefId).orElseThrow(() ->
//                new ResourceNotFoundException("dataDefId:" + dataDefId));
//
//        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() ->
//                new ResourceNotFoundException("experimentId for dataSetId: " + experimentId + " not found"));
//
//        try {
//            File fileData = storageService.storeProjectFile(experiment.getId(), dataDef, file);
//
//            fileData.setDataDefinitionId(dataDef.getId());
//            fileData.setExperimentId(experiment.getId());
//
//            fileRepository.save(fileData);
//
//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded " + file.getOriginalFilename() + "!");
//
//            return "Uploaded " + file.getOriginalFilename();
//        }
//        catch (StorageException e) {
//            return "Failed to upload " + file.getOriginalFilename();
//        }
//    }

    /**
     * Parses a string of comma separated data definition ids.
     * @param assignedDataDefIdStrings
     * @return
     */
    private long[] parseDataDefIds(String[] assignedDataDefIdStrings) {
        long[] parsedLongs = new long[assignedDataDefIdStrings.length];

        for(int i = 0; i < assignedDataDefIdStrings.length; i++) {
            try {
                parsedLongs[i] = Long.parseLong(assignedDataDefIdStrings[i]);
            }
            catch(NumberFormatException e) {
                return null;
            }
        }

        return parsedLongs;
    }

    /**
     * Retrieves the data type of the passed data definition ID.
     * @param dataDefId
     * @return
     */
    private DataType getDataDefDataType(long dataDefId) {
        Optional<DataDefinition> dataDefRes = dataDefinitionRepository.findById(dataDefId);

        if(dataDefRes.isPresent()) {
            DataDefinition dataDef = dataDefRes.get();

            Optional<DataType> dataTypeRes = dataTypeRepository.findById(dataDef.getDataTypeId());

            if(dataTypeRes.isPresent()) {
                return dataTypeRes.get();
            }
        }

        return null;
    }

    /**
     *
     * @param experimentId
     * @param files
     * @param assignedDataDefIds
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/addData/{experimentId}", method = {RequestMethod.POST})
    public String handleFileUpload(@PathVariable(name = "experimentId") long experimentId,
                                   @RequestParam(name = "files") MultipartFile[] files,
                                   @RequestParam("assignedDataDefinitionIds") String assignedDataDefIds,
                                   RedirectAttributes redirectAttributes) {
        String[] dataDefIdStrings = assignedDataDefIds.split(",");

        long[] parsedDataDefIds = parseDataDefIds(dataDefIdStrings);

        if(parsedDataDefIds == null || dataDefIdStrings.length != files.length) {
            throw new InternalError("The number of assigned data definitions does not match the number of files.");
        }

        Optional<Experiment> experimentRes = experimentRepository.findById(experimentId);

        if(experimentRes.isPresent()) {
            Experiment experiment = experimentRes.get();

            for (int i = 0; i < files.length; i++) {

                Optional<DataDefinition> dataDefRes = dataDefinitionRepository.findById(parsedDataDefIds[i]);

                if (dataDefRes.isPresent()) {
                    DataDefinition dataDef = dataDefRes.get();

                    Optional<DataType> dataTypeRes = dataTypeRepository.findById(dataDef.getDataTypeId());

                    if (dataTypeRes.isPresent()) {
                        DataType dataType = dataTypeRes.get();

                        long dataTypeId = dataType.getId();

                        if (dataTypeId == TIME_SERIES) {
                            saveDataPoints(dataDef, experimentId, files[i], redirectAttributes);
                        }
                        else if (dataTypeId == QUESTIONNAIRE) {
//                        saveQuestionnaire(dataDef)
                        }
                        else if (dataTypeId == FILE) {
                            saveFile(dataDef, experiment, files[i], redirectAttributes);
                        }
                    }
                }
            }
        }
        else {
            throw new ResourceNotFoundException("The passed experiment id, " + experimentId + ", does not exist.");
        }
//        System.out.println("OK");
        return "OK";
    }

    /**
     * Invoked to parse and store data in the passed file into influxDB.
     * @param dataDef
     * @param experimentId
     * @param file
     * @param redirectAttributes
     * @return
     */
    private String saveDataPoints(DataDefinition dataDef, long experimentId, MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        return parseFileAndStoreData(file, dataDef.getColumnDelimiter(), (int) dataDef.getLinesToSkip(), experimentId,
                dataDef, redirectAttributes, DataBaseToStoreIn.INFLUX, null, null,
                (int) dataDef.getTimestampColumn(), null, dataDef.getTimestampUnit());
    }

    /**
     * Invoked to store the passed file in a file system
     * @param dataDef
     * @param experiment
     * @param file
     * @param redirectAttributes
     * @return
     */
    public String saveFile(DataDefinition dataDef, Experiment experiment, MultipartFile file,
                           RedirectAttributes redirectAttributes) {
        try {
            File fileData = storageService.storeProjectFile(experiment.getId(), dataDef, file);

            fileData.setDataDefinitionId(dataDef.getId());
            fileData.setExperimentId(experiment.getId());

            fileRepository.save(fileData);

            return "Uploaded " + file.getOriginalFilename();
        }
        catch (StorageException e) {
            return "Failed to upload " + file.getOriginalFilename();
        }
    }

//    private void saveQuestionnaire(DataDefinition dataDef) {
//        // Checks whether the questionnaireDefinitionId exists.
//        // If not, an exception is thrown.
//        QuestionnaireDefinition questionnaireDef = questionnaireDefinitionRepository.findById(questionnaireDefId)
//                .orElseThrow(() -> new ResourceNotFoundException("questionnaireDefId:" + questionnaireDefId));
//
//        experimentRepository.findById(experimentId).orElseThrow(() ->
//                new ResourceNotFoundException("experimentId: " + experimentId));
//
//        return parseFileAndStoreData(file, columnSeparator, linesToSkip, experimentId, dataDef,
//                redirectAttributes, DataBaseToStoreIn.POSTGRES, questionnaireDef, cellItemsSeparator,
//                timestampColumn, timestampFormat, null);
//    }

//    @RequestMapping(value = "/addQuestionnaireAnswers", method = {RequestMethod.POST})
//    public String handleQuestionnaireAnswersUpload(@RequestParam("dataDefinitionId") long dataDefId,
//                                                   @RequestParam("questionnaireDefId") long questionnaireDefId,
//                                                   @RequestParam("experimentId") long experimentId,
//                                                   @RequestParam("file") MultipartFile file,
//                                                   @RequestParam("linesToSkip") int linesToSkip,
//                                                   @RequestParam("columnSeparator") String columnSeparator,
//                                                   @RequestParam("cellItemsSeparator") String cellItemsSeparator,
//                                                   @RequestParam("timestampColumn") int timestampColumn,
//                                                   @RequestParam("timestampFormat") String timestampFormat,
//                                                   RedirectAttributes redirectAttributes) {
//        DataDefinition dataDef = dataDefinitionRepository.findById(dataDefId).orElseThrow(() ->
//                new ResourceNotFoundException("dataDefId:" + dataDefId));
//
//        // Checks whether the questionnaireDefinitionId exists.
//        // If not, an exception is thrown.
//        QuestionnaireDefinition questionnaireDef = questionnaireDefinitionRepository.findById(questionnaireDefId)
//                .orElseThrow(() -> new ResourceNotFoundException("questionnaireDefId:" + questionnaireDefId));
//
//        experimentRepository.findById(experimentId).orElseThrow(() ->
//                new ResourceNotFoundException("experimentId: " + experimentId));
//
//        return parseFileAndStoreData(file, columnSeparator, linesToSkip, experimentId, dataDef,
//                redirectAttributes, DataBaseToStoreIn.POSTGRES, questionnaireDef, cellItemsSeparator,
//                timestampColumn, timestampFormat, null);
//
////        try {
////            // This object is not stored in the repo.
////            // It's simply used as a reference to the path where the file is stored.
////            File fileData = storageService.storeTmpFile(file);
////
////            final char SEPARATOR = parseSeparator(separator);
////
////            String filePathWithRoot = storageService.genFilePathWithRoot(fileData.getFilePath());
////
////            BufferedReader buffReader = new BufferedReader(new FileReader(filePathWithRoot));
////            String inputLine = null;
////            StringBuilder stringBuilder = new StringBuilder();
////
////            //Store the contents of the file to the StringBuilder.
////            while((inputLine = buffReader.readLine()) != null) {
////                stringBuilder.append(inputLine + "\n");
////            }
////
////            final CSVParser parser =
////                    new CSVParserBuilder()
//////                            .withSeparator('\t')
////                            .withSeparator(SEPARATOR)
////                            .withIgnoreQuotations(true)
////                            .build();
////
////            final CSVReader reader =
////                    new CSVReaderBuilder(new StringReader(stringBuilder.toString()))
////                            .withSkipLines(linesToSkip)
////                            .withCSVParser(parser)
////                            .build();
////
////            saveQuestionnaireDataToPostgres(reader, experimentId, dataDef);
////
////            redirectAttributes.addFlashAttribute("message",
////                    "You successfully uploaded " + file.getOriginalFilename() + "!");
////
////            storageService.deleteTmpFile(fileData.getFilePath());
////
////            return "Uploaded " + file.getOriginalFilename();
////        }
////        catch (StorageException | FileNotFoundException e) {
////            e.printStackTrace();
////            return "Failed to upload " + file.getOriginalFilename();
////        } catch (IOException e) {
////            e.printStackTrace();
////            return "Failed to upload " + file.getOriginalFilename();
////        }
//    }

//    @RequestMapping(value = "/addDataPoints", method = {RequestMethod.POST})
//    public String handleDataPointsUpload(@RequestParam("dataDefinitionId") long dataDefId,
//                                         @RequestParam("experimentId") long experimentId,
//                                         @RequestParam("file") MultipartFile file,
//                                         @RequestParam("linesToSkip") int linesToSkip,
//                                         @RequestParam("delimiter") String separator,
//                                         @RequestParam("timestampColumn") int timestampColumn,
//                                         @RequestParam("timestampFormat") String timestampFormat,
//                                         @RequestParam("timestampUnit") String timestampUnit,
////                                         @RequestParam("qualifier") String qualifier,
//                                         RedirectAttributes redirectAttributes) {
//
//        DataDefinition dataDef = dataDefinitionRepository.findById(dataDefId).orElseThrow(() ->
//                new ResourceNotFoundException("dataDefId:" + dataDefId));
//
//        experimentRepository.findById(experimentId).orElseThrow(() ->
//                new ResourceNotFoundException("experimentId: " + experimentId));
//
//        return parseFileAndStoreData(file, separator, linesToSkip, experimentId, dataDef,
//                redirectAttributes, DataBaseToStoreIn.INFLUX, null, null, timestampColumn, timestampFormat, timestampUnit);
//    }

    /**
     * Temporarily stores the passed file for parsing and saving its data into either the postgres or influx database.
     * @param file
     * @param separator Column separator
     * @param linesToSkip The number of lines to skip in the beginning
     * @param experimentId
     * @param dataDef
     * @param redirectAttributes
     * @param toStoreIn Enum to indicate storage in postgres or influx
     * @param questionnaireDef
     * @param cellItemsSeparator Cell data separator
     * @param timestampColumn
     * @param timestampFormat Format string for SimpleDataFormat
     * @param timestampUnit
     * @return
     */
    private String parseFileAndStoreData(MultipartFile file, String separator, int linesToSkip, long experimentId,
                                         DataDefinition dataDef, RedirectAttributes redirectAttributes,
                                         DataBaseToStoreIn toStoreIn, QuestionnaireDefinition questionnaireDef,
                                         String cellItemsSeparator, int timestampColumn, String timestampFormat,
                                         String timestampUnit) {
        try {
            // This object is not stored in the repo.
            // It's simply used as a reference to the path where the file is stored.
            File fileData = storageService.storeTmpFile(file);

            final char SEPARATOR = parseSeparator(separator);

            String filePathWithRoot = storageService.genFilePathWithRoot(fileData.getFilePath());

            BufferedReader buffReader = new BufferedReader(new FileReader(filePathWithRoot));
            String inputLine = null;
            StringBuilder stringBuilder = new StringBuilder();

            //Store the contents of the file to the StringBuilder.
            while((inputLine = buffReader.readLine()) != null) {
                stringBuilder.append(inputLine + "\n");
            }

            final CSVParser parser =
                    new CSVParserBuilder()
                            .withSeparator(SEPARATOR)
                            .withIgnoreQuotations(false)
                            .build();

            final CSVReader reader =
                    new CSVReaderBuilder(new StringReader(stringBuilder.toString()))
                            .withSkipLines(linesToSkip)
                            .withCSVParser(parser)
                            .build();

            switch(toStoreIn) {
                case INFLUX:
                    savePointsToInflux(reader, experimentId, dataDef, timestampColumn, timestampFormat, timestampUnit);
                    createAndSaveDatapointsObject(experimentId, dataDef);
                    break;
                case POSTGRES:
                    saveQuestionnaireDataToPostgres(reader, experimentId, dataDef, questionnaireDef,
                            cellItemsSeparator, timestampColumn, timestampFormat);
                    break;
            }

            storageService.deleteTmpFile(fileData.getFilePath());

            return "Uploaded " + file.getOriginalFilename();
        }
        catch (StorageException | FileNotFoundException e) {
            e.printStackTrace();
            return "Failed to upload " + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload " + file.getOriginalFilename();
        }
    }

    /**
     * Creates a data points object that is saved to the data points repository.
     * @param experimentId
     * @param dataDef
     */
    private void createAndSaveDatapointsObject(long experimentId, DataDefinition dataDef) {
        DataPoints dp = new DataPoints();

        dp.setExperimentId(experimentId);
        dp.setDataDefinitionId(dataDef.getId());
        dp.setMeasurementName(genMeasurementName(experimentId, dataDef));
        dp.setLastUpdated(new Date());

        dataPointsRepository.save(dp);
    }

    /**
     * Parses the separator symbol
     * @param separator
     * @return
     */
    private char parseSeparator(String separator) {
        switch(separator) {
            case "\\t":
                return '\t';
            case ",":
                return ',';
            default:
                return ' ';
        }
    }

    /**
     * Stores the data of the passed file in influx as a measurement
     * The rows are saved in batches as it is significantly faster than saving them individually.
     * @param reader
     * @param experimentId
     * @param dataDef
     * @param timestampColumn
     * @param timestampFormat
     * @param timestampUnit
     */
    private void savePointsToInflux(CSVReader reader, long experimentId, DataDefinition dataDef,
                                    int timestampColumn, String timestampFormat, String timestampUnit) {
        try {
            String[] header = reader.readNext();

            String[] line = reader.readNext();

            int lineNo = 1;

            while(line != null) {
                // Used to build a "row" in the database
                Point.Builder builder;

                if(timestampColumn < 0) {
                    builder = Point.measurement(genMeasurementName(experimentId, dataDef))
                            .time(System.nanoTime(), TimeUnit.NANOSECONDS);
                }
                else {
                    // Note: Decimals will be truncated.
                    builder = Point.measurement(genMeasurementName(experimentId, dataDef))
                            .time((long)Double.parseDouble(line[timestampColumn]), parseTimeUnit(timestampUnit));
                }

                for(int fieldNo = header.length - 2; fieldNo >= 0 ; fieldNo--) {
                    builder = builder.addField(header[fieldNo], line[fieldNo]);
                }

                Point point = builder.build();

                // Adds the created point to the batch to be saved
                batchPoints.point(point);

                if(lineNo % MAX_BATCH_SIZE == 0) {
                    influxDB.write(batchPoints);
                }

                lineNo++;

                line = reader.readNext();
            }

            influxDB.write(batchPoints);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the time unit used
     * @param timestampUnit
     * @return
     */
    private TimeUnit parseTimeUnit(String timestampUnit) {
        switch(timestampUnit) {
            case "milSec":
                return TimeUnit.MILLISECONDS;
            case "micSec":
                return TimeUnit.MICROSECONDS;
            case "nanSec":
                return TimeUnit.NANOSECONDS;
            default:
                return TimeUnit.SECONDS;
        }
    }

    /**
     * Generates the measurement name for the data points.
     * @param experimentId The experiment Id
     * @param dataDef The Data Definition object
     * @return The measurement name used to store the time series data.
     */
    private String genMeasurementName(long experimentId, DataDefinition dataDef) {
        return "" + experimentId + "-" + dataDef.getName();
    }

    /**
     * Parses the questionnaire data and maps them to the database tables.
     * If the passed file has a timestamp column,
     * the timestamp will be used to distinguish existing from new entries
     * when the same file with new rows is submitted.
     * @param reader
     * @param experimentId
     * @param dataDef
     * @param questionnaireDef
     * @param cellItemsSeparator
     * @param timestampColumn
     * @param timestampFormat
     */
    private void saveQuestionnaireDataToPostgres(CSVReader reader, long experimentId,
                                                 DataDefinition dataDef, QuestionnaireDefinition questionnaireDef,
                                                 String cellItemsSeparator, int timestampColumn,
                                                 String timestampFormat) {
        ArrayList<Question> questions = questionRepository.findAllByQuestionnaireDefinitionIdOrderByOrderNoAsc(
                questionnaireDef.getId());

        try {
            String[] header = reader.readNext();

            // Only when there are no questions related to the questionnaire definition, the asked questions are stored.
            if(questions.size() == 0) {
                int orderNo = 0;
                for(int q = 0; q < header.length; q++) {
                    if(q != timestampColumn) {
                        createAndSaveQuestionObject(questionnaireDef, header[q], orderNo);
                        orderNo++;
                    }
                }

                questions = questionRepository.findAllByQuestionnaireDefinitionIdOrderByOrderNoAsc(
                        questionnaireDef.getId());
            }

            String[] line = reader.readNext();

            Questionnaire newestQuestionnaire = questionnaireRepository
                    .findFirstByQuestionnaireDefinitionIdOrderByTimestampDesc(questionnaireDef.getId());

            Date newestSavedDate = null;

            if(newestQuestionnaire != null) {
                // Newest timestamp of a questionnaire taken.
                newestSavedDate = newestQuestionnaire.getTimestamp();
            }

            while(line != null) {
                Date timestamp = new Date();    // Current date and time.

                // If there is a column with a timestamp.
                if(timestampColumn >= 0) {
                    // Google Form default format: "yyyy/MM/dd hh:mm:ss aa z".
                    SimpleDateFormat dateFormat = new SimpleDateFormat(timestampFormat);

                    // Fixes a parse error when the timezone minutes are not included
                    if(timestampFormat.charAt(timestampFormat.length()-1) == 'z') {
                        Pattern p = Pattern.compile(".*[\\+|-][0-9]{1,2}");
                        Matcher m = p.matcher(line[timestampColumn]);

                        if(m.matches()) {
                            line[timestampColumn] = line[timestampColumn] + ":00";
                        }
                    }
                    timestamp = dateFormat.parse(line[timestampColumn]);
                }

                // If the current row has a timestamp before the newest questionnaire in the database,
                // it is skipped to prevent duplicate entries.
                if(newestSavedDate == null || timestamp.after(newestSavedDate)) {
                    Questionnaire questionnaire = createAndSaveQuestionnaireObject(experimentId, dataDef, questionnaireDef, timestamp);

                    int questionNo = 0;
                    for(int col = 0; col < line.length; col++) {
                        if(col != timestampColumn) {
                            long questionId = questions.get(questionNo++).getId();

                            // If the cellItemsSeparator is an empty string,
                            // it is assumed that a cell contains a single answer.
                            if(cellItemsSeparator.equals("")) {
                                createAndSaveAnswerObject(questionnaire, questionId, line[col]);
                            }
                            // Otherwise, the answers in the cell are split and saved as separate Answer objects.
                            else {
                                String[] cellAnswers = line[col].split(cellItemsSeparator);

                                for(String answerText : cellAnswers) {
                                    createAndSaveAnswerObject(questionnaire, questionId, answerText);
                                }
                            }
                        }
                    }
                }

                line = reader.readNext();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and saves a question object to the quesiton repository.
     * @param questionnaireDef The question defintion object.
     * @param text The question text.
     * @param orderNo The order of the question.
     */
    private void createAndSaveQuestionObject(QuestionnaireDefinition questionnaireDef, String text, int orderNo) {
        Question question = new Question();

        question.setQuestionnaireDefinitionId(questionnaireDef.getId());
        question.setOrderNo(orderNo);
        question.setText(text);

        questionRepository.save(question);
    }

    /**
     * Creates and saves a questionnaire object to the questionnaire repository.
     * @param experimentId The experiment ID.
     * @param dataDef The data definition object.
     * @param questionnaireDef The questionnaire definition object.
     * @param timestamp The timestamp for the questionnaire object.
     * @return The created questionnaire object.
     */
    private Questionnaire createAndSaveQuestionnaireObject(long experimentId, DataDefinition dataDef,
                                                           QuestionnaireDefinition questionnaireDef, Date timestamp) {
        Questionnaire questionnaire = new Questionnaire();

        questionnaire.setDataDefId(dataDef.getId());
        questionnaire.setExperimentId(experimentId);
        questionnaire.setQuestionnaireDefinitionId(questionnaireDef.getId());
        questionnaire.setTimestamp(timestamp);

        questionnaireRepository.save(questionnaire);
        return questionnaire;
    }

    /**
     * Creates and saves an answer object to the answer repository.
     * @param questionnaire The questionnaire object.
     * @param questionId The question ID.
     * @param answerText The answer text.
     */
    private void createAndSaveAnswerObject(Questionnaire questionnaire, long questionId, String answerText) {
        Answer answer = new Answer();

        answer.setQuestionnaireId(questionnaire.getId());
        answer.setQuestionId(questionId);
        answer.setText(answerText);

        answerRepository.save(answer);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
