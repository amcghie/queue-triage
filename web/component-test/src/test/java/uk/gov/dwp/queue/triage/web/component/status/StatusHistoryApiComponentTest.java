package uk.gov.dwp.queue.triage.web.component.status;

import org.junit.Ignore;
import org.junit.Test;
import uk.gov.dwp.queue.triage.core.client.FailedMessageStatus;
import uk.gov.dwp.queue.triage.core.client.status.StatusHistoryResponse;
import uk.gov.dwp.queue.triage.id.FailedMessageId;
import uk.gov.dwp.queue.triage.web.component.WebComponentTest;
import uk.gov.dwp.queue.triage.web.server.api.Constants;
import uk.gov.dwp.queue.triage.web.server.api.status.StatusHistoryListItemMatcher;

import java.time.Instant;

import static org.hamcrest.Matchers.contains;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.FAILED;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.RESENDING;

public class StatusHistoryApiComponentTest extends WebComponentTest<StatusHistoryGivenStage, StatusHistoryWhenStage, StatusHistoryThenStage> {

    private static final FailedMessageId FAILED_MESSAGE_ID_1 = FailedMessageId.newFailedMessageId();
    private static final Instant NOW = Instant.now();

    @Ignore("Requires a change to the Security Model")
    @Test
    public void requestStatusHistoryForFailedMessage() {
        given().aFailedMessageWithId$Has$(
                FAILED_MESSAGE_ID_1,
                new StatusHistoryResponse(FAILED, NOW),
                new StatusHistoryResponse(RESENDING, NOW.plusSeconds(5)));
        when().theStatusHistoryIsRequestedForFailedMessage$(FAILED_MESSAGE_ID_1);
        then().theStatusHistoryResponseForFailedMessage$ContainsAndEntryWith$(FAILED_MESSAGE_ID_1, contains(
                statusHistoryListItem(1, FAILED, NOW),
                statusHistoryListItem(2, RESENDING, NOW.plusSeconds(5))
        ));
    }

    public StatusHistoryListItemMatcher statusHistoryListItem(int recid, FailedMessageStatus status, Instant instant) {
        return StatusHistoryListItemMatcher.statusHistoryListItem(
                String.valueOf(recid),
                status.getDescription(),
                Constants.toIsoDateTimeWithMs(instant)
        );
    }
}
