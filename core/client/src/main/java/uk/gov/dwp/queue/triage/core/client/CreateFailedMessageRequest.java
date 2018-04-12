package uk.gov.dwp.queue.triage.core.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CreateFailedMessageRequest {

    @NotNull
    private final FailedMessageId failedMessageId;
    @NotNull
    private final String brokerName;
    @NotNull
    private final String destination;
    @NotNull
    private final Instant sentAt;
    @NotNull
    private final Instant failedAt;
    @NotNull
    private final String content;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private final Map<String, Object> properties;
    @NotNull
    private final Set<String> labels;

    CreateFailedMessageRequest(@JsonProperty("failedMessageId") FailedMessageId failedMessageId,
                               @JsonProperty("brokerName") String brokerName,
                               @JsonProperty("destinationName") String destination,
                               @JsonProperty("sentAt") Instant sentAt,
                               @JsonProperty("failedAt") Instant failedAt,
                               @JsonProperty("content") String content,
                               @JsonProperty("properties") Map<String, Object> properties,
                               @JsonProperty("labels") Set<String> labels) {
        this.failedMessageId = failedMessageId;
        this.brokerName = brokerName;
        this.destination = destination;
        this.sentAt = sentAt;
        this.failedAt = failedAt;
        this.content = content;
        this.properties = properties;
        this.labels = labels;
    }

    public static CreateFailedMessageRequestBuilder newCreateFailedMessageRequest() {
        return new CreateFailedMessageRequestBuilder();
    }

    public FailedMessageId getFailedMessageId() {
        return failedMessageId;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public String getDestinationName() {
        return destination;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public Instant getFailedAt() {
        return failedAt;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public static class CreateFailedMessageRequestBuilder {
        private FailedMessageId failedMessageId;
        private String brokerName;
        private String destinationName;
        private Instant sentDateTime;
        private Instant failedDateTime;
        private String content;
        private Map<String, Object> properties = new HashMap<>();
        private Set<String> labels= new HashSet<>();

        private CreateFailedMessageRequestBuilder() {
        }

        public CreateFailedMessageRequest build() {
            return new CreateFailedMessageRequest(failedMessageId, brokerName, destinationName, sentDateTime, failedDateTime, content, properties, labels);
        }

        public CreateFailedMessageRequestBuilder withFailedMessageId(FailedMessageId failedMessageId) {
            this.failedMessageId = failedMessageId;
            return this;
        }

        public CreateFailedMessageRequestBuilder withBrokerName(String brokerName) {
            this.brokerName = brokerName;
            return this;
        }

        public CreateFailedMessageRequestBuilder withDestinationName(String destinationName) {
            this.destinationName = destinationName;
            return this;
        }

        public CreateFailedMessageRequestBuilder withSentDateTime(Instant sentDateTime) {
            this.sentDateTime = sentDateTime;
            return this;
        }

        public CreateFailedMessageRequestBuilder withFailedDateTime(Instant failedDateTime) {
            this.failedDateTime = failedDateTime;
            return this;
        }

        public CreateFailedMessageRequestBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public CreateFailedMessageRequestBuilder withProperties(Map<String, Object> properties) {
            this.properties = Optional.ofNullable(properties).map(HashMap::new).orElse(new HashMap<>());
            return this;
        }

        public CreateFailedMessageRequestBuilder withProperty(String key, Object value) {
            this.properties.put(key, value);
            return this;
        }

        public CreateFailedMessageRequestBuilder withLabels(Set<String> labels) {
            this.labels = Optional.ofNullable(labels).map(HashSet::new).orElse(new HashSet<>());
            return this;
        }

        public CreateFailedMessageRequestBuilder withLabel(String label) {
            this.labels.add(label);
            return this;
        }
    }
}
