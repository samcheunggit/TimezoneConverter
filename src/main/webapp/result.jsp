<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <title>Timezone Converter</title>
    </head>
    <body>
	    <div class="jumbotron">
			<div class="container">
		        <div id="result">
		            <h5>${requestScope["message"]}</h5>
		            <br>
		            <div>
		            	<a href='${requestScope["outputFileURL"]}'>${requestScope["outputFileURL"]}</a>
		            	<br><br>
			            <form action="restart" method="post">
			            	<input type="submit" name="restart" value="Restart" class="btn btn-info"></input>
			            </form>
		            </div>            
		        </div>
	        </div>
	    </div>
    </body>
</html>
