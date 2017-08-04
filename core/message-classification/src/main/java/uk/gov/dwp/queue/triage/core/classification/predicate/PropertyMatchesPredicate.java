package uk.gov.dwp.queue.triage.core.classification.predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import uk.gov.dwp.queue.triage.core.domain.FailedMessage;

import java.util.Optional;
import java.util.regex.Pattern;

public class PropertyMatchesPredicate implements FailedMessagePredicate {

    @JsonProperty
    private final String name;
    @JsonProperty
    private final String regex;
    @JsonIgnore
    private final Pattern pattern;

    public PropertyMatchesPredicate(@JsonProperty("name") String name,
                                    @JsonProperty("regex") String regex) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (StringUtils.isBlank(regex)) {
            throw new IllegalArgumentException("regex cannot be null");
        }
        this.name = name;
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean test(FailedMessage failedMessage) {
        return pattern
                .matcher(getPropertyAsString(failedMessage))
                .matches();
    }

    public String getPropertyAsString(FailedMessage failedMessage) {
        // TODO: Should we only support Objects or type Number, CharSequence, Boolean
        return Optional.ofNullable(failedMessage.getProperty(name))
                .map(String::valueOf)
                .orElse("");
    }
}
