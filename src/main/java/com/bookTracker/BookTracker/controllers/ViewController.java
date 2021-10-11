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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/** 
 * This class is meant to simulate a frontend application, this implies that the class
 * is used to serve pages to the user while also communicating with the backend
 * endpoints.
 * @author Georgios Davakos
 * @since 2021-10-10  
 */
@Controller
public class ViewController {

	@Autowired
	private BookLibrary bookLibrary;

	@Autowired
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

	@GetMapping("/")
	public String root(Model model) {
		List<Book> books = bookLibrary.getAllEntries();
		model.addAttribute("books", books);
    	return "index";
	}
	
	/**
	 * Searching for a book based on the given full-text search	
	 * @param query The full-text search query
	 * @return A list of books that were found	
	 */
	@RequestMapping("/tempSearchBook")
	public String searchForBook(@RequestParam("query") String query, Model model) {
		query = query.replace(" ", "+");
		Optional<List<BookSearch>> optional = googleBooks.searchBook(query);

		if(optional.isPresent()) {
			model.addAttribute("searchResult", optional.get());
			List<Book> books = bookLibrary.getAllEntries();
			model.addAttribute("books", books);
			return "withBooks";
		} 
			
		model.addAttribute("searchResult", new ArrayList<BookSearch>());
		return "withBooks";
	}

	/**
	 * Adds a book into book tracker database with \"to read\" status
	 * @param id The Google Books API id of the book	
	 */
	@RequestMapping("/tempToRead")
	@ResponseStatus(code = HttpStatus.OK)
	public String addBookToRead(@RequestParam("id") String id, Model model) {
	    saveBookIntoDatabase(id, false, null);

		List<Book> books = bookLibrary.getAllEntries();
		model.addAttribute("books", books);
    	return "index";
	}

	/**
	 * Adds a book into book tracker database with \"reading\" status
	 * @param id The Google Books API id of the book
	 */
	@RequestMapping("/tempReading")
	@ResponseStatus(code = HttpStatus.OK)
	public String addBookReading(@RequestParam("id") String id, Model model) {
		saveBookIntoDatabase(id, true, null);

		List<Book> books = bookLibrary.getAllEntries();
		model.addAttribute("books", books);
    	return "index";
	}

	/**
	 * Adds a book int obook tracker database with \"completed\" status
	 * @param id The Google Books API id of the book
	 */
	@RequestMapping("/tempCompleted")
	@ResponseStatus(code = HttpStatus.OK)
	public String addBookCompleted(@RequestParam("id") String id, Model model) {
		saveBookIntoDatabase(id, false, new Date(System.currentTimeMillis()));

		List<Book> books = bookLibrary.getAllEntries();
		model.addAttribute("books", books);
    	return "index";
	}

	/**
	 * Removes book with the given id from the book tracker database
	 * @param id The id of the book	
	 */
	@RequestMapping("/tempDelete")
	@ResponseStatus(code = HttpStatus.OK)
	public String removeBook(@RequestParam("id") int id, Model model) {
		bookLibrary.deleteBook(id);

		List<Book> books = bookLibrary.getAllEntries();
		model.addAttribute("books", books);
    	return "index";
	}

	/**
	 * Changes the status of a book to \"to read\"
	 * @param id The id of the book	
	 */
	@RequestMapping("/tempUpdateToRead")
	@ResponseStatus(code = HttpStatus.OK)
	public String updateBookToRead(@RequestParam("id") int id, Model model) {
		Optional<Book> optional = bookLibrary.getBook(id);
		if(optional.isPresent()) {
			Book book = optional.get();
			book.setReading(false);
			book.setCompleted_date(null);
			
			bookLibrary.updateBook(book);

			List<Book> books = bookLibrary.getAllEntries();
			model.addAttribute("books", books);
			return "index";
		} else {
			throw new BookNotFoundException(id);			
		}
	}

	/**
	 * Changes the status of a book to \"reading\"
	 * @param id The id of the book	
	 */
	@RequestMapping("/tempUpdateReading")
	@ResponseStatus(code = HttpStatus.OK)
	public String updateBookToReading(@RequestParam("id") int id, Model model) {
		Optional<Book> optional = bookLibrary.getBook(id);
		if(optional.isPresent()) {
			Book book = optional.get();
			book.setReading(true);
			book.setCompleted_date(null);
			
			bookLibrary.updateBook(book);

			List<Book> books = bookLibrary.getAllEntries();
			model.addAttribute("books", books);
			return "index";
		} else {
			throw new BookNotFoundException(id);
		}
	}

	/**
	 * Changes the status of a book to \"completed\"
	 * @param id The id of the book	
	 */
	@RequestMapping("/tempUpdateCompleted")
	@ResponseStatus(code = HttpStatus.OK)
	public String updateBookToCompleted(@RequestParam("id") int id, Model model) {
	    Optional<Book> optional = bookLibrary.getBook(id);
		if (optional.isPresent()) {
			Book book = optional.get();
			book.setReading(false);
			book.setCompleted_date(new Date(System.currentTimeMillis()));

			bookLibrary.updateBook(book);

			List<Book> books = bookLibrary.getAllEntries();
			model.addAttribute("books", books);
			return "index";
		} else {
			throw new BookNotFoundException(id);
		}
	}

	/**
	 * Provides a list of all books currently in the database
	 * @return A list of all the books in the database
	 */
	@RequestMapping("/tempCurrentBooks")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Book> booksInDatabase() {
	    return bookLibrary.getAllEntries();
	}
}
