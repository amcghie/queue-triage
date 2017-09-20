package uk.gov.dwp.queue.triage.core.resend.spring;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import uk.gov.dwp.queue.triage.core.resend.HistoricStatusPredicate;
import uk.gov.dwp.queue.triage.core.resend.ResendFailedMessageService;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class ResendFailedMessageServiceBeanDefinitionFactory {

    static final String RESEND_FAILED_MESSAGE_SERVICE_BEAN_NAME_PREFIX = "resendFailedMessageService-";

    private final HistoricStatusPredicate historicStatusPredicate;

    public ResendFailedMessageServiceBeanDefinitionFactory(HistoricStatusPredicate historicStatusPredicate) {
        this.historicStatusPredicate = historicStatusPredicate;
    }

    public AbstractBeanDefinition create(String brokerName, String messageSenderBeanName) {
        return genericBeanDefinition(ResendFailedMessageService.class)
                .addConstructorArgValue(brokerName)
                .addConstructorArgReference("failedMessageSearchService")
                .addConstructorArgReference(messageSenderBeanName)
                .addConstructorArgValue(historicStatusPredicate)
                .getBeanDefinition();
    }

    public String createBeanName(String brokerName) {
        return RESEND_FAILED_MESSAGE_SERVICE_BEAN_NAME_PREFIX + brokerName;
    }
}
