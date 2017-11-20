package com.spring.upload.FileUploadController;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.upload.model.FileBucket;

@RestController
public class FileUploadController {

	private static final Logger logger = LogManager.getLogger(FileUploadController.class);
	private static final String bucketName = "mp4-upload-1";
	private static String mimeType;
	private static String fileName;
	FileBucket fileInfo = new FileBucket();
	HttpHeaders headers = new HttpHeaders();

	BasicAWSCredentials credentials = new BasicAWSCredentials("aws_access_key_id ", "aws_secret_access_key");
	AmazonS3Client s3client = new AmazonS3Client(credentials);

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FileBucket> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {

		mimeType = file.getOriginalFilename().split("\\.")[1];
		logger.info("The file extension: " + mimeType);
		fileName = file.getOriginalFilename();

		if (!file.isEmpty()) {
			try {
				if (!mimeType.equals("mp4")) {
					String fileFormatError = fileName + " is not allowed to upload please mp4 type ";
					logger.error("The file Format error: " + fileName + " is not mp4 type");
					fileInfo.setFileName(fileFormatError);
					fileInfo.setFileSize(file.getSize()/(1024*1024));
					return new ResponseEntity<FileBucket>(fileInfo, HttpStatus.BAD_REQUEST);
				}

				else {

					try {

						logger.info("Uploading a new object to S3 from a file\n");

						InputStream is = file.getInputStream();
						s3client.putObject(new PutObjectRequest(bucketName, fileName, is, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
						logger.info("Uploading sucessful");
						logger.info("The file Name is: " + fileName);
						fileInfo.setFileName(fileName);
						fileInfo.setFileSize(file.getSize()/(1024*1024));
						headers.add("file Upload Succesfully", fileName);
						/*IContainer container = IContainer.make();
						int result = container.open(UPLOAD_LOCATION + file.getOriginalFilename(), IContainer.Type.READ, null);
						long duration = container.getDuration();
						logger.info("File Duration: "+duration);*/

					} catch (IllegalArgumentException ex) {
						System.out.println(ex.getMessage());
					} catch (AmazonServiceException ase) {

						logger.info("Error Message:    " + ase.getMessage());
						logger.info("HTTP Status Code: " + ase.getStatusCode());
						logger.info("AWS Error Code:   " + ase.getErrorCode());
						logger.info("Error Type:       " + ase.getErrorType());
						logger.info("Request ID:       " + ase.getRequestId());

					} catch (AmazonClientException ace) {

						logger.info("Error Message: " + ace.getMessage());
					}

					return new ResponseEntity<FileBucket>(fileInfo, headers, HttpStatus.OK);
				}
			} catch (Exception ex) {
				return new ResponseEntity<FileBucket>(HttpStatus.BAD_REQUEST);
			}

		} else {
			return new ResponseEntity<FileBucket>(HttpStatus.BAD_REQUEST);
		}
	}

}
