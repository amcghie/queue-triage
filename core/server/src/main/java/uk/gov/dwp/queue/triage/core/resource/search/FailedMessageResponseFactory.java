package uk.gov.dwp.queue.triage.core.resource.search;

import uk.gov.dwp.queue.triage.core.client.FailedMessageResponse;
import uk.gov.dwp.queue.triage.core.domain.FailedMessage;

public class FailedMessageResponseFactory {

    public FailedMessageResponse create(FailedMessage failedMessage) {
        return new FailedMessageResponse(
                failedMessage.getFailedMessageId(),
                failedMessage.getDestination().getBrokerName(),
                failedMessage.getDestination().getName(),
                failedMessage.getSentAt(),
                failedMessage.getFailedAt(),
                failedMessage.getContent(),
                failedMessage.getProperties()
        );
    }
}
