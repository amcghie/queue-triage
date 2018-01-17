package uk.gov.dwp.queue.triage.core.resource.status;

import uk.gov.dwp.queue.triage.core.client.FailedMessageStatus;
import uk.gov.dwp.queue.triage.core.client.status.StatusHistoryResponse;
import uk.gov.dwp.queue.triage.core.domain.StatusHistoryEvent;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dwp.queue.triage.core.domain.FailedMessageStatusAdapter.toFailedMessageStatus;

public class StatusHistoryResponseAdapter {

    public List<StatusHistoryResponse> toStatusHistoryResponses(List<StatusHistoryEvent> statusHistory) {
        final List<StatusHistoryResponse> statusHistoryResponses = new ArrayList<>();
        for (int i=0; i<statusHistory.size(); i++) {
            final FailedMessageStatus failedMessageStatus = toFailedMessageStatus(statusHistory.get(i).getStatus());
            if (i+1<statusHistory.size() && failedMessageStatus.equals(toFailedMessageStatus(statusHistory.get(i+1).getStatus()))) {
                i++;
            }
            statusHistoryResponses.add(new StatusHistoryResponse(failedMessageStatus, statusHistory.get(i).getEffectiveDateTime()));
        }
        return statusHistoryResponses;
    }

}
