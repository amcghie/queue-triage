package uk.gov.dwp.queue.triage.web.server.search;

import uk.gov.dwp.queue.triage.core.client.search.SearchFailedMessageResponse;
import uk.gov.dwp.queue.triage.web.server.api.Constants;
import uk.gov.dwp.queue.triage.web.server.list.FailedMessageListItem;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FailedMessageListItemAdapter {

    public List<FailedMessageListItem> adapt(Collection<SearchFailedMessageResponse> failedMessages) {
        return failedMessages
                .stream()
                .map(fm -> new FailedMessageListItem(
                        fm.getFailedMessageId().getId().toString(),
                        Optional.ofNullable(fm.getContent()).map(content -> content.replace("\"", "\\\"")).orElse(null),
                        fm.getBroker(),
                        fm.getDestination().orElse(null),
                        Constants.toIsoDateTimeWithMs(fm.getSentDateTime()),
                        Constants.toIsoDateTimeWithMs(fm.getLastFailedDateTime()),
                        Optional.ofNullable(fm.getLabels())
                                .map(labels -> labels.stream().collect(Collectors.joining(", ")))
                                .orElse(null)))
                .collect(Collectors.toList());
    }
}
