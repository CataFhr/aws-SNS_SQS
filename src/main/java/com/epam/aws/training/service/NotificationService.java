package com.epam.aws.training.service;

import com.amazonaws.services.sqs.model.Message;

import java.util.List;

public interface NotificationService {

  void subscribeEmail(String email);

  void unsubscribeEmail(String email);

  void sendMessageToQueue(String message);

  void sendMessageToTopic(String message);

  List<Message> readMessages();
}
