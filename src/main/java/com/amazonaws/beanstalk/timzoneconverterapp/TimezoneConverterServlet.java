package com.amazonaws.beanstalk.timzoneconverterapp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.credentials.Credentials;
import com.amazonaws.lambda.timezoneapi.RequestClass;
import com.amazonaws.regions.Regions;
import com.amazonaws.s3.DeleteFile;
import com.amazonaws.s3.UploadFile;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.datetimeformtter.DatetimeFormatter;
import com.google.gson.Gson;


/**
 * Servlet implementation class TimezoneConverterServlet
 */

@WebServlet(name = "TimezoneConverterServlet", urlPatterns = {"/upload", "/restart"})
@MultipartConfig
public class TimezoneConverterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String outputFileName = "output.csv";
	private static final Integer NUM_ARGUMENTS = 3;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimezoneConverterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	} 

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	// Post request from result.jsp
    	if(request.getParameter("restart") != null && request.getParameter("restart").equalsIgnoreCase("restart")) {
    		new DeleteFile().deleteFileFromS3();
    		request.getRequestDispatcher("/index.jsp").forward(request, response);
    	}
    	// Post request from index.jsp
    	else {
        	// Create path components to save the file
            final Part filePart = request.getPart("file");
            String fileName = filePart.getSubmittedFileName();
        	String fileExt = "";
        	int i = fileName.lastIndexOf('.');
        	if (i > 0) {
        		fileExt = fileName.substring(i+1);
        	}
        	
        	System.out.println("filename: "+fileName+" fileExt: "+fileExt);            
            if(filePart.getSize() != 0 && fileExt.equalsIgnoreCase("csv")) {
            	
            	InputStream fileContent = filePart.getInputStream();
            	List<String> outputList = readAndProcessFile(fileContent);
                
                if(!outputList.isEmpty() && outputList != null) {
                	System.out.println("output List: "+outputList.toString());
                	String outputFileURL = outputFile(outputList);
                	if(outputFileURL != null && !outputFileURL.isEmpty()) {
                		request.setAttribute("message","Converted successfully, please click the following link to download:");
                		request.setAttribute("outputFileURL",outputFileURL);
                	}
                	else {
                		request.setAttribute("message","Error when writing the output file.");
                	}
                }
                else {
                	request.setAttribute("message","Error when processing the input file.");
                }
            }
            else {
            	request.setAttribute("message","The file is empty or incorrect file type, please upload a CSV file.");
            }
            request.getRequestDispatcher("/result.jsp").forward(request, response);
    	}

    }
    
    private static List<String> readAndProcessFile(InputStream fileContent){
     	
        Long timestamp = null;
        Double latitude = null;
        Double longtitude = null;
        String line ="";
        List<String> outputList = new ArrayList<String>();
                
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileContent))) {

        	int index = 0;
            while ((line = br.readLine()) != null) {
            	
            	if (line == null || line.trim().isEmpty()) {
                    System.out.println(String.format("Row %s: Skip empty row.", index));
                    outputList.add(String.format("Empty Row %s", index));
                    continue;
                }

                // use comma as separator
                String[] record = line.split(",");
                
                if (NUM_ARGUMENTS != record.length) {
                    System.out.println(String.format("Row %s: No. of arguments does not match.", index));
                    outputList.add(String.format("Row %s: No. of arguments does not match.", index));
                    continue;
                }
                
                timestamp = new DatetimeFormatter(record[0]).datetimeToEpochtime();
                
                try {                	
                    latitude = Double.parseDouble(record[1]);
                    longtitude = Double.parseDouble(record[2]);               
                } catch (Exception e) {
                    System.out.println(String.format("Row %s: Latitude or longtitude should be a number.", index));
                    outputList.add(String.format("Row %s: Latitude or longtitude should be a number.", index));
                    continue;
                }
                String timeZoneId = invokeGetZoneId(latitude, longtitude, timestamp);
                String outputDate = new DatetimeFormatter(record[0]).localiseDatetime(timeZoneId);
                outputList.add(String.join(",", line, timeZoneId, outputDate));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputList;
    }
	 
    private static String invokeGetZoneId(Double latitude, Double longtitude, Long timestamp) {

    	 // (1) Define the AWS Region in which the function is to be invoked
         // (2) Instantiate AWSLambdaClientBuilder to build the Lambda client
         // (3) Build the client, which will ultimately invoke the function
         AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                 .withRegion(Regions.AP_SOUTHEAST_2)
                 .withCredentials(new Credentials().getAWSStaticCredentialsProvider()).build();
         
         Gson gson = new Gson();
         RequestClass requestClass = new RequestClass();
         requestClass.setLatitude(latitude);
         requestClass.setLongtitude(longtitude);
         requestClass.setTimestamp(timestamp);
                  
      // (4) Create an InvokeRequest with required parameters
         InvokeRequest invokeRequest = new InvokeRequest()
                 .withFunctionName("getZoneId")
                 .withPayload(gson.toJson(requestClass));
         
         
         InvokeResult invokeResult = null;
         try {
         	// (5) Invoke the function and capture response
             invokeResult = awsLambda.invoke(invokeRequest);
             
             ByteBuffer byteBuf = invokeResult.getPayload();
             if (byteBuf != null) {
                 String result = StandardCharsets.UTF_8.decode(byteBuf).toString();
                 System.out.println("Result: "+result);
                 return result;
             } else {
            	 return null;
             }
         }
         catch (Exception e) {
             System.out.println(e);
         }
         return null;
	}
    
    private static String outputFile(List<String> outputList) {
            try {
                String result = String.join(System.lineSeparator(), outputList);
                
                InputStream inputStream = new ByteArrayInputStream(result.getBytes());
                
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("text/csv");
                metadata.setContentLength(result.getBytes().length);
   
                return new UploadFile().uploadFileToS3(outputFileName, inputStream, metadata);

            } catch (Exception e) {
                System.out.println("Output File Error: "+e.getMessage());
                e.printStackTrace();
                return null;
            }
    }
}
