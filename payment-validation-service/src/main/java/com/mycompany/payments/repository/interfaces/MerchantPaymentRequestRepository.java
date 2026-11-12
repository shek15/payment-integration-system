package com.mycompany.payments.repository.interfaces;

import com.mycompany.payments.entity.MerchantPaymentRequestEntity;

public interface MerchantPaymentRequestRepository {

    public int saveMerchantPaymentRequest(
        MerchantPaymentRequestEntity merchantPaymentRequestEntity
    );
    
}
