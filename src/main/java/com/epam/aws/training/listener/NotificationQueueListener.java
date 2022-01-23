package com.epam.aws.training.listener;

import com.amazonaws.services.sqs.model.Message;
import com.epam.aws.training.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.var;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationQueueListener {

  private final NotificationService notificationService;

  @Scheduled(fixedRate = 3000)
  public void readBatchFromQueueAndPushToTopic() {
    List<Message> messages = notificationService.readMessages();
    messages.forEach(message -> notificationService.sendMessageToTopic(message.getBody()));
  }
}
