# TimezoneConverter

http://timezoneconverterenv.dza322s4uw.ap-southeast-2.elasticbeanstalk.com

# Description
This application is written in Java Servlet, it utilises Google Timezone API to convert UTC datetime, latitude and longtitude to time zone and localised time, it will only accept csv file and generate the output csv file for the user to download.

The application is put on AWS Elastic Beanstalk, and using AWS S3 for storage of output file. The conversion function is set in AWS lambda to pass the parameters from input.csv and it will return the required information and write in output.csv.
