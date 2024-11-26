package com.bookin.bookin.global.apiPayload.code.exception.handler;


import com.bookin.bookin.global.apiPayload.code.BaseErrorCode;
import com.bookin.bookin.global.apiPayload.code.exception.GeneralException;

public class CustomHandler extends GeneralException {

    public CustomHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}