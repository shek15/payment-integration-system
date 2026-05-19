package com.mycompany.payments.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.mycompany.payments.constant.ValidatorRuleEnum;
import com.mycompany.payments.pojo.PaymentRequest;
import com.mycompany.payments.service.interfaces.BusinessValidator;
import com.mycompany.payments.service.interfaces.PaymentService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    @Value("${businessValidatorRules}")
    private String validationRules;

    private final ApplicationContext applicationContext;

    @Override
    public String validateAndCreatePayment(PaymentRequest paymentRequest) {
        String[] rules = validationRules.split(",");
        for (String rule : rules) {
            Optional<Class<? extends BusinessValidator>> validatorClass = ValidatorRuleEnum.getValidatorClassByRuleName(rule);

            if (!validatorClass.isPresent()) {
                log.warn("Validator class {} not found", rule);
                continue;
            }

            BusinessValidator validator = applicationContext.getBean(validatorClass.get());

            if (validator == null) {
                log.warn("No bean found for Validator Class: {}", validatorClass.get().getName());
                continue;
            }

            validator.validate(paymentRequest);
        }
        log.info("Successfully passed all the validator rules...");
        return "Payment Request created";
    }

    @PostConstruct
    void init() {
        log.info("Validation Rules: {}", validationRules);
    }

}
