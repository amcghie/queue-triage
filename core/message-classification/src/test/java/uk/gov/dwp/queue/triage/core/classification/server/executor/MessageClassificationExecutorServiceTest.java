package uk.gov.dwp.queue.triage.core.classification.server.executor;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.After;
import org.junit.Test;
import uk.gov.dwp.queue.triage.core.classification.server.MessageClassificationService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class MessageClassificationExecutorServiceTest {

    private final MessageClassificationService messageClassificationService = mock(MessageClassificationService.class);
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private MessageClassificationExecutorService underTest = new MessageClassificationExecutorService(
            scheduledExecutorService,
            messageClassificationService,
            0,
            100,
            TimeUnit.MILLISECONDS
    );

    @After
    public void tearDown() throws Exception {
        scheduledExecutorService.shutdownNow();
    }

    @Test
    public void jobExecutesSuccessfully() throws Exception {
        underTest.start();
        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));
        verify(messageClassificationService, timeout(75).times(1)).classifyFailedMessages();
    }

    @Test
    public void jobContinuesToExecuteIfExceptionIsThrown() {
        doThrow(new RuntimeException()).when(messageClassificationService).classifyFailedMessages();

        underTest.start();

        verify(messageClassificationService, timeout(75).times(1)).classifyFailedMessages();

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));

        verify(messageClassificationService, timeout(100).times(2)).classifyFailedMessages();

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));
    }

    @Test
    public void jobCanBeExecutedOnDemand() {
        MessageClassificationExecutorService underTest = new MessageClassificationExecutorService(
                scheduledExecutorService,
                messageClassificationService,
                1,
                1,
                TimeUnit.HOURS
        );
        underTest.start();

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));

        verify(messageClassificationService, timeout(75).times(0)).classifyFailedMessages();

        underTest.execute();

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));

        verify(messageClassificationService, timeout(75).times(1)).classifyFailedMessages();
    }

    @Test
    public void executorCanBePausedAndResumed() {
        underTest.start();

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));
        verify(messageClassificationService, timeout(75).times(1)).classifyFailedMessages();

        underTest.pause();
        assertThat(underTest.getScheduledFuture(), allOf(done(true), cancelled(true)));

        underTest.start();
        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));
        verify(messageClassificationService, timeout(75).times(2)).classifyFailedMessages();
    }

    private TypeSafeDiagnosingMatcher<ScheduledFuture<?>> done(boolean done) {
        return new TypeSafeDiagnosingMatcher<ScheduledFuture<?>>() {
            @Override
            protected boolean matchesSafely(ScheduledFuture<?> scheduledFuture, Description description) {
                boolean matches = scheduledFuture.isDone() == done;
                if (!matches) {
                    description.appendText("is").appendText(done ? "" : "not").appendText(" done");
                }
                return matches;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is done");
            }
        };
    }

    private TypeSafeDiagnosingMatcher<ScheduledFuture<?>> cancelled(boolean cancelled) {
        return new TypeSafeDiagnosingMatcher<ScheduledFuture<?>>() {
            @Override
            protected boolean matchesSafely(ScheduledFuture<?> scheduledFuture, Description description) {
                boolean matches = scheduledFuture.isCancelled() == cancelled;
                if (!matches) {
                    description.appendText("is").appendText(cancelled ? "" : "not").appendText(" cancelled");
                }
                return matches;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is cancelled");
            }
        };
    }
}