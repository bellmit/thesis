package platform.bjarki.databaseClient;
import platform.bjarki.databaseClient.repository.DeviceInfluxRepository;
import org.jboss.logging.Logger;


public class DeviceClient {
    private static final Logger logger = Logger.getLogger(DeviceClient.class.getName());

    DeviceInfluxRepository deviceInfluxRepository = new DeviceInfluxRepository();

    public void insertDataInflux(String type, String device, String attributes, String dataid, String line){

        deviceInfluxRepository.insertValuesInflux(type,device,attributes,dataid,line);

    }
}