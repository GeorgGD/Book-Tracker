package com.bookTracker.BookTracker.exceptions;

/**
 * Throw this exception when a book is not found from either
 * Google Books API or book tracker database
 */
public class BookNotFoundException extends RuntimeException {

	public BookNotFoundException(String query) {
		super("No book was found after using the following query: " + query);
	}
}
