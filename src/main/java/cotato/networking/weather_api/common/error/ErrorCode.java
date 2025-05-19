package cotato.networking.weather_api.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	USER_INPUT_EXCEPTION(HttpStatus.BAD_REQUEST, "C-001", "사용자 입력 오류"),
	USER_ROLE_EXCEPTION(HttpStatus.FORBIDDEN, "C-002", "유저 권한 오류"),
	AUTHENTICATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-003", "공통 권한 에러(필터)"),

	// Server
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부에서 에러가 발생하였습니다."),

	// Auth
	USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "A-001", "이미 존재하는 아이디입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "A-002", "가입하지 않은 유저입니다."),
	PASSWORD_NOT_MATCH(HttpStatus.NOT_FOUND, "A-003", "비밀번호가 일치하지 않습니다."),

	// Weather API
	WEATHER_API_AUTH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "W-001", "날씨 API 키 인증 실패"),
	WEATHER_API_BAD_REQUEST(HttpStatus.BAD_REQUEST, "W-002", "잘못된 날씨 API 요청 파라미터"),
	WEATHER_API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "W-003", "날씨 서비스 오류"),

	// Location API
	LOCATION_NAME_DUPLICATED_EXCEPTION(HttpStatus.CONFLICT, "L-001", "이미 존재하는 위치 이름입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}