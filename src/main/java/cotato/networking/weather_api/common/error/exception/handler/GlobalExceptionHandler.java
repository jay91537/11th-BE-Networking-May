package cotato.networking.weather_api.common.error.exception.handler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> handleAppException(AppException e) {
		log.error("AppException 발생: errorCode={}, message={}", e.getErrorCode().getCode(), e.getMessage());

		ErrorResponse errorResponse = ErrorResponse.from(e.getErrorCode());

		return ResponseEntity
			.status(e.getErrorCode().getStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("처리되지 않은 예외 발생: ", e);

		ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(errorResponse);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatusCode status,
		WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String requestURI = httpServletRequest.getRequestURI();

		List<String> messages = ex.getBindingResult().getFieldErrors().stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();

		log.error("MethodArgumentNotValidException 발생: requestURI={}, error={}", requestURI, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(ErrorCode.USER_INPUT_EXCEPTION, messages));
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex, HttpHeaders headers,
		HttpStatusCode status, WebRequest request) {

		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String requestURI = httpServletRequest.getRequestURI();

		String message = String.format("필수 파라미터 '%s'이(가) 누락되었습니다.", ex.getParameterName());
		List<String> messages = Collections.singletonList(message);

		log.error("MissingServletRequestParameterException 발생: requestURI={}, error={}", requestURI, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(ErrorCode.USER_INPUT_EXCEPTION, messages));
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String requestURI = httpServletRequest.getRequestURI();

		String supportedMethods = "";
		if (ex.getSupportedHttpMethods() != null) {
			supportedMethods = ex.getSupportedHttpMethods().stream()
				.map(HttpMethod::name)
				.collect(Collectors.joining(", "));
		}

		String message = String.format(
			"요청 메소드 '%s'는 지원되지 않습니다. 지원되는 메소드: %s",
			ex.getMethod(), supportedMethods);
		List<String> messages = Collections.singletonList(message);

		log.error("HttpRequestMethodNotSupportedException 발생: requestURI={}, error={}", requestURI, ex.getMessage());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(ErrorResponse.of(ErrorCode.USER_INPUT_EXCEPTION, messages));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(
		ConstraintViolationException ex, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String requestURI = httpServletRequest.getRequestURI();

		List<String> messages = ex.getConstraintViolations().stream()
			.map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
			.toList();

		log.error("ConstraintViolationException 발생: requestURI={}, error={}", requestURI, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of(ErrorCode.USER_INPUT_EXCEPTION, messages));
	}
}