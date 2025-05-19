package cotato.networking.weather_api.common.error.exception;

import cotato.networking.weather_api.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

	private final ErrorCode errorCode;

	public AppException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}