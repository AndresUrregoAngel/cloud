##### Spark quick configuration

source: https://datawookie.netlify.com/blog/2017/07/installing-spark-on-ubuntu/


- start master and slave

	$SPARK_HOME/sbin/start-master.sh

	$SPARK_HOME/sbin/start-slave.sh spark://andrw-VirtualBox:7077


- stop master and slave 

	$SPARK_HOME/sbin/stop-slave.sh

	$SPARK_HOME/sbin/stop-master.sh


- Initiate SPark project

	mvn archetype:generate -DgroupId=com.streaming.sparkdemo -DartifactId=spark-demo -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

- Register a consumer

	aws kinesis register-stream-consumer --stream-arn arn:aws:kinesis:us-east-1:account:stream/KinesisDS --consumer-name consumer1