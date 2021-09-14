package com.bookTracker.BookTracker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Connects to external database and has CRUD access over the books table
 * @author Georgios Davakos
 * @since 2021-09-14 
 */
@Repository
public interface BooksRepository extends CrudRepository<Book, Integer> {
	
}
