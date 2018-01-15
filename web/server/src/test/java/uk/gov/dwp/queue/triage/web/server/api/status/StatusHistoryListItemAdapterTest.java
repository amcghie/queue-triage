package uk.gov.dwp.queue.triage.web.server.api.status;

import org.junit.Test;
import uk.gov.dwp.queue.triage.core.client.status.StatusHistoryResponse;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.FAILED;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.RESENDING;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.SENT;
import static uk.gov.dwp.queue.triage.web.server.api.Constants.toIsoDateTimeWithMs;
import static uk.gov.dwp.queue.triage.web.server.api.status.StatusHistoryListItemMatcher.statusHistoryListItem;

public class StatusHistoryListItemAdapterTest {

    private static final Instant NOW = Instant.now();
    private final StatusHistoryListItemAdapter underTest = new StatusHistoryListItemAdapter();

    @Test
    public void adaptAnEmptyList() {
        assertThat(underTest.adapt(Collections.emptyList()), is(empty()));
    }

    @Test
    public void orderOfElementsIsPreserved() {
        assertThat(underTest.adapt(Arrays.asList(
                new StatusHistoryResponse(FAILED, NOW),
                new StatusHistoryResponse(RESENDING, NOW.plusSeconds(1)),
                new StatusHistoryResponse(SENT, NOW.plusSeconds(2))
        )), contains(
                statusHistoryListItem("0", FAILED.getDescription(), toIsoDateTimeWithMs(NOW)),
                statusHistoryListItem("1", RESENDING.getDescription(), toIsoDateTimeWithMs(NOW.plusSeconds(1))),
                statusHistoryListItem("2", SENT.getDescription(), toIsoDateTimeWithMs(NOW.plusSeconds(2))
        )));
    }
}