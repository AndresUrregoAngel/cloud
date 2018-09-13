#### Training / Deplyments 
All exercices documents in this section are coming from the course Spark /AWS in coursera

Here is a personal repository for POC aws deployments or certification exams training.

---

### Commands
This  first example is deplopyed using the old spark-ec2 feature included in the spark standard versioin. Once the package is downloaded locally in the host and the AIM are created with the required Key pair in the console I proced to execute the commands below:

* Creation:
        spark-ec2/spark-ec2 -k NAME_OF_KEY_PAIR --identity-file=PATH_TO_PEM_FILE --region=us-west-2 --zone=us-west-2a --copy-aws-                 credentials --instance-type t2.micro --worker-instances 1 launch NAME_OF_YOUR_CLUSTER
 
 * Log in: 
                ec2/spark-ec2 -k NAME_OF_KEY_PAIR --identity-file=PATH_TO_PEM_FILE --region=us-west-2 --zone=us-west-2a login NAME_OF_YOUR_CLUSTER
                
... Option 2: ssh -i <identity-file>.pem root@master-ip   
...[resource](https://stackoverflow.com/questions/28002443/cluster-hangs-in-ssh-ready-state-using-spark-1-2-0-ec2-launch-script)
                
* Start / Stop :  spark-ec2/spark-ec2 -k NAME_OF_KEY_PAIR --identity-file=PATH_TO_PEM_FILE --region=us-west-2 --zone=us-west-2a stop NAME_OF_YOUR_CLUSTER
spark-ec2/spark-ec2 -k NAME_OF_KEY_PAIR --identity-file=PATH_TO_PEM_FILE --region=us-west-2 --zone=us-west-2a --copy-aws-credentials start NAME_OF_YOUR_CLUSTER

* Start EC2 instances thru AWS CLI:  aws ec2 start-instances --instance-id 'id instance'  --profile 

---

### Running a job:
* bin/spark-submit python/primes_2-0.py --master local[8] --executor-memory 20G
* bin/spark-submit python/primes_2-0.py --master spark://23.195.26.187:7077
* dependencies: bin/spark-submit --py-files python/dependencies.egg myapp.py
   

