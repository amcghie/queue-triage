package uk.gov.dwp.queue.triage.core.resource.status;

import uk.gov.dwp.queue.triage.core.client.status.FailedMessageStatusHistoryClient;
import uk.gov.dwp.queue.triage.core.client.status.FailedMessageStatusHistoryResponse;
import uk.gov.dwp.queue.triage.core.client.status.StatusHistoryResponse;
import uk.gov.dwp.queue.triage.core.dao.FailedMessageDao;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

import java.util.List;
import java.util.stream.Collectors;

public class FailedMessageStatusHistoryResource implements FailedMessageStatusHistoryClient {

    private final FailedMessageDao failedMessageDao;
    private final StatusHistoryResponseAdapter statusHistoryResponseAdapter;

    public FailedMessageStatusHistoryResource(FailedMessageDao failedMessageDao,
                                              StatusHistoryResponseAdapter statusHistoryResponseAdapter) {
        this.failedMessageDao = failedMessageDao;
        this.statusHistoryResponseAdapter = statusHistoryResponseAdapter;
    }

    @Override
    public List<StatusHistoryResponse> getStatusHistory(FailedMessageId failedMessageId) {
        return statusHistoryResponseAdapter.toStatusHistoryResponses(
                failedMessageDao.getStatusHistory(failedMessageId)
        );
    }

    @Override
    public List<FailedMessageStatusHistoryResponse> getStatusHistory(List<FailedMessageId> failedMessageIds) {
        // We may want to introduce a "bulk" call to the DAO.
        return failedMessageIds
                .stream()
                .map(failedMessageId -> new FailedMessageStatusHistoryResponse(failedMessageId, getStatusHistory(failedMessageId)))
                .collect(Collectors.toList());
    }
}
