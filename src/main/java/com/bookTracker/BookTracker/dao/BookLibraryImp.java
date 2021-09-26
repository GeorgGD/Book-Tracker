package com.bookTracker.BookTracker.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookTracker.BookTracker.model.Book;
import com.bookTracker.BookTracker.repository.BooksRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Handles all communications to the library database
 * @author Georgios Davakos
 * @since 2021-09-24
 */
@RequiredArgsConstructor
@Service
public class BookLibraryImp implements BookLibrary {

	private final BooksRepository bookRepo;
	
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
		Iterable<Book> books = bookRepo.findAll();
		Book prevBook = null;
		int diff = 0;
		int counter = 0;

		for (Book book : books) {
			if (prevBook != null) {
				diff = book.getId() - prevBook.getId();
				if (diff > 1) {
					int idNotUsed = prevBook.getId() + 1;
					return idNotUsed;
				}
			}

			prevBook = book;
			counter++;
		}

		int idNotUsed = counter + 1;
		return 0;
	}

	/**
	 * Retrieves a single book
	 * @param id The id of the book
	 * @return The book	
	 */	
	@Override
	public Optional<Book> getBook(int id) {		
		return bookRepo.findById(id);
	}
	
}
