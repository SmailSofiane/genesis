package be.test.genesis.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class genesisExceptionHandler {

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<?> handelDataAccessError(DataAccessException maex, HttpServletRequest httpServletRequest) {
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimastamp(new Date().getTime());

		errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String requestPath = (String) httpServletRequest.getAttribute("javax.servlet.error.request_uri");
		if (requestPath == null) {
			requestPath = httpServletRequest.getRequestURI();
		}
		errorDetail.setPath(requestPath);
		errorDetail.setTitle("Internal error");
		errorDetail.setDetail("Internal error:somthing wrong when access Repository");
		errorDetail.setDevelopperMessage(maex.getClass().getName());
		return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handelResourceNotFoundException(ResourceNotFoundException rnex, HttpServletRequest httpServletRequest) {
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimastamp(new Date().getTime());

		errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
		String requestPath = (String) httpServletRequest.getAttribute("javax.servlet.error.request_uri");
		if (requestPath == null) {
			requestPath = httpServletRequest.getRequestURI();
		}
		errorDetail.setPath(requestPath);
		errorDetail.setTitle("Resource Not Found");
		errorDetail.setDetail(rnex.getMessage());
		errorDetail.setDevelopperMessage(rnex.getClass().getName());
		return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handelArgumentNotValidException(MethodArgumentNotValidException maex,
			HttpServletRequest httpServletRequest) {
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimastamp(new Date().getTime());

		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
		String requestPath = (String) httpServletRequest.getAttribute("javax.servlet.error.request_uri");
		if (requestPath == null) {
			requestPath = httpServletRequest.getRequestURI();
		}
		errorDetail.setPath(requestPath);
		errorDetail.setTitle("Validation Failed");
		errorDetail.setDetail("Input validation Failed "); 		 
		errorDetail.setDevelopperMessage(maex.getClass().getName());
		List<FieldError> listFieldError = maex.getBindingResult().getFieldErrors();
		List<ObjectError> listobjectError = maex.getBindingResult().getAllErrors();
		for (ObjectError objectError : listobjectError) {
			 
			errorDetail.setDetail(String.format("Input validation Failed in %s :%s ", objectError.getObjectName(),objectError.getDefaultMessage()));
		}
		for (FieldError fieldError : listFieldError) {
			List<ValidationError> listValidationError = errorDetail.getErrors().get(fieldError.getField());
			if (Objects.isNull(listValidationError)) {
				listValidationError = new ArrayList<>();
				errorDetail.getErrors().put(fieldError.getField(), listValidationError);
			}
			ValidationError validationError = new ValidationError();
			validationError.setCode(fieldError.getCode());
			validationError.setMessage(fieldError.getDefaultMessage());
			listValidationError.add(validationError);

		}
		return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
	}

	

}
