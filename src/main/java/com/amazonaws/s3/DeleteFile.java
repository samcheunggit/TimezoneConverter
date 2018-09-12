package com.amazonaws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.credentials.Credentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class DeleteFile {
	
	private static final String outputFolderName = "outputFolder";
	private static final String bucketName = "timezone-converter-bucket";
	private static final String outputFileName = "output.csv";
	
	public void deleteFileFromS3() {
		
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.AP_SOUTHEAST_2)
				.withCredentials(new Credentials().getAWSStaticCredentialsProvider()).build();
		
		String fileName = outputFolderName + "/" + outputFileName;

		try {
            s3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
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
	}
}
