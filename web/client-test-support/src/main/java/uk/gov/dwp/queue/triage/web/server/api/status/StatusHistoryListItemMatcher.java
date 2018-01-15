package uk.gov.dwp.queue.triage.web.server.api.status;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.equalTo;

public class StatusHistoryListItemMatcher extends TypeSafeDiagnosingMatcher<StatusHistoryListItem> {

        private final Matcher<String> recid;
        private final Matcher<String> status;
        private final Matcher<String> effectiveDateTime;

        private StatusHistoryListItemMatcher(Matcher<String> recid,
                                             Matcher<String> status,
                                             Matcher<String> effectiveDateTime) {
            this.recid = recid;
            this.status = status;
            this.effectiveDateTime = effectiveDateTime;
        }

        public static StatusHistoryListItemMatcher statusHistoryListItem(String recid,
                                                                         String status,
                                                                         String effectiveDateTime) {
            return new StatusHistoryListItemMatcher(
                    equalTo(recid),
                    equalTo(status),
                    equalTo(effectiveDateTime)
            );
        }

        @Override
        protected boolean matchesSafely(StatusHistoryListItem statusHistoryListItem, Description description) {
            return recid.matches(statusHistoryListItem.getRecid()) &&
                    status.matches(statusHistoryListItem.getStatus()) &&
                    effectiveDateTime.matches(statusHistoryListItem.getEffectiveDateTime());
        }

        @Override
        public void describeTo(Description description) {

        }
    }