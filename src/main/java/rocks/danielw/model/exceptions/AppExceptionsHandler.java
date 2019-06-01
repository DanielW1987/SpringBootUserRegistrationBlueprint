package rocks.danielw.model.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import rocks.danielw.web.dto.response.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all Exception that can occur while using the REST API of this application.
 */
@ControllerAdvice
public class AppExceptionsHandler {

  private static final Logger LOG = LoggerFactory.getLogger(AppExceptionsHandler.class);

  @ExceptionHandler(value = ValidationException.class)
  public ResponseEntity<ErrorResponse> handleBadRequests(ValidationException exception, WebRequest webRequest) {
    LOG.warn(webRequest.getContextPath() + ": " + exception.getMessage());
    return new ResponseEntity<>(createErrorResponse(exception), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(UserAlreadyExistsException exception, WebRequest webRequest) {
    LOG.warn(webRequest.getContextPath() + ": " + exception.getMessage());
    return new ResponseEntity<>(createErrorResponse(exception), new HttpHeaders(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = {ResourceNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException exception, WebRequest webRequest) {
    LOG.warn(webRequest.getContextPath() + ": " + exception.getMessage());
    return new ResponseEntity<>(createErrorResponse(exception), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception exception, WebRequest webRequest) {
    LOG.error(webRequest.getContextPath() + ": " + exception.getMessage(), exception);
    return new ResponseEntity<>(createErrorResponse(exception), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse createErrorResponse(Throwable exception) {
    return new ErrorResponse(exception.getMessage());
  }

  private ErrorResponse createErrorResponse(ValidationException exception) {
    List<String> messages = new ArrayList<>();
    messages.add(exception.getMessage());

    for (FieldError error : exception.getFieldErrors()) {
      messages.add(error.getField() + ": " + error.getDefaultMessage());
    }

    return new ErrorResponse(messages);
  }
	
}
