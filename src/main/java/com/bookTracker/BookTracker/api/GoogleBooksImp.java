package com.bookTracker.BookTracker.api;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class GoogleBooksImp implements GoogleBooks {

	@Override
	public void closeClient() {
		// TODO Auto-generated method stub		
	}

	@Override
	public Optional<List<BookSearch>> searchBook(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Book> bookInfo(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
