package com.paypal.mocca.client;

import feign.FeignException;
import feign.codec.EncodeException;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MoccaExceptionHandlerTest {

    @Test
    public void errorTest() {
        Error error = new OutOfMemoryError("too much to remember");
        assertEquals(MoccaExceptionHandler.handleException(error), error);
    }

    @Test
    public void constraintViolationExceptionTest() {
        ConstraintViolationException constraintViolationException = new ConstraintViolationException(Collections.emptySet());
        FeignException feignException = new EncodeException("Encode exception", constraintViolationException);
        assertEquals(MoccaExceptionHandler.handleException(feignException), constraintViolationException);
    }

    @Test
    public void moccaExceptionTest() {
        MoccaException moccaException = new MoccaException("Mocca must be hot");
        FeignException feignException = new EncodeException("encode exception", moccaException);
        assertEquals(MoccaExceptionHandler.handleException(feignException), moccaException);
    }

    @Test
    public void feignExceptionNoCauseTest() {
        FeignException feignException = new EncodeException("encode exception wihout mocca");
        Throwable throwable = MoccaExceptionHandler.handleException(feignException);
        assertTrue(throwable instanceof MoccaException);
        assertEquals(throwable.getCause(), feignException);
    }

    @Test
    public void feignExceptionWithCauseTest() {
        FeignException feignException = new EncodeException("encode exception", new RuntimeException("foo"));
        Throwable throwable = MoccaExceptionHandler.handleException(feignException);
        assertTrue(throwable instanceof MoccaException);
        assertEquals(throwable.getCause(), feignException);
    }

    @Test
    public void nonFeignRuntimeExceptionNoCauseTest() {
        RuntimeException runtimeException = new RuntimeException("runtime exception");
        Throwable throwable = MoccaExceptionHandler.handleException(runtimeException);
        assertTrue(throwable instanceof MoccaException);
        assertEquals(throwable.getCause(), runtimeException);
    }

}