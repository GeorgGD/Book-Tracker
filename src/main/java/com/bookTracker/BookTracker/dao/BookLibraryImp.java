package com.bookTracker.BookTracker.dao;

import java.util.List;

import com.bookTracker.BookTracker.model.Book;

import org.springframework.stereotype.Service;

/**
 * Handles all communications to the library database
 * @author Georgios Davakos
 * @since 2021-09-24
 */
@Service
public class BookLibraryImp implements BookLibrary {

	/**
	 * Retrieves all the entries from the database
	 * @return All the books	
	 */
	@Override
	public List<Book> getAllEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Creates a book in the database
	 * @param entry The book
	 */
	@Override
	public void createBook(Book entry) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Updates a book in the database
	 * @param entry The updated book
	 */	
	@Override
	public void updateBook(Book entry) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Deletes a book from the database
	 * @param id The id of the book	
	 */
	@Override
	public void deleteBook(Integer id) {
		// TODO Auto-generated method stub
		
	}

   	/**
	 * Provides an available ID for the book
	 * @return The available id	
	 */
	@Override
	public int availableID() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
