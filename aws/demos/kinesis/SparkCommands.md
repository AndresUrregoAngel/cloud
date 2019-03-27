##### Spark quick configuration

source: https://datawookie.netlify.com/blog/2017/07/installing-spark-on-ubuntu/


- start master and slave

	$SPARK_HOME/sbin/start-master.sh
	$SPARK_HOME/sbin/start-slave.sh spark://andrw-VirtualBox:7077


- stop master and slave 

	$SPARK_HOME/sbin/stop-slave.sh
	$SPARK_HOME/sbin/stop-master.sh
