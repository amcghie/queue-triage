package uk.gov.dwp.queue.triage.web.server.api.status;

import uk.gov.dwp.queue.triage.core.client.status.StatusHistoryResponse;
import uk.gov.dwp.queue.triage.web.server.api.Constants;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StatusHistoryListItemAdapter {

    public List<StatusHistoryListItem> adapt(List<StatusHistoryResponse> statusHistoryResponses) {
        final AtomicInteger index = new AtomicInteger(0);
        return statusHistoryResponses
                .stream()
                .map(statusHistoryEvent -> new StatusHistoryListItem(
                        Integer.toString(index.getAndIncrement()),
                        statusHistoryEvent.getStatus().getDescription(),
                        Constants.toIsoDateTimeWithMs(statusHistoryEvent.getEffectiveDateTime())))
                .collect(Collectors.toList());
    }
}
