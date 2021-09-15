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
import okhttp3.Request;
import okhttp3.Response;

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

	/**
	 * Searches for books based on the given search query
	 * @param query The search query
	 * @return List of books matching the search query	
	 */	
	@Override
	public Optional<List<BookSearch>> searchBook(String query) {
	    if(query.equals(""))
			return Optional.ofNullable(null);
		
		Optional<List<BookSearch>> optional = Optional.ofNullable(null);
		final Request request = prepRequest(query);

		Response response = null;

		try{
			response = client.newCall(request).execute();
	
			if(response.isSuccessful()) {
				List<BookSearch> books = prepBooksList(response);
				optional = Optional.ofNullable(books);
			}
			return optional;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(response != null)
				response.close();
		}
		
		return optional;
	}

	@Override
	public Optional<Book> bookInfo(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Prepers the request to the API
	 * @param query The search query 
	 * @return The prepered request object	
	 */	
	private final Request prepRequest(String query) {
		final Request request = new Request.Builder()
			.url("https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + API_KEY)
			.get()
			.build();
		return request;
	}
}
