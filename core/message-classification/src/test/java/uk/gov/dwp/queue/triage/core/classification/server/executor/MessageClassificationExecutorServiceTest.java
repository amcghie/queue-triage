package uk.gov.dwp.queue.triage.core.classification.server.executor;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageClassificationExecutorServiceTest.class);

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
        LoggerFactory.getLogger(MessageClassificationExecutorServiceTest.class).debug("Verifying Message Classification Service");
        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));
        verifyMessageClassificationServiceExecutions(1, 75);
    }


    @Test
    public void jobContinuesToExecuteIfExceptionIsThrown() {
        doThrow(new RuntimeException()).when(messageClassificationService).classifyFailedMessages();

        underTest.start();

        verifyMessageClassificationServiceExecutions(1, 75);

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));

        verifyMessageClassificationServiceExecutions(2, 120);

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

        verifyMessageClassificationServiceExecutions(0, 75);

        underTest.execute();

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));

        verifyMessageClassificationServiceExecutions(1, 75);
    }

    @Test
    public void executorCanBePausedAndResumed() {
        underTest.start();

        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));
        verifyMessageClassificationServiceExecutions(1, 75);

        underTest.pause();
        assertThat(underTest.getScheduledFuture(), allOf(done(true), cancelled(true)));

        underTest.start();
        assertThat(underTest.getScheduledFuture(), allOf(done(false), cancelled(false)));
        verifyMessageClassificationServiceExecutions(2, 75);
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

    private void verifyMessageClassificationServiceExecutions(int times, int timeoutInMillis) {
        LOGGER.debug("Verifying messageClassificationService has been called {} times", times);
        verify(messageClassificationService, timeout(timeoutInMillis).times(times)).classifyFailedMessages();
    }
}