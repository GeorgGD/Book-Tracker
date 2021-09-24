package com.bookTracker.BookTracker.dao;

import java.util.List;

import com.bookTracker.BookTracker.model.Book;

import org.springframework.stereotype.Service;

@Service
public interface BookLibrary {

	public List<Book> getAllEntries();

}
