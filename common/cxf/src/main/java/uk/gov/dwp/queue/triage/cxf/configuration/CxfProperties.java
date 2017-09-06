package uk.gov.dwp.queue.triage.cxf.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        prefix = "cxf",
        ignoreUnknownFields = false
)
public class CxfProperties {

    private String contextPath;

    public String getContextPath() {
        return contextPath;
    }

    public CxfProperties setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }
}
