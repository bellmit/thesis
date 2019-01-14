package platform.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken;
import com.github.scribejava.core.model.*;


import platform.model.DPUser;
import platform.model.Device;
import platform.persistence.DPUserRepository;
import platform.persistence.DeviceRepository;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

import java.io.IOException;


import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class FitbitController {


    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    private static final Token EMPTY_TOKEN = null;
    Map<Long,FitBitOAuth2AccessToken> fitbitMap = new HashMap<>();

    @Autowired
    private platform.fitbit.FitbitClient FitbitClient;

    private DeviceRepository deviceRepository;
    private DPUserRepository dataSubjectRepository;

    public FitbitController( DPUserRepository dataSubjectRepository, DeviceRepository deviceRepository) {

        this.dataSubjectRepository = dataSubjectRepository;
        this.deviceRepository = deviceRepository;
    }


    @RequestMapping(value = "/token", method = {RequestMethod.POST, RequestMethod.GET,RequestMethod.OPTIONS})
    public String getAuthURL(HttpServletRequest request) throws IOException, URISyntaxException {
/*
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        String url1 = FitbitClient.getAuthorizatioUrl();
        return new ModelAndView("redirect:/" + url1);

*/
        String url1 = FitbitClient.getAuthorizatioUrl();
/*
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url1);
*/
        return url1;


    }


    @RequestMapping(value = "/accesstoken", method = {RequestMethod.POST, RequestMethod.GET})
    public void getAccessToken(@RequestParam long id, HttpServletRequest request) throws InterruptedException, ExecutionException, IOException, URISyntaxException {
        String receivedUrl =  request.getHeader("referer");// request.getRequestURL().toString() + "?" + request.getQueryString();//request.getHeader("referer");
        String[] splitter = receivedUrl.split("=");
        String requestcode = splitter[1];
        FitBitOAuth2AccessToken auth2AccessToken = FitbitClient.authorize(requestcode);

        fitbitMap.put(id,auth2AccessToken);
    }

    @RequestMapping(value = "/fitbitdevices", method = {RequestMethod.POST, RequestMethod.GET})
    public ArrayList<Device> getFitbitDevices(@RequestParam long id) throws InterruptedException, ExecutionException, IOException, URISyntaxException {
        FitBitOAuth2AccessToken  token = fitbitMap.get(id);
       DPUser dpUser = dataSubjectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id:" + id));


        return FitbitClient.getUserDevices(token,dpUser);
    }
    @RequestMapping(value = "/activities", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonNode getFitbitActivity(@RequestParam long id) throws InterruptedException, ExecutionException, IOException, URISyntaxException {
        FitBitOAuth2AccessToken  token = fitbitMap.get(id);

        return FitbitClient.getActvitities(token);
    }


    @RequestMapping(value = "/steps", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonNode getFitbitSteps(@RequestParam long id) throws InterruptedException, ExecutionException, IOException, URISyntaxException {
            FitBitOAuth2AccessToken  token = fitbitMap.get(id);

         return FitbitClient.getSteps(token);
    }

}
