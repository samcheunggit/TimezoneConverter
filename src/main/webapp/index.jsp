<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap CSS -->
    	<link rel="stylesheet" href="css/bootstrap.min.css">
    	
    	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
	    <script src="js/bootstrap.min.js" ></script> 
        <title>Timezone Converter</title>
    </head>
	<body>
	<div class="jumbotron">
		<div class="container">
			<div>
				<h5>Upload a CSV file with format (UTC datetime,latitude,longtitude)</h5>
				<p>Example: 2013-07-10 02:52:49,-44.490947,171.220966</p>
			</div>
			<br>
			<br>
			<div>
				<form action="upload" method="post" enctype="multipart/form-data">
				    <div class="input-group mb-3">
			            <div class="custom-file">
			                <input type="file" name="file" class="custom-file-input" id="file"/>
			                <label class="custom-file-label" for="file">Choose file</label>
			            </div>
			            <div class="input-group-append">
			                <input type="submit" value="Submit" class="btn btn-info"></input>
			        	</div>
				        <script>
				            $('#file').on('change',function(){
				                //get the file name
				                var fileName = $(this).val();
				
				                // Change the node's value by removing the fake path (Chrome)
				                fileName = fileName.replace("C:\\fakepath\\", "");
				
				                if (fileName != undefined || fileName != "") {
				                  $(this).next(".custom-file-label").html(fileName);
				                }
				            })
				        </script>
					</div>
		    	</form> 
			</div>  
		</div>
	</div>
	</body>
</html>
