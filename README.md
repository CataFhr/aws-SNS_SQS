• The application include the following functions:
   - upload an image in a S3 bucket and save metadata in a MySql db.
   - show metadata for the existing images
   - get metadata for a random image
   - download an image by name from S3
   - delete an image by name from S3 and metadata from db
    
• Create a standard SQS queue
• Create an SNS topic
• Create two new endpoints:
  -subscribe an email for notifications
  -unsubscribe an email from notifications
  
After a user visits the subscription endpoint, the specified email will receive a confirmation 
message.  Whenever a user visits the unsubscription endpoint, AWS should stop sending the email 
notifications.
Whenever an image is uploaded using your web application, a message describing that event should 
be published to the SQS queue.

• The application run a scheduled background process which extracts the SQS 
messages in batch and sends them to the SNS topic (and then an email recieved by the subscribers).
SNS notifications will be in plain text which includes:
  - an explanation that an image has been uploaded
  - the image metadata (size, name, extension)
  - a link to the web application endpoint for downloading the image
