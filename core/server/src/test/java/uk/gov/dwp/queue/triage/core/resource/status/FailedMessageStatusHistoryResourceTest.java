package uk.gov.dwp.queue.triage.core.resource.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.dwp.queue.triage.core.client.status.FailedMessageStatusHistoryResponse;
import uk.gov.dwp.queue.triage.core.client.status.StatusHistoryResponse;
import uk.gov.dwp.queue.triage.core.dao.FailedMessageDao;
import uk.gov.dwp.queue.triage.core.domain.StatusHistoryEvent;
import uk.gov.dwp.queue.triage.core.domain.status.FailedMessageStatusHistoryResponseMatcher;
import uk.gov.dwp.queue.triage.id.FailedMessageId;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.queue.triage.core.client.FailedMessageStatus.FAILED;
import static uk.gov.dwp.queue.triage.core.domain.status.FailedMessageStatusHistoryResponseMatcher.failedMessageStatusHistoryResponse;
import static uk.gov.dwp.queue.triage.core.domain.status.StatusHistoryResponseMatcher.statusHistoryResponse;

public class FailedMessageStatusHistoryResourceTest {

    private static final FailedMessageId FAILED_MESSAGE_ID_1 = FailedMessageId.newFailedMessageId();
    private static final FailedMessageId FAILED_MESSAGE_ID_2 = FailedMessageId.newFailedMessageId();
    private static final Instant NOW = Instant.now();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private FailedMessageDao failedMessageDao;
    @Mock
    private List<StatusHistoryEvent> statusHistoryEvents1;
    @Mock
    private List<StatusHistoryEvent> statusHistoryEvents2;
    @Mock
    private StatusHistoryResponseAdapter statusHistoryResponseAdapter;
    @Mock
    private List<StatusHistoryResponse> statusHistoryResponses1;
    @Mock
    private List<StatusHistoryResponse> statusHistoryResponses2;

    private FailedMessageStatusHistoryResource underTest;

    @Before
    public void setUp() {
        underTest = new FailedMessageStatusHistoryResource(failedMessageDao, statusHistoryResponseAdapter);
    }

    @Test
    public void getStatusHistoryForASingleFailedMessageId() {
        when(failedMessageDao.getStatusHistory(FAILED_MESSAGE_ID_1)).thenReturn(statusHistoryEvents1);
        when(statusHistoryResponseAdapter.toStatusHistoryResponses(statusHistoryEvents1)).thenReturn(statusHistoryResponses1);

        final List<StatusHistoryResponse> statusHistory = underTest.getStatusHistory(FAILED_MESSAGE_ID_1);

        assertThat(statusHistory, equalTo(statusHistoryResponses1));
    }

    @Test
    public void getStatusHistoryForAMultipleFailedMessageIds() {
        when(failedMessageDao.getStatusHistory(FAILED_MESSAGE_ID_1)).thenReturn(statusHistoryEvents1);
        when(statusHistoryResponseAdapter.toStatusHistoryResponses(statusHistoryEvents1)).thenReturn(statusHistoryResponses1);
        when(failedMessageDao.getStatusHistory(FAILED_MESSAGE_ID_2)).thenReturn(statusHistoryEvents2);
        when(statusHistoryResponseAdapter.toStatusHistoryResponses(statusHistoryEvents2)).thenReturn(statusHistoryResponses2);

        final List<FailedMessageStatusHistoryResponse> statusHistory = underTest.getStatusHistory(Arrays.asList(FAILED_MESSAGE_ID_1, FAILED_MESSAGE_ID_2));

        assertThat(statusHistory, contains(
                failedMessageStatusHistoryResponse().withFailedMessageId(FAILED_MESSAGE_ID_1).withStatusHistory(Matchers.equalTo(statusHistoryResponses1)),
                failedMessageStatusHistoryResponse().withFailedMessageId(FAILED_MESSAGE_ID_2).withStatusHistory(Matchers.equalTo(statusHistoryResponses2))
        ));
    }
}