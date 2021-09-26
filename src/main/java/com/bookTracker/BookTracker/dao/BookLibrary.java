package com.bookTracker.BookTracker.dao;

import java.util.List;
import java.util.Optional;

import com.bookTracker.BookTracker.model.Book;

import org.springframework.stereotype.Service;

/**
 * Handles all communications to the library database
 * @author Georgios Davakos
 * @since 2021-09-24
 */
@Service
public interface BookLibrary {

	/**
	 * Retrieves all the entries from the database
	 * @return All the books	
	 */
	public List<Book> getAllEntries();

	/**
	 * Retrieves a single book
	 * @param id The id of the book
	 * @return The book	
	 */	
	public Optional<Book> getBook(int id);
	
	/**
	 * Creates a book in the database
	 * @param entry The book
	 */
	public void createBook(Book entry);

	/**
	 * Updates a book in the database
	 * @param entry The updated book
	 */	
	public void updateBook(Book entry);

	/**
	 * Deletes a book from the database
	 * @param id The id of the book	
	 */
	public void deleteBook(Integer id);

	/**
	 * Provides an available ID for the book
	 * @return The available id	
	 */
	public int availableID();
}
