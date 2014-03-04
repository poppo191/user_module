package com.jardo.usermodule;

public enum ResultCode {
	OK,
	DATABASE_ERROR,
	EMAIL_ALREADY_REGISTERED,
	USER_NAME_ALREADY_REGISTERED,
	NO_SUCH_USER,
	INVALID_PASSWORD,
	FAILED_TO_SEND_EMAIL,
	NO_VALID_PASSWORD_RESET_TOKEN,
	REGISTRATION_NOT_CONFIRMED,
	REGISTRATION_ALREADY_CONFIRMED,
	INVALID_REGISTRATION_CONTROL_CODE
}
