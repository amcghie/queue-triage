package uk.gov.dwp.queue.triage.core.resource.status;

import org.junit.Test;
import uk.gov.dwp.queue.triage.core.domain.StatusHistoryEvent;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.FAILED;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.SENT;
import static uk.gov.dwp.queue.triage.core.domain.status.StatusHistoryResponseMatcher.statusHistoryResponse;

public class StatusHistoryResponseAdapterTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = FailedMessageId.newFailedMessageId();
    private static final Instant NOW = Instant.now();

    private final StatusHistoryResponseAdapter underTest = new StatusHistoryResponseAdapter();

    @Test
    public void failedMessageWithStatusHistoryEntries() {
        List<StatusHistoryEvent> statusHistoryEvents = Arrays.asList(
                new StatusHistoryEvent(StatusHistoryEvent.Status.FAILED, NOW),
                new StatusHistoryEvent(StatusHistoryEvent.Status.SENT, NOW.plusSeconds(1))
        );
        assertThat(underTest.toStatusHistoryResponses(statusHistoryEvents), contains(
                statusHistoryResponse().withStatus(FAILED).withEffectiveDateTime(NOW),
                statusHistoryResponse().withStatus(SENT).withEffectiveDateTime(NOW.plusSeconds(1))
        ));
    }

    // FAILED & CLASSIFIED -> FAILED

    @Test
    public void responseOnlyContainsASingleEntryWhenFailedAndClassifiedStatusAreConcurrent() {
        List<StatusHistoryEvent> statusHistoryEvents = Arrays.asList(
                new StatusHistoryEvent(StatusHistoryEvent.Status.FAILED, NOW),
                new StatusHistoryEvent(StatusHistoryEvent.Status.CLASSIFIED, NOW.plusSeconds(1))
        );
        assertThat(underTest.toStatusHistoryResponses(statusHistoryEvents), contains(
                statusHistoryResponse().withStatus(FAILED).withEffectiveDateTime(NOW.plusSeconds(1))
        ));
    }

    @Test
    public void singleStatusHistoryEntry() {
        List<StatusHistoryEvent> statusHistoryEvents = Arrays.asList(
                new StatusHistoryEvent(StatusHistoryEvent.Status.CLASSIFIED, NOW)
        );
        assertThat(underTest.toStatusHistoryResponses(statusHistoryEvents), contains(
                statusHistoryResponse().withStatus(FAILED).withEffectiveDateTime(NOW)
        ));
    }
}