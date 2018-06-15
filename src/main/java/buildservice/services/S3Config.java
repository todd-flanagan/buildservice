package buildservice.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.model.GetObjectRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class S3Config {
  private Logger logger = LoggerFactory.getLogger(S3Config.class);

	@Value("${api-user.s3.region}")
	private String region;

  @Autowired
  private CredProviderDevelopment credProvider;


	@Bean
	public AmazonS3 s3client() {

  logger.info("Creating client");
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
								.withRegion(Regions.fromName(region))
		                        .withCredentials(credProvider.credProvider())
		                        .build();

    logger.info("client: " + s3Client);

		return s3Client;
	}
}
