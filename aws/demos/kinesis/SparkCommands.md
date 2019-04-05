##### Spark quick configuration

sources:

	- https://datawookie.netlify.com/blog/2017/07/installing-spark-on-ubuntu/
	
	- https://docs.aws.amazon.com/cloud9/latest/user-guide/sample-java.html#sample-java-sdk-creds


- start master and slave

	$SPARK_HOME/sbin/start-master.sh

	$SPARK_HOME/sbin/start-slave.sh spark://andrw-VirtualBox:7077


- stop master and slave 

	$SPARK_HOME/sbin/stop-slave.sh

	$SPARK_HOME/sbin/stop-master.sh


- Initiate Spark project

	mvn archetype:generate -DgroupId=com.streaming.sparkdemo -DartifactId=spark-demo -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

- Register a consumer

	aws kinesis register-stream-consumer --stream-arn arn:aws:kinesis:us-east-1:account:stream/KinesisDS --consumer-name consumer1
	
- Kinesis Data Generator

	https://awslabs.github.io/amazon-kinesis-data-generator/web/producer.html
	
	Template 1 : {{name.firstName}},{{name.lastName}},{{random.number(70)}},{{internet.ip}}
	log : kinesisdg / kinesisdg2019

	Template 2 :

				{
		"name" : {{name.firstName}},
		"lastname": {{name.lastName}},
		"age" : {{random.number(70)}},
		"yearsalary" : {{random.number({
				    "min" : 35000,
				    "max" : 200000
					    })}}


		}

- EMR web interfaces

	https://docs.aws.amazon.com/emr/latest/ManagementGuide/emr-web-interfaces.html

- Submit example KCL job

	bin/run-example --packages org.apache.spark:spark-streaming-kinesis-asl_2.11:2.3.0 streaming.JavaKinesisWordCountASL myapp  KinesisDS https://kinesis.us-east-1.amazonaws.com 
