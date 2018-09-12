package com.amazonaws.credentials;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class Credentials {
	
    private static final String accessKey = "AKIAIHMIWEXI3YNVLGLQ";
    private static final String secretKey = "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";
 	
	public Credentials(){}
	
	public BasicAWSCredentials getBasicAWSCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}
	
	public AWSStaticCredentialsProvider getAWSStaticCredentialsProvider() {
		return new AWSStaticCredentialsProvider(getBasicAWSCredentials());
	}

}
