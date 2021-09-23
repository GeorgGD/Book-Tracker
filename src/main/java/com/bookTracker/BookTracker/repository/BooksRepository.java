package com.bookTracker.BookTracker.repository;

import com.bookTracker.BookTracker.model.Book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Connects to external database and has CRUD access over the library table
 * @author Georgios Davakos
 * @since 2021-09-14 
 */
@Repository
public interface BooksRepository extends CrudRepository<Book, Integer> {
	
}
