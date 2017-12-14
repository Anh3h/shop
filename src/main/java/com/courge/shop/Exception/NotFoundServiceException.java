package com.courge.shop.Exception;

import java.text.MessageFormat;

public class NotFoundServiceException extends RuntimeException {

    private ServiceError serviceError;

    public NotFoundServiceException( ServiceError serviceError ) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }

    public static NotFoundServiceException create( String message, Object... args ) {
        return new NotFoundServiceException(new ServiceError(404, MessageFormat.format(message, args)));
    }

}