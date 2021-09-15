package com.bookTracker.BookTracker.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import com.bookTracker.BookTracker.dto.BookSearch;
import com.bookTracker.BookTracker.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
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

	/**
	 * Prepers a list of books from google-books-api
	 * @param response The response from the API call
	 * @return A list with books
	 * @throws JsonMappingException 
	 * @throws JsonProcessingException
	 * @throws IOException	
	 */	
	private List<BookSearch> prepBooksList(Response response) throws JsonMappingException, JsonProcessingException, IOException {
		JsonNode jsonTreeRoot = mapper.readTree(response.body().string());
		JsonNode node = jsonTreeRoot.get("items");
		if (node != null) {
			int size = node.size();
			List<BookSearch> list = Collections.synchronizedList(new ArrayList<BookSearch>());
			CountDownLatch latch = new CountDownLatch(size);
			
			for (int i = 0; i < size; ++i) {				
				JsonNode item = node.get(i);
				Thread thread = new Thread(new ThreadingBookSearch(item, latch, list));
				thread.start();
			}
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return list;
		}
		return null;
	}

	/**
	 * Maps each book from google-books-api into an object
	 * @param item The item containing the books information
	 * @return An optional with the book item if the parameter was valid	
	 */
	private Optional<BookSearch> prepBook(JsonNode item) {
		JsonNode tmp = null;
		BookSearch bookSearch = new BookSearch();
		String selfLink = item.get("selfLink").asText();
		
		if(selfLink != null) {
			tmp = makeRequest(selfLink);
		}
		
		if(tmp != null) {
			bookSearch.setId(tmp.get("id").asText());
			tmp = tmp.get("volumeInfo");
			bookSearch.setTitle(tmp.get("title").asText());

			JsonNode autherNode = tmp.get("authors");
			if(autherNode != null)
				bookSearch.setAuther(autherNode.get(0).asText());
			else
				return Optional.ofNullable(null);
			
			JsonNode imageNode = tmp.get("imageLinks");
			if(imageNode != null && imageNode.get("medium") != null)				
				bookSearch.setCover_img(imageNode.get("medium").asText().replaceFirst("http", "https"));
			else
				return Optional.ofNullable(null);
			return Optional.ofNullable(bookSearch);
		}
		return Optional.ofNullable(null);
	}

	/**
	 * Perform an HTTP request to the given url, 
	 * the expected response is in JSON
	 * @param url The url used for the HTTP request
	 * @return The response	from the HTTP request
	 */	
	private final JsonNode makeRequest(String url) {
		final Request request = new Request.Builder().url(url).get().build();
		Response response = null;
		try {
		    response = client.newCall(request).execute();
			if(response.isSuccessful())
				return mapper.readTree(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return null;
	}
	
	/**
	 * Meant for multithreading the server side rendering of books taken
	 * from the google-books-api 	
	 */
	class ThreadingBookSearch implements Runnable {
		private JsonNode node;
		private CountDownLatch latch;
		private List<BookSearch> list;

		public ThreadingBookSearch(JsonNode node, CountDownLatch latch, List<BookSearch> list) {
			this.node = node;
			this.latch = latch;
			this.list = list;
		}
		
		@Override
		public void run() {
			Optional<BookSearch> optional = prepBook(node);			
			if (optional.isPresent()) {
				list.add(optional.get());
			}			
			latch.countDown();
		}
		
	}
}
