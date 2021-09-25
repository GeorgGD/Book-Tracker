package com.bookTracker.BookTracker.controllers;

import java.sql.Date;
import java.util.Optional;

import com.bookTracker.BookTracker.api.GoogleBooks;
import com.bookTracker.BookTracker.dao.BookLibrary;
import com.bookTracker.BookTracker.model.Book;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

/**
 * The following controller manages a book tracking system
 * were a user can save books they have read, are reading or want 
 * to read in the future
 * @author Georgios Davakos
 * @since 2021-09-25
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class BookLibraryController {

	private BookLibrary bookLibrary;
	private GoogleBooks googleBooks;

	/**
	 * Saves a book from Google Books API into the database 
	 * @param id The Google Books API id for the book
	 * @param reading True if you are reading the book
	 * @param date The Date you finished reading the book
	 */
	private void saveBookIntoDatabase(String id, boolean reading, Date date) {
		Optional<Book> optional = googleBooks.bookInfo(id);

		if (optional.isPresent()) {
			Book book = optional.get();
			book.setReading(reading);
			book.setCompleted_date(date);
			book.setId(bookLibrary.availableID());

			bookLibrary.createBook(book);
		}
	}
}
