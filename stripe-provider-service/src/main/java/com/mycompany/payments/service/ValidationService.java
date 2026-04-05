package com.mycompany.payments.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.exception.StripeProviderException;
import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.pojo.LineItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidationService {

    private static final Pattern URL_PATTERN =
            Pattern.compile("^(https?)://[^/\\s]+\\.[^/\\s]+(:\\d+)?(/.*)?$", Pattern.CASE_INSENSITIVE);

    public void validate(CreatePaymentReq req) {
        log.debug("Validating create payment request");

        if (req == null) {
            throwEx(ErrorCodeEnum.REQUEST_BODY_NULL);
        }

        validateUrl(req.getSuccessUrl(),
                ErrorCodeEnum.SUCCESS_URL_MISSING,
                ErrorCodeEnum.SUCCESS_URL_INVALID,
                ErrorCodeEnum.SUCCESS_URL_PATTERN_MISMATCH);

        validateUrl(req.getCancelUrl(),
                ErrorCodeEnum.CANCEL_URL_MISSING,
                ErrorCodeEnum.CANCEL_URL_INVALID,
                ErrorCodeEnum.CANCEL_URL_PATTERN_MISMATCH);

        if (CollectionUtils.isEmpty(req.getLineItems())) {
            throwEx(ErrorCodeEnum.LINE_ITEMS_MISSING);
        }

        int index = 0;
        for (LineItem item : req.getLineItems()) {
            validateLineItem(item, ++index);
        }

        log.debug("Create payment request validation completed for {} line items", req.getLineItems().size());
    }

    // -------------------- URL VALIDATION --------------------
    private void validateUrl(String url,
                             ErrorCodeEnum missing,
                             ErrorCodeEnum invalid,
                             ErrorCodeEnum patternMismatch) {

        if (!StringUtils.hasText(url)) {
            throwEx(missing);
        }

        if (!isValidUrl(url)) {
            throwEx(invalid);
        }

        if (!URL_PATTERN.matcher(url).matches()) {
            throwEx(patternMismatch);
        }
    }

    // -------------------- LINE ITEM VALIDATION --------------------
    private void validateLineItem(LineItem li, int index) {

        if (li == null) {
            throwEx(ErrorCodeEnum.LINE_ITEM_NULL, index);
        }

        if (!StringUtils.hasText(li.getCurrency())) {
            throwEx(ErrorCodeEnum.CURRENCY_MISSING, index);
        }

        if (!li.getCurrency().matches("[A-Za-z]{3}")) {
            throwEx(ErrorCodeEnum.CURRENCY_INVALID, index);
        }

        if (!StringUtils.hasText(li.getProductName())) {
            throwEx(ErrorCodeEnum.PRODUCT_NAME_MISSING, index);
        }

        if (li.getUnitAmount() <= 0) {
            throwEx(ErrorCodeEnum.UNIT_AMOUNT_INVALID, index);
        }

        if (li.getQuantity() <= 0) {
            throwEx(ErrorCodeEnum.QUANTITY_INVALID, index);
        }
    }

    // -------------------- COMMON METHODS --------------------
    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            return scheme != null &&
                    (scheme.equalsIgnoreCase("http") 
                    || scheme.equalsIgnoreCase("https")) 
                    &&
                    uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private void throwEx(ErrorCodeEnum e) {
        log.warn("Validation failed with error code {} and message '{}'", e.getErrorCode(), e.getMessage());
        throw new StripeProviderException(
                HttpStatus.BAD_REQUEST,
                e.getErrorCode(),
                e.getMessage()
        );
    }

    private void throwEx(ErrorCodeEnum e, int index) {
        log.warn("Validation failed with error code {} and message '{}'", e.getErrorCode(), e.getMessage(index));
        throw new StripeProviderException(
                HttpStatus.BAD_REQUEST,
                e.getErrorCode(),
                e.getMessage(index)
        );
    }
}
