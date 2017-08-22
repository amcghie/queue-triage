package uk.gov.dwp.migration.mongo.demo.cxf.configuration;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import uk.gov.dwp.migration.mongo.demo.cxf.client.CxfConfiguration;
import uk.gov.dwp.migration.mongo.demo.cxf.client.ProviderRegistry;
import uk.gov.dwp.migration.mongo.demo.cxf.client.ResourceRegistry;

import java.util.List;

@Configuration
@Import({
        CxfConfiguration.class,
})
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
@EnableConfigurationProperties(CxfProperties.class)
public class CxfBusConfiguration {

    @Bean
    public ServletRegistrationBean cxfServlet() {
        ServletRegistrationBean cxfServlet = new ServletRegistrationBean(new CXFServlet(), "/*");
        cxfServlet.addInitParameter("static-resources-list", "/static/.+");
        return cxfServlet;
    }

    @Bean
    public Server server(Bus bus,
                         List<Feature> features,
                         ProviderRegistry providerRegistry,
                         ResourceRegistry resourceRegistry,
                         CxfProperties cxfProperties) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setServiceBeans(resourceRegistry.getResources());
        endpoint.setAddress(cxfProperties.getContextPath());
        endpoint.setProviders(providerRegistry.getProviders());
        endpoint.setBus(bus);
        endpoint.setFeatures(features);
        return endpoint.create();
    }

    @Bean
    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }
}
