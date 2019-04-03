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
        - Add the website application to the user pool under the name `WildRydesWebApp`
        - Uncheck the Generate client secret option. Client secrets aren't currently supported for use with browser-based applications.
    - Set the list on the file `config.js` to allow the web page hook the users pool list adding `userPoolId` and `userPoolClientId`

3. Create serverless backend

    - Create DynamoDB table
        - Name: Rides
        - PK: RideId
    - Create lambda function with the name `RequestUnicorn`
        - Test the lambda function
        ```
        {
            "path": "/ride",
            "httpMethod": "POST",
            "headers": {
                "Accept": "*/*",
                "Authorization": "eyJraWQiOiJLTzRVMWZs",
                "content-type": "application/json; charset=UTF-8"
            },
            "queryStringParameters": null,
            "pathParameters": null,
            "requestContext": {
                "authorizer": {
                    "claims": {
                        "cognito:username": "the_username"
                    }
                }
            },
            "body": "{\"PickupLocation\":{\"Latitude\":47.6174755835663,\"Longitude\":-122.28837066650185}}"
        }
      ```
4. Deploy an API Gateway and hook it up along with the lambda function

    - API Name: `WildRydes`
    - Set the function as `edge optimized`
    - Create a resource called `ride`
    - Create a method POST and linked with the lambda function
        - Edit method request card and add `WildRydes Cognito` for authorization 
    
5. Configure the pools authorizer for API GW

    - Authorizer name: `WildRydes`
    - Token source : `Authorization`
      
