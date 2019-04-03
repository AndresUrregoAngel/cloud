##### Flow Steps

[AWS Source](https://aws.amazon.com/getting-started/projects/build-serverless-web-app-lambda-apigateway-s3-dynamodb-cognito/module-1/)

1. Create the S3 bucket:

    - Grant read access to all public via ACL
    
    ```
    {
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow", 
            "Principal": "*", 
            "Action": "s3:GetObject", 
            "Resource": "arn:aws:s3:::[YOUR_BUCKET_NAME]/*" 
        } 
      ] 
    }
   ``` 
    
    - Set a static web site on properties
    - Load the website folder content

2. Configure website authentication on Cognito

    - Create a manage pool list
    - Set the list on the file `config.js` to allow the web page hook the users pool list
