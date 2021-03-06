package com.bookTracker.BookTracker.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookTracker.BookTracker.api.GoogleBooks;
import com.bookTracker.BookTracker.dao.BookLibrary;
import com.bookTracker.BookTracker.dto.BookSearch;
import com.bookTracker.BookTracker.exceptions.BookNotFoundException;
import com.bookTracker.BookTracker.model.Book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * The following controller manages a book tracking system
 * were a user can save books they have read, are reading or want 
 * to read in the future
 * @author Georgios Davakos
 * @since 2021-09-25
 */
@RequiredArgsConstructor
@RestController
public class BookLibraryController {

	private final BookLibrary bookLibrary;
	private final GoogleBooks googleBooks;

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

	/**
	 * Searching for a book based on the given full-text search	
	 * @param query The full-text search query
	 * @return A list of books that were found	
	 */
	@RequestMapping("/searchBook")
	public List<BookSearch> searchForBook(@RequestParam("query") String query) {
		query = query.replace(" ", "+");
		Optional<List<BookSearch>> optional = googleBooks.searchBook(query);

		if(optional.isPresent()) 
			return optional.get();
		
		return new ArrayList<BookSearch>();
	}

	/**
	 * Adds a book into book tracker database with \"to read\" status
	 * @param id The Google Books API id of the book	
	 */
	@RequestMapping("/toRead")
	@ResponseStatus(code = HttpStatus.OK)
	public void addBookToRead(@RequestParam("id") String id) {
	    saveBookIntoDatabase(id, false, null);
	}

	/**
	 * Adds a book into book tracker database with \"reading\" status
	 * @param id The Google Books API id of the book
	 */
	@RequestMapping("/reading")
	@ResponseStatus(code = HttpStatus.OK)
	public void addBookReading(@RequestParam("id") String id) {
		saveBookIntoDatabase(id, true, null);
	}

	/**
	 * Adds a book int obook tracker database with \"completed\" status
	 * @param id The Google Books API id of the book
	 */
	@RequestMapping("/completed")
	@ResponseStatus(code = HttpStatus.OK)
	public void addBookCompleted(@RequestParam("id") String id) {
		saveBookIntoDatabase(id, false, new Date(System.currentTimeMillis()));
	}

	/**
	 * Removes book with the given id from the book tracker database
	 * @param id The id of the book	
	 */
	@RequestMapping("/delete")
	@ResponseStatus(code = HttpStatus.OK)
	public void removeBook(@RequestParam("id") int id) {
		bookLibrary.deleteBook(id);
	}

	/**
	 * Changes the status of a book to \"to read\"
	 * @param id The id of the book	
	 */
	@RequestMapping("/updateToRead")
	@ResponseStatus(code = HttpStatus.OK)
	public void updateBookToRead(@RequestParam("id") int id) {
		Optional<Book> optional = bookLibrary.getBook(id);
		if(optional.isPresent()) {
			Book book = optional.get();
			book.setReading(false);
			book.setCompleted_date(null);
			
			bookLibrary.updateBook(book);
		} else {
			throw new BookNotFoundException(id);
		}
	}

	/**
	 * Changes the status of a book to \"reading\"
	 * @param id The id of the book	
	 */
	@RequestMapping("/updateReading")
	@ResponseStatus(code = HttpStatus.OK)
	public void updateBookToReading(@RequestParam("id") int id) {
		Optional<Book> optional = bookLibrary.getBook(id);
		if(optional.isPresent()) {
			Book book = optional.get();
			book.setReading(true);
			book.setCompleted_date(null);
			
			bookLibrary.updateBook(book);
		} else {
			throw new BookNotFoundException(id);
		}
	}

	/**
	 * Changes the status of a book to \"completed\"
	 * @param id The id of the book	
	 */
	@RequestMapping("/updateCompleted")
	@ResponseStatus(code = HttpStatus.OK)
	public void updateBookToCompleted(@RequestParam("id") int id) {
	    Optional<Book> optional = bookLibrary.getBook(id);
		if (optional.isPresent()) {
			Book book = optional.get();
			book.setReading(false);
			book.setCompleted_date(new Date(System.currentTimeMillis()));

			bookLibrary.updateBook(book);
		} else {
			throw new BookNotFoundException(id);
		}
	}

	/**
	 * Provides a list of all books currently in the database
	 * @return A list of all the books in the database
	 */
	@RequestMapping("/currentBooks")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Book> booksInDatabase() {
	    return bookLibrary.getAllEntries();
	}
}
