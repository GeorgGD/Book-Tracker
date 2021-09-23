package com.bookTracker.BookTracker.api;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.bookTracker.BookTracker.dto.BookSearch;
import com.bookTracker.BookTracker.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SpringBootTest
public class GoogleBooksImpTest {

	private static GoogleBooksImp googleBooksImp;
	private static OkHttpClient client;
	private static Call call;

	private static void setupClient() throws IOException {
		client = mock(OkHttpClient.class);		
		when(client.newCall(any(Request.class))).thenReturn(call);
		when(client.cache()).thenReturn(new Cache(null, 10));
	}

	private static void setupCall() throws IOException {
		call = mock(Call.class);
		Response response = setupSearchBookResponse("bookSearchJSON.txt", new Request.Builder().url("http://www.notneededurl.com").get().build());
		Response secondResponse = setupSearchBookResponse("selfLinkBookJSON.txt", new Request.Builder().url("http://www.notneededurl.com").get().build());
		
		when(call.execute()).thenReturn(response, secondResponse);
	}
	
	private static Response setupSearchBookResponse(String fileName, Request request) {
		Response response = new Response.Builder()
			.request(request)
			.protocol(Protocol.HTTP_1_0)
			.code(200)
			.message("")
			.body(ResponseBody.create(MediaType.get("application/json; charset=utf-8"), readJsonFile(fileName)))
			.build();
		return response;
	}

	private static String readJsonFile(String fileName) {
		fileName = System.getProperty("user.dir") + "/target/test-classes/" + fileName;
		
		File file = new File(fileName);
		String line;
		String text = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null) {
				text += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	@BeforeAll
    public static void createGoogleBooksImp() {
		try {
			setupCall();
			setupClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
		googleBooksImp = new GoogleBooksImp(client, new ObjectMapper());
	}

	@Test
	public void closeClient_ClosesClient_ClosingCache() {
		googleBooksImp.closeClient();
		verify(client, times(1)).cache();
	}

	@Test
	@Order(1)
	public void searchBook_SearchForBook_FindBook() throws IOException {
		int expectedSize = 1;
		String expectedBookTitle = "Courage Is Calling";
		String expectedBookAuther = "Ryan Holiday";
		
		Optional<List<BookSearch>> optional = googleBooksImp.searchBook("courage+is+calling");

		verify(call, times(2)).execute();
		
		if(optional.isEmpty())
			fail("No book was returned after a search when a book should have been returned");

		List<BookSearch> listOfRetrievedBooks = optional.get();
		assertEquals(expectedSize, listOfRetrievedBooks.size());

		BookSearch bookSearch = listOfRetrievedBooks.get(0);
		assertEquals(expectedBookTitle, bookSearch.getTitle());
		assertEquals(expectedBookAuther, bookSearch.getAuther());
		assertNotNull(bookSearch.getCoverImg());
	}

	@Test
	@Order(2)
	public void bookInfo_SearchForSpecificBook_FindBook() {
		String nonsenseArg = "hi";
		String expectedBookName = "Courage Is Calling";
		String expectedBookAuther = "Ryan Holiday";

		Optional<Book> optional = googleBooksImp.bookInfo(nonsenseArg);

		if(optional.isEmpty())
			fail("Book was not found");

		Book book = optional.get();
		assertEquals(expectedBookName, book.getName());
		assertEquals(expectedBookAuther, book.getAuther());	    
	}
}
