package com.bookTracker.BookTracker.exceptions;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookNotFoundExceptionHandler {

    @ExceptionHandler(value = { BookNotFoundException.class })
	public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException e) {
		HttpStatus notFound = HttpStatus.NOT_FOUND;
		BookException bookException = new BookException(e.getMessage(),
														notFound.value(),
														notFound,
														ZonedDateTime.now().toString());

		return new ResponseEntity<>(bookException, notFound);
	}
}
