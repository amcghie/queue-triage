package uk.gov.dwp.queue.triage.core.client.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

import javax.validation.constraints.NotNull;
import java.util.List;

public class FailedMessageStatusHistoryResponse {

    @JsonProperty
    @NotNull
    private final FailedMessageId failedMessageId;
    @JsonProperty
    private final List<StatusHistoryResponse> statusHistory;

    public FailedMessageStatusHistoryResponse(@JsonProperty("failedMessageId") FailedMessageId failedMessageId,
                                              @JsonProperty("statusHistory") List<StatusHistoryResponse> statusHistory) {
        this.failedMessageId = failedMessageId;
        this.statusHistory = statusHistory;
    }

    public FailedMessageId getFailedMessageId() {
        return failedMessageId;
    }

    public List<StatusHistoryResponse> getStatusHistory() {
        return statusHistory;
    }
}
