package platform.fitbit;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;


import platform.model.DPUser;
import platform.model.Device;
import platform.model.DeviceType;

import platform.persistence.DeviceRepository;
import platform.util.InfluxDBConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;


import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;


import java.io.IOException;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class FitbitClient {

    private static final String NETWORK_NAME = "Fitbit";

    private static final String PROTECTED_RESOURCE_URL = "https://api.fitbit.com/1/user/%s/profile.json";
    private static final String PROTECTED_RESOURCE_URL_DEVICES = "https://api.fitbit.com/1/user/%s/devices.json";
    private static final String PROTECTED_RESOURCE_URL_ACTIVTITIES = "https://api.fitbit.com/1/user/%s/activities/recent.json";
    private static final String PROTECTED_RESOURCE_URL_STEPS = "https://api.fitbit.com/1/user/%s/activities/steps/date/today/1m.json";
    private static final Logger logger = LogManager.getLogger();
    private FitBitOAuth2AccessToken accessToken;
    private DeviceRepository deviceRepository;
    JsonParser parser = new JacksonJsonParser();

    //set up influxDB
    InfluxDBConfig influxDBConfig = new InfluxDBConfig();
    InfluxDB influxDB = influxDBConfig.connectToInflux();
    BatchPoints batchPoints = BatchPoints.database("thesisDB")
            .build();


    // set up service for scribe
    final String clientId = "22CW3C";
    final String clientSecret = "d9d8be511a6be324d5b3a2558aeb682f";
    final OAuth20Service service = new ServiceBuilder(clientId)
            .apiSecret(clientSecret)
            .scope("activity profile heartrate location nutrition social weight settings") // replace with desired scope
            //your callback URL to storeProjectFile and handle the authorization code sent by Fitbit
            .callback("http://localhost:3000/datasubject")
            .build(FitbitApi20.instance());


    public FitbitClient(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public String getAuthorizatioUrl() {
        return service.getAuthorizationUrl();
    }

    public FitBitOAuth2AccessToken authorize(String url) {
        try {
            final OAuth2AccessToken oAuth2AccessToken = service.getAccessToken(url);
            accessToken = (FitBitOAuth2AccessToken) oAuth2AccessToken;
        } catch (IOException e) {
            logger.debug(e);
        } catch (InterruptedException e) {
            logger.debug(e);
        } catch (ExecutionException e) {
            logger.debug(e);
        }
        return accessToken;
    }

    public String getProfile() throws InterruptedException, ExecutionException, IOException {

        OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL, accessToken.getUserId()));
        request.addHeader("x-li-format", "json");

        service.signRequest(accessToken, request);

        Response response = service.execute(request);
        return response.getBody();
    }

    public ArrayList<Device> getUserDevices(FitBitOAuth2AccessToken auth2AccessToken, DPUser dpUser) throws InterruptedException, ExecutionException, IOException {
        Response response = getRessources(PROTECTED_RESOURCE_URL_DEVICES, auth2AccessToken);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());
        ArrayList<Device> devices = new ArrayList<>();

        if (jsonNode.isArray()) {
            for (JsonNode objNode : jsonNode) {
                if (this.deviceRepository.existsByMuid(String.valueOf(objNode.get("id")))) {
                    devices.add(this.deviceRepository.findByMuid(String.valueOf(objNode.get("id"))));
                } else {
                    Device device = new Device();
                    device.setMuid(String.valueOf(objNode.get("id")));
                    device.setBatterylevel(String.valueOf(objNode.get("battery")));
                    device.setLastSyncTime(String.valueOf(objNode.get("lastSyncTime")));
                    device.setDeviceType(new DeviceType("fitbit", String.valueOf(objNode.get("deviceVersion")), String.valueOf(objNode.get("type"))));
                    device.setDataSubject(dpUser);
                    devices.add(device);
                   this.deviceRepository.save(device);
                }
            }
        }

        return devices;
    }


    public JsonNode getActvitities(FitBitOAuth2AccessToken auth2AccessToken) throws InterruptedException, ExecutionException, IOException {

        /*
        QueryResult queryResult = influxDB.query(new Query("SELECT last(time) FROM fitbit_actitivty", "thesisDB"));
       String instant = queryResult.toString();
       LocalDateTime ldt = LocalDateTime.ofInstant();
       */

        Response response = getRessources("https://api.fitbit.com/1/user/%s/activities/list.json?afterDate=2018-03-01&sort=asc&limit=20&offset=0", auth2AccessToken);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.getBody());
        JsonNode objNode = actualObj.get("activities");
        if (!objNode.isArray()) {
            // for (JsonNode objNode : jsonNode) {
            Point point1 = Point.measurement("fitbit_activity")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("userId", accessToken.getUserId())
                    .addField("activityDuration", String.valueOf(objNode.get("activeDuration")))
                    .addField("activityLevel", String.valueOf(objNode.get("activityLevel")))
                    .addField("averageHeartRate", String.valueOf(objNode.get("averageHeartRate")))
                    .addField("distance", String.valueOf(objNode.get("distance")))
                    .addField("distanceUnit", String.valueOf(objNode.get("distanceUnit")))
                    .addField("heartRateZones", String.valueOf(objNode.get("heartRateZones")))
                    .addField("activityId", String.valueOf(objNode.get("activityId")))
                    .addField("calories", String.valueOf(objNode.get("calories")))
                    .addField("description", String.valueOf(objNode.get("description")))
                    .addField("distance", String.valueOf(objNode.get("distance")))
                    .addField("duration", String.valueOf(objNode.get("duration")))
                    .addField("activityName", String.valueOf(objNode.get("activityName")))
                    .addField("source", String.valueOf(objNode.get("source")))
                    .addField("steps", String.valueOf(objNode.get("steps")))
                    .addField("startTime", String.valueOf(objNode.get("startTime")))
                    .build();
            batchPoints.point(point1);
            influxDB.write(batchPoints);
        }
        //}

        return actualObj;
    }

    public JsonNode getSteps(FitBitOAuth2AccessToken auth2AccessToken) throws InterruptedException, ExecutionException, IOException {
        Response response = getRessources(PROTECTED_RESOURCE_URL_STEPS, auth2AccessToken);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.getBody());
        JsonNode jsonNode = actualObj.get("activities-steps");
        if (jsonNode.isArray()) {
            for (JsonNode objNode : jsonNode) {
                Point point1 = Point.measurement("fitbit_Steps")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("userId", accessToken.getUserId())
                        .addField("dateTime", String.valueOf(objNode.get("dateTime")))
                        .addField("steps", String.valueOf(objNode.get("value")))
                        .build();
                batchPoints.point(point1);
                influxDB.write(batchPoints);
            }

        }

        return jsonNode;
    }


    public Response getRessources(String protectedResourceUrl, FitBitOAuth2AccessToken auth2AccessToken) throws InterruptedException, ExecutionException, IOException {
        OAuthRequest request = new OAuthRequest(Verb.GET, String.format(protectedResourceUrl, auth2AccessToken.getUserId()));
        request.addHeader("x-li-format", "json");
        service.signRequest(auth2AccessToken, request);
        Response response = service.execute(request);


        return response;
    }


}
