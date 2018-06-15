package buildservice.services;

import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class DeployServiceImpl implements DeployService {

  private Logger logger = LoggerFactory.getLogger(DeployServiceImpl.class);

  private final String uri;

  @Autowired
  public DeployServiceImpl(@Value("${deployuri}") String uri) {
      this.uri =uri;
  }

  @Override
  public void deploy(String keyName) {

    try
         {
             /*
                 This is code to post and return a user object
              */
             RestTemplate rt = new RestTemplate();

             Map<String, String> vars = new HashMap<String, String>();
             vars.put("keyName", keyName);

             String result = rt.postForObject(uri, keyName, String.class, vars);
         }
         catch (HttpClientErrorException e)
         {
             /**
              *
              * If we get a HTTP Exception display the error message
              */
             logger.error("error:  " + e.getResponseBodyAsString());
         }
         catch(Exception e)
         {
             logger.error("error:  " + e.getMessage());
         }
     }

}
