package com.mycompany.payments.constant;

import java.util.Optional;

import com.mycompany.payments.service.impl.businessvalidatorimpl.DuplicateTxnValidator;
import com.mycompany.payments.service.interfaces.BusinessValidator;

import lombok.Getter;

@Getter
public enum ValidatorRuleEnum {
    DUPLICATE_TXN_VALIDATOR("DUPLICATE_TXN_VALIDATOR", DuplicateTxnValidator.class);

    private final String ruleName;
    private final Class<? extends BusinessValidator> validatorClass;

    ValidatorRuleEnum(String ruleName, Class<? extends BusinessValidator> validatorClass) {
        this.ruleName = ruleName;
        this.validatorClass = validatorClass;
    }

    public static Optional<Class<? extends BusinessValidator>> getValidatorClassByRuleName(String ruleName) {
        if (ruleName == null)
            return Optional.empty();
        
        for (ValidatorRuleEnum ruleEnum : ValidatorRuleEnum.values()) {
            if (ruleEnum.ruleName.equals(ruleName)) {
                return Optional.of(ruleEnum.getValidatorClass());
            }
        }
        return Optional.empty();
    }
}
