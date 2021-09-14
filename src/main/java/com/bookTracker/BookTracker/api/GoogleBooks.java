package com.bookTracker.BookTracker.api;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * A google books api interface for book searching
 * @auther Georgios Davakos
 * @since 2021-09-14
 */
@Component
public interface GoogleBooks {

	/**
	 * Closes the client used for api calls
	 */	
	public void closeClient();

	/**
	 * Searches for books based on the given search query
	 * @param query The search query
	 * @return Optional list of books matching the search query	
	 */		
	public Optional<List<BookSearch>> searchBook(String query);

	/**
	 * Collects information about a given book
	 * @param bookID The is of the book
	 * @return The book if the given book id exists	
	 */
	public Optional<Book> bookInfo(String bookId);
}
