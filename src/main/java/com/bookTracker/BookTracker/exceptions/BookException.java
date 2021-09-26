package com.bookTracker.BookTracker.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * An object that represents {@link BookNotFoundExceptionHandler}
 */
@Data
@AllArgsConstructor
public class BookException {
	private final String message;
	private final int errorCode;
	private final HttpStatus errorType;
	private final String dateTime;
}
