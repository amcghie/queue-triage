package uk.gov.dwp.queue.triage.core.search.mongo;

import com.mongodb.DBObject;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import uk.gov.dwp.queue.triage.core.client.FailedMessageStatus;

import java.util.regex.Pattern;

import static com.mongodb.QueryOperators.IN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static uk.gov.dwp.queue.triage.core.client.search.SearchFailedMessageRequest.newSearchFailedMessageRequest;
import static uk.gov.dwp.queue.triage.core.dao.mongo.DBObjectMatcher.hasField;

public class MongoSearchRequestAdapterTest {

    private final MongoSearchRequestAdapter underTest = new MongoSearchRequestAdapter();

    @Test
    public void searchRequestWithAllParametersSpecified() {
        final DBObject dbObject = underTest.toQuery(newSearchFailedMessageRequest()
                .withBroker("broker-name")
                .withDestination("mars")
                .withStatus(FailedMessageStatus.FAILED)
                .withStatus(FailedMessageStatus.RESENDING)
                .withStatus(FailedMessageStatus.SENT)
                .withContent("id")
                .build()
        );
        assertThat(dbObject, Matchers.allOf(
                hasField("destination.brokerName", equalTo("broker-name")),
                hasField("statusHistory.0.status", hasField(IN, hasItems("FAILED", "CLASSIFIED", "RESEND", "SENT"))),
                hasField("destination.name", equalTo("mars")),
                hasField("content", allOf(willMatch("user_id"), willMatch("id"), willMatch("hidden"), not(willMatch("find"))))
        ));
    }

    private TypeSafeMatcher<Pattern> willMatch(final String input) {
        return new TypeSafeMatcher<Pattern>() {
            @Override
            protected boolean matchesSafely(Pattern pattern) {
                return pattern.matcher(input).find();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected the pattern to match ").appendText(input);
            }
        };
    }

    @Test
    public void searchRequestWithoutDestinationAndBrokerAndDefaultStatus() throws Exception {
        final DBObject dbObject = underTest.toQuery(newSearchFailedMessageRequest().build());

        assertThat(dbObject, Matchers.allOf(
                hasField("statusHistory.0.status", hasField("$ne", equalTo("DELETED")))
        ));
    }
}