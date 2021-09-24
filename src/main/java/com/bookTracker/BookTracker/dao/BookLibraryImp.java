package com.bookTracker.BookTracker.dao;

import java.util.ArrayList;
import java.util.List;

import com.bookTracker.BookTracker.model.Book;
import com.bookTracker.BookTracker.repository.BooksRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles all communications to the library database
 * @author Georgios Davakos
 * @since 2021-09-24
 */
@Service
public class BookLibraryImp implements BookLibrary {

	@Autowired
	private BooksRepository bookRepo;
	
	/**
	 * Retrieves all the entries from the database
	 * @return All the books	
	 */
	@Override
	public List<Book> getAllEntries() {
		List<Book> books = new ArrayList<>();
		try {
			bookRepo.findAll().forEach(books::add);
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return books;
	}

	/**
	 * Creates a book in the database
	 * @param entry The book
	 */
	@Override
	public void createBook(Book entry) {
	    try {
			bookRepo.save(entry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates a book in the database
	 * @param entry The updated book
	 */	
	@Override
	public void updateBook(Book entry) {
	    createBook(entry);
	}

	/**
	 * Deletes a book from the database
	 * @param id The id of the book	
	 */
	@Override
	public void deleteBook(Integer id) {
	    try {
			bookRepo.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
