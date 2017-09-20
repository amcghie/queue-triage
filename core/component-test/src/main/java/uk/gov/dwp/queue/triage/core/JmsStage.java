package uk.gov.dwp.queue.triage.core;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import uk.gov.dwp.queue.triage.core.classification.MessageClassifier;
import uk.gov.dwp.queue.triage.core.classification.action.FailedMessageAction;
import uk.gov.dwp.queue.triage.core.classification.predicate.ContentEqualToPredicate;
import uk.gov.dwp.queue.triage.core.stub.app.resource.StubMessageClassifierResource;

import static org.slf4j.LoggerFactory.getLogger;

@JGivenStage
public class JmsStage extends Stage<JmsStage> {

    private static final Logger LOGGER = getLogger("DoNothingMessageAction");
    @Autowired
    private JmsTemplate dummyAppJmsTemplate;
    @Autowired
    private StubMessageClassifierResource stubMessageClassifierResource;

    public JmsStage aMessageWithContent$WillDeadLetter(String content) {
        addMessageClassifierWithContent(
                content,
                failedMessage -> {
                    throw new RuntimeException("Content is " + failedMessage.getContent());
                }
        );
        return this;
    }

    public JmsStage aMessageWithContent$WillBeConsumedSuccessfully(String content) {
        addMessageClassifierWithContent(
                content,
                failedMessage -> LOGGER.debug("Received Message with content: {}", content)
        );
        return this;
    }

    public void addMessageClassifierWithContent(String content, FailedMessageAction failedMessageAction) {
        stubMessageClassifierResource.addMessageClassifier(MessageClassifier
                .when(new ContentEqualToPredicate(content))
                .then(failedMessageAction)
        );
    }

    public JmsStage aMessageWithContent$IsSentTo$OnBroker$(String content, String destination, String brokerName) {
        dummyAppJmsTemplate.send(destination, session -> session.createTextMessage(content));
        return this;
    }
}
