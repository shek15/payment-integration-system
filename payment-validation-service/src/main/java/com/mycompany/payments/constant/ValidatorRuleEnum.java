package com.mycompany.payments.constant;

import java.util.Optional;

import com.mycompany.payments.service.impl.businessvalidatorimpl.ValidatorRule1;
import com.mycompany.payments.service.impl.businessvalidatorimpl.ValidatorRule2;
import com.mycompany.payments.service.impl.businessvalidatorimpl.ValidatorRule3;
import com.mycompany.payments.service.interfaces.BusinessValidator;

import lombok.Getter;

@Getter
public enum ValidatorRuleEnum {
    VALIDATION_RULE_1("VALIDATION_RULE_1", ValidatorRule1.class),
    VALIDATION_RULE_2("VALIDATION_RULE_2", ValidatorRule2.class), 
    VALIDATION_RULE_3("VALIDATION_RULE_3",ValidatorRule3.class);

    private final String ruleName;
    private final Class<? extends BusinessValidator> validatorClass;

    ValidatorRuleEnum(String ruleName, Class<? extends BusinessValidator> validatorClass) {
        this.ruleName = ruleName;
        this.validatorClass = validatorClass;
    }

    public static Optional<Class<? extends BusinessValidator>> getValidatorClassByRuleName(String ruleName) {
        if (Optional.class == null)
            return Optional.empty();
        
        for (ValidatorRuleEnum ruleEnum : ValidatorRuleEnum.values()) {
            if (ruleEnum.ruleName.equals(ruleName)) {
                return Optional.of(ruleEnum.getValidatorClass());
            }
        }
        return Optional.empty();
    }
}
