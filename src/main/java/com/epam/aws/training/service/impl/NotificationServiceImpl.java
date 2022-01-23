package com.epam.aws.training.service.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.epam.aws.training.config.properties.SNSClientProperties;
import com.epam.aws.training.config.properties.SQSClientProperties;
import com.epam.aws.training.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.var;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private static final String SNS_PROTOCOL = "email";

  private final SNSClientProperties snsClientProperties;
  private final SQSClientProperties sqsClientProperties;
  private final AmazonSNS snsClient;
  private final AmazonSQS sqsClient;

  @Override
  public void subscribeEmail(String email) {
    try {
      SubscribeRequest request = new SubscribeRequest()
          .withProtocol(SNS_PROTOCOL)
          .withEndpoint(email)
          .withTopicArn(snsClientProperties.getTopicArn());
      snsClient.subscribe(request);
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public void unsubscribeEmail(String email) {
    try {
      ListSubscriptionsByTopicResult listResult = snsClient.listSubscriptionsByTopic(snsClientProperties.getTopicArn());
      List<Subscription> subscriptions = listResult.getSubscriptions();
      subscriptions.stream()
          .filter(subscription -> email.equals(subscription.getEndpoint()))
          .findAny()
          .ifPresent(subscription -> unsubscribe(subscription.getSubscriptionArn()));
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public void sendMessageToQueue(String message) {
    try {
      SendMessageRequest request = new SendMessageRequest()
          .withQueueUrl(sqsClientProperties.getQueueUrl())
          .withMessageBody(message)
          .withDelaySeconds(5);
      sqsClient.sendMessage(request);
    } catch (AmazonSQSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public void sendMessageToTopic(String message) {
    try {
      PublishRequest publishRequest = new PublishRequest()
          .withMessage(message)
          .withTopicArn(snsClientProperties.getTopicArn());
      snsClient.publish(publishRequest);
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public List<Message> readMessages() {
    try {
      String queueUrl = sqsClientProperties.getQueueUrl();
      ReceiveMessageRequest request = new ReceiveMessageRequest()
          .withQueueUrl(queueUrl)
          .withWaitTimeSeconds(10)
          .withMaxNumberOfMessages(10);
      List<Message> messages = sqsClient.receiveMessage(request).getMessages();
      messages.stream()
              .map(Message::getReceiptHandle)
              .forEach(receipt -> sqsClient.deleteMessage(queueUrl, receipt));
      return messages;
    } catch (AmazonSQSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  private void unsubscribe(String subscriptionArn) {
    try {
      UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest()
          .withSubscriptionArn(subscriptionArn);
      snsClient.unsubscribe(unsubscribeRequest);
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }
}
