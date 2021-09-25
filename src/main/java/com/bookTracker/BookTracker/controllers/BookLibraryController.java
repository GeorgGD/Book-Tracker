package com.bookTracker.BookTracker.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import com.bookTracker.BookTracker.api.GoogleBooks;
import com.bookTracker.BookTracker.dao.BookLibrary;
import com.bookTracker.BookTracker.dto.BookSearch;
import com.bookTracker.BookTracker.exceptions.BookNotFoundException;
import com.bookTracker.BookTracker.model.Book;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;

/**
 * The following controller manages a book tracking system
 * were a user can save books they have read, are reading or want 
 * to read in the future
 * @author Georgios Davakos
 * @since 2021-09-25
 */
@RequiredArgsConstructor
@Controller
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

		if(optional.isPresent()) {
			Book book = optional.get();
			book.setReading(reading);
			book.setCompleted_date(date);
			book.setId(bookLibrary.availableID());

			bookLibrary.createBook(book);
		}
	}

	@RequestMapping("/searchBook")
	public List<BookSearch> searchForBook(@RequestParam("query") String query) {
		query = query.replace(" ", "+");
		Optional<List<BookSearch>> optional = googleBooks.searchBook(query);

		if(optional.isPresent()) 
			return optional.get();
		

	   throw new BookNotFoundException(query);
	}
}
