package platform.bjarki.beans.services;


import platform.bjarki.databaseClient.DeviceClient;
import com.google.gson.Gson;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import platform.model.bjarki.DataMessage;
import platform.model.bjarki.ReplyMessage;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

@Named
@ApplicationScoped
public class DataService {
    private static final Logger logger = Logger.getLogger(DataService.class.getName());


    private Gson gson = new Gson();
    private DeviceClient eyetrackerClient = new DeviceClient();
    private ReplyMessage replyMessage = new ReplyMessage();

    private String ERROR_RESPONSE = "Something went wrong, data was not saved to database";
    private String DATA_SAVED = "Data saved to database";


    public String createJsonStringResponse(String message, String data, Boolean success){
        replyMessage.setReplyMessage(message);
        replyMessage.setData(data);
        replyMessage.setSucess(success);
        return gson.toJson(replyMessage);
    }

    public String saveDataToInflux(String message){

        DataMessage dataMessage = gson.fromJson(message, DataMessage.class);
        String type = dataMessage.getType();
        String dataid = dataMessage.getId();
        String attributes = dataMessage.getAttributes();
        String device = dataMessage.getDevice();

        try (BufferedReader br = new BufferedReader(new StringReader(dataMessage.getData()))) {
            String line;
            while ((line = br.readLine()) != null) {
                eyetrackerClient.insertDataInflux(type,device,attributes,dataid,line);
            }
            return createJsonStringResponse(DATA_SAVED, dataMessage.getType(), true);

        } catch(IOException e) {
            logger.warn("error in Save raw Data " + e.toString());
            return createJsonStringResponse(ERROR_RESPONSE, dataMessage.getType(), false);
        }
    }


}
