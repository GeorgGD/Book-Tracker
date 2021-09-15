package com.bookTracker.BookTracker.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.bookTracker.BookTracker.dto.BookSearch;
import com.bookTracker.BookTracker.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;

/**
 * A google books api class that searches for books
 * @auther Georgios Davakos
 * @since 2021-09-14
 */
@Component
public class GoogleBooksImp implements GoogleBooks {

	@Value("${google.api.key: no-key}")
	static private String API_KEY;

	@Autowired
	private OkHttpClient client;

	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public void closeClient() {
		try {
			if(client != null)
				client.cache().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
