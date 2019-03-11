### Flow:


#### prior steps

- create key pair
- copy the `js` file in s3 buckets

  `aws s3api put-object --bucket poc-developers --key http_server.js --body http_server.js --storage-class ONEZONE_IA`
  
- create EC2 instance

  Boostrapping:
  
  `sudo apt-get update`
  
  `sudo apt-get install python3-pip -y`
  
  `pip3 install awscli`
  
  `curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.32.1/install.sh | bash`
  
  `source ~/.bashrc`
  
 ` nvm install 7`
  
  `aws s3api get-object --bucket poc-developers --key package.json package.json`
  
  `aws s3api get-object --bucket poc-developers --key http_server.js http_server.js`
  
  `npm install express --save-dev`
  
  `node index.js`
  
  
  
  [source] (https://hackernoon.com/tutorial-creating-and-managing-a-node-js-server-on-aws-part-1-d67367ac5171)
  [source 2] (https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/user-data.html) 
