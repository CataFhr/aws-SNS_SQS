package com.epam.aws.training.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.SetSubscriptionAttributesRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterPolicy {

  private final Map<String, MessageAttributeValue> filterPolicy = new HashMap<>();

  public void addAttribute(final String attributeName, final String attributeValue) {
    filterPolicy.put(attributeName, new MessageAttributeValue()
        .withDataType("String")
        .withStringValue(attributeValue));
  }

  public void apply(AmazonSNS snsClient, final String subscriptionArn) {
    final SetSubscriptionAttributesRequest request =
        new SetSubscriptionAttributesRequest(subscriptionArn,
            "FilterPolicy", formatFilterPolicy());
    snsClient.setSubscriptionAttributes(request);
  }

  private String formatFilterPolicy() {
    return filterPolicy.entrySet()
        .stream()
        .map(entry -> "\"" + entry.getKey() + "\": [" + entry.getValue() + "]")
        .collect(Collectors.joining(", ", "{", "}"));
  }
}
