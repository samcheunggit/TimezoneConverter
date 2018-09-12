package com.amazonaws.s3;

import java.io.File;
import java.io.InputStream;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.credentials.Credentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadFile {
	
	private static final String outputFolderName = "outputFolder";
	private static final String bucketName = "timezone-converter-bucket";
	
	public String uploadFileToS3(String fileName, InputStream inputStream, ObjectMetadata metadata) {
		
		try {
		
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.AP_SOUTHEAST_2)
				.withCredentials(new Credentials().getAWSStaticCredentialsProvider()).build();
		
		
		// upload file to folder and set it to public
		String key = outputFolderName + "/" + fileName;
		s3.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		
		return s3.getUrl(bucketName, key).toString();
		
	}
    catch(AmazonServiceException e) {
        // The call was transmitted successfully, but Amazon S3 couldn't process 
        // it, so it returned an error response.
        e.printStackTrace();
    }
    catch(SdkClientException e) {
        // Amazon S3 couldn't be contacted for a response, or the client
        // couldn't parse the response from Amazon S3.
        e.printStackTrace();
    }
		
		return null;
		
		
	}
}
