package com.bookTracker.BookTracker.api;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public interface GoogleBooks {

	public void closeClient();

	public Optional<List<BookSearch>> searchBook(String query);

	public Optional<Book> bookInfo(String bookId);
}
