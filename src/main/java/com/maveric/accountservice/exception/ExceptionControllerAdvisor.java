package com.maveric.accountservice.exception;

import com.maveric.accountservice.dto.ErrorDto;
import feign.FeignException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.maveric.accountservice.constants.Constants.*;

@RestControllerAdvice
public class ExceptionControllerAdvisor {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionControllerAdvisor.class);

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleAccountNotFoundException(AccountNotFoundException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(ACCOUNT_NOT_FOUND_CODE);
        errorDto.setMessage(exception.getMessage());
        log.error("{} {}",ACCOUNT_NOT_FOUND_CODE,exception.getMessage());
        return errorDto;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ErrorDto handleCustomerNotFoundException(CustomerNotFoundException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(ACCOUNT_NOT_FOUND_CODE);
        errorDto.setMessage(exception.getMessage());
        log.error("{}->{}",ACCOUNT_NOT_FOUND_CODE,exception.getMessage());
        return errorDto;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(BAD_REQUEST_CODE);
        errorDto.setMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        log.error("{} -> {} -> {}",BAD_REQUEST_CODE,ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),ex.getMessage());
        return errorDto;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorDto handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(METHOD_NOT_ALLOWED_CODE);
        errorDto.setMessage(METHOD_NOT_ALLOWED_MESSAGE);
        log.error("{}-> {}",METHOD_NOT_ALLOWED_CODE,METHOD_NOT_ALLOWED_MESSAGE);
        return errorDto;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(BAD_REQUEST_CODE);
        if(ex.getMessage().contains("com.maveric.accountservice.constants.AccountType")) //NOSONAR
            errorDto.setMessage(INVALID_INPUT_TYPE);
        else
            errorDto.setMessage(HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE);
        log.error("{} ->{}",BAD_REQUEST_CODE,ex.getMessage());
        return errorDto;
    }

    @ExceptionHandler(PathParamsVsInputParamsMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ErrorDto handlePathParamsVsInputParamsMismatchException(PathParamsVsInputParamsMismatchException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(BAD_REQUEST_CODE);
        errorDto.setMessage(exception.getMessage());
        log.error("{}-{}",BAD_REQUEST_CODE,exception.getMessage());
        return errorDto;
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDto handleHttpFeignException(
            FeignException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(SERVICE_UNAVAILABLE_CODE);
        errorDto.setMessage(SERVICE_UNAVAILABLE_MESSAGE);
        log.error("{} -> {}-> {}",SERVICE_UNAVAILABLE_CODE,SERVICE_UNAVAILABLE_MESSAGE,ex.getMessage());
        return errorDto;
    }


    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public final ErrorDto handleUnAuthorizedException(UnAuthorizedException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(AUTH_HEADER_ERROR_CODE);
        errorDto.setMessage(exception.getMessage());
        log.error("{}--{}",AUTH_HEADER_ERROR_CODE,exception.getMessage());
        return errorDto;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ErrorDto handleOtherHttpException(Exception exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(INTERNAL_SERVER_ERROR_CODE);
        errorDto.setMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        log.error("{} - {} - {}",INTERNAL_SERVER_ERROR_CODE,INTERNAL_SERVER_ERROR_MESSAGE,exception.getMessage());
        return errorDto;
    }

}
