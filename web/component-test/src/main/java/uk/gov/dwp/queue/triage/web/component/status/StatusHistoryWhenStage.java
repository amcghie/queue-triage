package uk.gov.dwp.queue.triage.web.component.status;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import uk.gov.dwp.queue.triage.id.FailedMessageId;
import uk.gov.dwp.queue.triage.jgiven.WhenStage;
import uk.gov.dwp.queue.triage.web.server.api.status.StatusHistoryListItem;

import java.util.Collections;
import java.util.List;

@JGivenStage
public class StatusHistoryWhenStage extends WhenStage<StatusHistoryWhenStage> {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @ExpectedScenarioState
    private ResponseEntity<List<StatusHistoryListItem>> statusHistoryListItemsResponse;

    public StatusHistoryWhenStage theStatusHistoryIsRequestedForFailedMessage$(FailedMessageId failedMessageId) {
        statusHistoryListItemsResponse = testRestTemplate
                .withBasicAuth("bobbuilder", "fixit")
                .exchange(
                "/web/api/failed-messages/status-history/{failedMessageId}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<StatusHistoryListItem>>() {},
                Collections.singletonMap("failedMessageId", failedMessageId));
        return self();
    }
}
