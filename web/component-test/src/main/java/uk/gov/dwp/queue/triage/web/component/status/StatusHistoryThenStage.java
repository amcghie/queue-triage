package uk.gov.dwp.queue.triage.web.component.status;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.hamcrest.Matcher;
import org.springframework.http.ResponseEntity;
import uk.gov.dwp.queue.triage.id.FailedMessageId;
import uk.gov.dwp.queue.triage.jgiven.ThenStage;
import uk.gov.dwp.queue.triage.web.server.api.status.StatusHistoryListItem;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JGivenStage
public class StatusHistoryThenStage extends ThenStage<StatusHistoryThenStage> {

    @ProvidedScenarioState
    private ResponseEntity<List<StatusHistoryListItem>> statusHistoryListItemsResponse;

    public StatusHistoryThenStage theStatusHistoryResponseForFailedMessage$ContainsAndEntryWith$(
            FailedMessageId failedMessageId,
            Matcher<Iterable<? extends StatusHistoryListItem>> statusHistoryListItemMatcher) {
        assertThat(statusHistoryListItemsResponse.getStatusCodeValue(), is(200));
        assertThat(statusHistoryListItemsResponse.getBody(), statusHistoryListItemMatcher);
        return this;
    }


}
