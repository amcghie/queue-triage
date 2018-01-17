package uk.gov.dwp.queue.triage.core.domain.status;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsAnything;
import uk.gov.dwp.queue.triage.core.client.status.FailedMessageStatusHistoryResponse;
import uk.gov.dwp.queue.triage.core.client.status.StatusHistoryResponse;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

public class FailedMessageStatusHistoryResponseMatcher extends TypeSafeDiagnosingMatcher<FailedMessageStatusHistoryResponse> {

    private Matcher<FailedMessageId> failedMessageIdMatcher = Matchers.notNullValue(FailedMessageId.class);
    private Matcher<Iterable<? super StatusHistoryResponse>> statusHistoryMatcher = new IsAnything<>();

    private FailedMessageStatusHistoryResponseMatcher() {

    }

    public static FailedMessageStatusHistoryResponseMatcher failedMessageStatusHistoryResponse() {
        return new FailedMessageStatusHistoryResponseMatcher();
    }

    public FailedMessageStatusHistoryResponseMatcher withFailedMessageId(FailedMessageId failedMessageId) {
        this.failedMessageIdMatcher = Matchers.equalTo(failedMessageId);
        return this;
    }

    public FailedMessageStatusHistoryResponseMatcher withStatusHistory(Matcher<Iterable<? super StatusHistoryResponse>> statusHistoryMatcher) {
        this.statusHistoryMatcher = statusHistoryMatcher;
        return this;
    }

    @Override
    protected boolean matchesSafely(FailedMessageStatusHistoryResponse actual, Description description) {
        description
                .appendText("failedMessageId=").appendValue(actual.getFailedMessageId())
                .appendText(", statusHistory=").appendValue(actual.getStatusHistory());
        return failedMessageIdMatcher.matches(actual.getFailedMessageId()) &&
                statusHistoryMatcher.matches(actual.getStatusHistory());
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("failedMessageId is ").appendDescriptionOf(failedMessageIdMatcher)
                .appendText(", statusHistory is ").appendDescriptionOf(statusHistoryMatcher);
    }
}
