package uk.gov.dwp.queue.triage.web.server.api.status;

import uk.gov.dwp.queue.triage.core.client.status.FailedMessageStatusHistoryClient;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("api/failed-messages/status-history/{failedMessageId}")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class StatusHistoryResource {

    private final FailedMessageStatusHistoryClient failedMessageStatusHistoryClient;
    private final StatusHistoryListItemAdapter statusHistoryListItemAdapter;

    public StatusHistoryResource(FailedMessageStatusHistoryClient failedMessageStatusHistoryClient,
                                 StatusHistoryListItemAdapter statusHistoryListItemAdapter) {
        this.failedMessageStatusHistoryClient = failedMessageStatusHistoryClient;
        this.statusHistoryListItemAdapter = statusHistoryListItemAdapter;
    }

    @GET
    public List<StatusHistoryListItem> statusHistory(@PathParam("failedMessageId") FailedMessageId failedMessageId) {
        return statusHistoryListItemAdapter.adapt(failedMessageStatusHistoryClient.getStatusHistory(failedMessageId));
    }
}
