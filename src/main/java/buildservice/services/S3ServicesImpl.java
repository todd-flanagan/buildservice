package buildservice.services;


import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.ObjectMetadata;

import buildservice.storage.StorageService;
import java.nio.file.Path;

@Service
public class S3ServicesImpl implements S3Services {

	private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);

	private AmazonS3 s3client;
  private StorageService storageService;

	@Value("${api-user.s3.bucket}")
	private String bucketName;

	@Autowired
	public S3ServicesImpl(AmazonS3 s3client, StorageService storageService) {
		logger.info("clinet: " + s3client);
        this.s3client = s3client;

				this.storageService = storageService;
	}

	@Override
	public void uploadFile(String keyName) {

		try {


            logger.info("Uploading an object");

						Path ctf = storageService.loadCtf(keyName);

						logger.info("clinet: " + s3client);

						// Upload a file as a new object with ContentType and title specified.
						PutObjectRequest request = new PutObjectRequest(bucketName, keyName, ctf.toFile());
						logger.info("clinet: " + s3client);

						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType("plain/text");
						metadata.addUserMetadata("x-amz-meta-title", keyName);

						logger.info("metadata: " + metadata);
						logger.info("request: " + request);

						request.setMetadata(metadata);
						s3client.putObject(request);

        } catch (AmazonServiceException ase) {
        	logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
					logger.info("Error Message:    " + ase.getMessage());
					logger.info("HTTP Status Code: " + ase.getStatusCode());
					logger.info("AWS Error Code:   " + ase.getErrorCode());
					logger.info("Error Type:       " + ase.getErrorType());
					logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
        		logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
				}


	}

}
