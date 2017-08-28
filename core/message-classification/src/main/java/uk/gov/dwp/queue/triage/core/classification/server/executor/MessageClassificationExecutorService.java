package uk.gov.dwp.queue.triage.core.classification.server.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.queue.triage.core.classification.server.MessageClassificationService;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Path("/admin/executor/message-classification")
public class MessageClassificationExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageClassificationExecutorService.class);

    private final ScheduledExecutorService scheduledExecutorService;
    private final Runnable runnable;
    private final long initialDelay;
    private final long executionFrequency;
    private final TimeUnit timeUnit;

    private ScheduledFuture<?> futureTask;

    public MessageClassificationExecutorService(ScheduledExecutorService scheduledExecutorService,
                                                MessageClassificationService messageClassificationService,
                                                long initialDelay,
                                                long executionFrequency,
                                                TimeUnit timeUnit) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.runnable = () -> {
            try {
                LOGGER.debug("Executing the Classify Failed Messages Job");
                messageClassificationService.classifyFailedMessages();
            } catch (Throwable t) {
                LOGGER.error("An error occurred classifying FailedMessages", t);
            }
        };
        this.initialDelay = initialDelay;
        this.executionFrequency = executionFrequency;
        this.timeUnit = timeUnit;
    }

    @POST
    @Path("/start")
    public void start() {
        LOGGER.info("MessageClassificationService scheduled to start in {} {} and then execute every {} {}",
                initialDelay,
                timeUnit,
                executionFrequency,
                timeUnit
        );
        scheduleAtAFixedRate(initialDelay);
    }

    @POST
    @Path("/execute")
    public void execute() {
        cancelFutureTask();
        LOGGER.info("Executing the MessageClassificationService immediately and then scheduling to execute every {} {}",
                executionFrequency,
                timeUnit
        );
        scheduleAtAFixedRate(0);
    }

    @PUT
    @Path("/pause")
    public void pause() {
        LOGGER.info("Pausing execution of the MessageClassificationService");
        cancelFutureTask();
        LOGGER.info("Execution of the MessageClassificationService paused");
    }

    private void cancelFutureTask() {
        if (futureTask != null && !futureTask.isCancelled()) {
            futureTask.cancel(true);
        }
    }

    public void stop() {
        LOGGER.info("Stopping execution of the MessageClassificationService");
        scheduledExecutorService.shutdown();
        LOGGER.info("Execution of the MessageClassificationService stopped");
    }

    private void scheduleAtAFixedRate(long initialDelay) {
        futureTask = this.scheduledExecutorService.scheduleAtFixedRate(
                runnable,
                initialDelay,
                executionFrequency,
                timeUnit
        );
    }
}
