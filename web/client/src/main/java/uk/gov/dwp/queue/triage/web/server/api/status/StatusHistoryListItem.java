package uk.gov.dwp.queue.triage.web.server.api.status;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusHistoryListItem {

    @JsonProperty
    private final String recid;
    @JsonProperty
    private final String status;
    @JsonProperty
    private final String effectiveDateTime;

    public StatusHistoryListItem(@JsonProperty("recid") String recid,
                                 @JsonProperty("status") String status,
                                 @JsonProperty("effectiveDateTime") String effectiveDateTime) {
        this.recid = recid;
        this.status = status;
        this.effectiveDateTime = effectiveDateTime;
    }

    public String getRecid() {
        return recid;
    }

    public String getStatus() {
        return status;
    }

    public String getEffectiveDateTime() {
        return effectiveDateTime;
    }
}
