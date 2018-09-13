#### Objects manipoulation

* aws s3api list-objects --bucket biximontreal
* aws s3api list-objects --bucket biximontreal --prefix Stations --query Contents[].[Key] --output text


#### Enable MFA Delete and versioning

* aws s3api put-bucket-versioning --bucket webapp-status-reports --versioning-configuration '{"MFADelete":"Enabled","Status":"Enabled"}'
--mfa 'arn:aws:iam::<aws_account_id>:mfa/root-account-mfa-device <passcode>'
  
* aws s3api get-bucket-versioning --bucket webapp-status-reports
  
#### Multipart upload AWS CLI (Files grater than 100MB)

* Create an upload ID in the bucket where is required the multipart: This command will retrive an upload id that must be used to load files in this bucket thru the **multipart approach**.

	-aws s3api create-multipart-upload --bucket my-bucket --key testfile.

* Execute the multipart upload aming the file to be loaded:

	aws s3api complete-multipart-upload --multipart-upload file://mpustruct --bucket my-bucket --key 'multipart/01' --upload-id dfRtDYU0WWCCcH43C3WFbkRONycyCpTJJvxu2i5GYkZljF.Yxwh6XG7WfS2vC4to6HiV6Yjlx.cph0gtNBtJ8P3URCSbB7rjxI5iEwVDmgaXZOGgkk5nVTW16HOQ5l0R	

* List the current multiparts

	aws s3api list-multipart-uploads --bucket my-bucket

* Delete a multipart

	aws s3api create-multipart-upload --bucket my-bucket --key <key name>

  ---
  
  source: 
  
  * [cloud_formity](https://www.cloudconformity.com/conformity-rules/S3/s3-bucket-mfa-delete-enabled.html)
  * [AWS CLI](https://docs.aws.amazon.com/cli/latest/index.html)
