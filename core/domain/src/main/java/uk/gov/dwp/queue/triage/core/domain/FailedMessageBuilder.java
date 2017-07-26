package uk.gov.dwp.queue.triage.core.domain;

import uk.gov.dwp.queue.triage.core.domain.FailedMessageStatus.Status;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class FailedMessageBuilder {

    private FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();
    private Destination destination;
    private Instant sentDateTime;
    private Instant failedDateTime;
    private String content;
    private Map<String, Object> properties = new HashMap<>();
    private FailedMessageStatus failedMessageStatus;

    private FailedMessageBuilder() {
    }

    public static FailedMessageBuilder newFailedMessage() {
        return new FailedMessageBuilder()
                .withFailedMessageStatus(Status.FAILED);
    }

    public static FailedMessageBuilder clone(FailedMessage failedMessage) {
        return newFailedMessage()
                .withFailedMessageId(failedMessage.getFailedMessageId())
                .withDestination(failedMessage.getDestination())
                .withSentDateTime(failedMessage.getSentAt())
                .withFailedDateTime(failedMessage.getFailedAt())
                .withContent(failedMessage.getContent())
                .withProperties(failedMessage.getProperties())
                .withFailedMessageStatus(failedMessage.getFailedMessageStatus());
    }

    public FailedMessage build() {
        return new FailedMessage(failedMessageId, destination, sentDateTime, failedDateTime, content, properties, failedMessageStatus);
    }

    public FailedMessageBuilder withNewFailedMessageId() {
        this.failedMessageId = FailedMessageId.newFailedMessageId();
        return this;
    }

    public FailedMessageBuilder withFailedMessageId(FailedMessageId failedMessageId) {
        this.failedMessageId = failedMessageId;
        return this;
    }

    public FailedMessageBuilder withDestination(Destination destination) {
        this.destination = destination;
        return this;
    }

    public FailedMessageBuilder withSentDateTime(Instant sentDateTime) {
        this.sentDateTime = sentDateTime;
        return this;
    }

    public FailedMessageBuilder withFailedDateTime(Instant failedDateTime) {
        this.failedDateTime = failedDateTime;
        return this;
    }

    public FailedMessageBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public FailedMessageBuilder withProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    public FailedMessageBuilder withProperty(String key, Object value) {
        this.properties.put(key, value);
        return this;
    }

    public FailedMessageBuilder withFailedMessageStatus(Status status) {
        this.failedMessageStatus = FailedMessageStatus.failedMessageStatus(status);
        return this;
    }

    public FailedMessageBuilder withFailedMessageStatus(FailedMessageStatus failedMessageStatus) {
        this.failedMessageStatus = failedMessageStatus;
        return this;
    }
}
