## Flow:


1. sudo apt-get update
2. sudo apt-get install python3-pip
3. sudo pip3 install awscli
4. curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.32.1/install.sh | bash
5. source ~/.bashrc
6. nvm install 7
7. npm init
8. aws s3api get-object --bucket poc-developers --key http_server.js http_server.js
9. aws s3api get-object --bucket poc-developers --key package.json package.json
10. open tmux and keep on running the server
11. Create the AIM

Default configuration php
#!/bin/bash
yum update -y
amazon-linux-extras install -y lamp-mariadb10.2-php7.2 php7.2
yum install -y httpd mariadb-server
systemctl start httpd
systemctl enable httpd
usermod -a -G apache ec2-user
chown -R ec2-user:apache /var/www
chmod 2775 /var/www
find /var/www -type d -exec chmod 2775 {} \;
find /var/www -type f -exec chmod 0664 {} \;
echo "<?php phpinfo(); ?>" > /var/www/html/phpinfo.php


## AIM 

1. create a new VM with the AIM
2. set at starting boostrapping `sudo apt-get update`

## Create AUtoscaling group

1. Create AutoScaling template based on the same AIM
2. Create the autoscaling group


## RDS

1. command connection mysql -h cinncopoc.czjiomcrhfyx.us-east-1.rds.amazonaws.com -u cinncopoc -p cinncopoc --port=3306
2.  
 
  [source] (https://hackernoon.com/tutorial-creating-and-managing-a-node-js-server-on-aws-part-1-d67367ac5171)
 
  [source 2] (https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/user-data.html) 
