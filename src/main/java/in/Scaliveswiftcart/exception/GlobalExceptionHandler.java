package in.Scaliveswiftcart.exception;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice

public class GlobalExceptionHandler {
   
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFindException(ResourceNotFoundException ex, HttpServletRequest request){
		 
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error("Not_found")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request){
		 
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.error("Conflict")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientStockException(DuplicateResourceException ex, HttpServletRequest request){
		 
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Insuffcient Stock")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EmptyCartException.class)
	public ResponseEntity<ErrorResponse> handleEmptyCartException(EmptyCartException ex, HttpServletRequest request){
		 
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Empty Cart")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidOprationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidOprationException(InvalidOprationException ex, HttpServletRequest request){
		 
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Invalid Opration")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
		Map<String, String> validationErrors = new HashMap<>();
		 for(FieldError error: ex.getBindingResult().getFieldErrors()) {
			 validationErrors.put(error.getField(), error.getDefaultMessage());
		 }
		
		ErrorResponse error = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Validation Fail")
				.message("Input Validation Failed please check the error")
				.path(request.getRequestURI())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request){
		Class c = ex.getRequiredType();
		String message = String.format("Invalid value '%s' for the parameter '%s' Expected Type '%s'", ex.getValue(), ex.getName(), c!=null?c.getSimpleName():"Unknow");
				ErrorResponse error = ErrorResponse.builder()
						.timeStamp(LocalDateTime.now())
						.status(HttpStatus.BAD_REQUEST.value())
						.error("Type Mismatch")
						.message(message)
						.path(request.getRequestURI())
						.build();
				
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);	
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request){
		ErrorResponse error = ErrorResponse.builder()
						.timeStamp(LocalDateTime.now())
						.status(HttpStatus.BAD_REQUEST.value())
						.error("Wrong Argument")
						.message(ex.getMessage())
						.path(request.getRequestURI())
						.build();				
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);	
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request){
		ErrorResponse error = ErrorResponse.builder()
						.timeStamp(LocalDateTime.now())
						.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.error("Internal Server Error")
						.message(ex.getMessage())
						.path(request.getRequestURI())
						.build();				
				return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);	
	}
		
	
}
