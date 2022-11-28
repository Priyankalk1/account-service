package com.maveric.accountservice.exception;

import com.maveric.accountservice.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ExceptionControllerAdvisorTest {

    private ExceptionControllerAdvisor controllerAdvisor = new ExceptionControllerAdvisor();

    @Test
    void handleAccountNotFoundException() {
        AccountNotFoundException exception = new AccountNotFoundException("Account Not found");
        ErrorDto error = controllerAdvisor.handleAccountNotFoundException(exception);
        assertEquals("404",error.getCode());
    }

    @Test
    void handleCustomerNotFoundException() {
        CustomerNotFoundException exception = new CustomerNotFoundException("Customer Not found");
        ErrorDto error = controllerAdvisor.handleCustomerNotFoundException(exception);
        assertEquals("404",error.getCode());
    }

    @Test
    void handlePathParamsVsInputParamsMismatchException() {
        PathParamsVsInputParamsMismatchException exception = new PathParamsVsInputParamsMismatchException("Input Not valid");
        ErrorDto error = controllerAdvisor.handlePathParamsVsInputParamsMismatchException(exception);
        assertEquals("400",error.getCode());
    }

    @Test
    void handleHttpRequestMethodNotSupportedException() {
        MethodParameter methodParameter = mock(MethodParameter.class);
        BindingResult bindingResult = mock(BindingResult.class);
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("error");;
        ErrorDto error = controllerAdvisor.handleHttpRequestMethodNotSupportedException(exception);
        assertEquals("405",error.getCode());
    }

    @Test
    void handleHttpMessageNotReadableException()
    {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Exception");
        ErrorDto error = controllerAdvisor.handleHttpMessageNotReadableException(exception);
        assertEquals("400",error.getCode());
    }

    @Test
    void handleUnAuthorizedException()
    {
        UnAuthorizedException exception = new UnAuthorizedException("UnAuthorized exception");
        ErrorDto error = controllerAdvisor.handleUnAuthorizedException(exception);
        assertEquals("401",error.getCode());
    }

    @Test
    void handleOtherHttpException()
    {
        Exception exception = new Exception();
        ErrorDto error = controllerAdvisor.handleOtherHttpException(exception);
        assertEquals("500",error.getCode());
    }
}