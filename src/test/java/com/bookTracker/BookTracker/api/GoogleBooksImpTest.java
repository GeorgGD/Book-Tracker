package com.bookTracker.BookTracker.api;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GoogleBooksImpTest {

	private GoogleBooksImp googleBooksImp;
	private OkHttpClient client;

	private void setupClient() throws IOException {
		client = mock(OkHttpClient.class);
		String url = "https://www.googleapis.com/books/v1/volumes?q=courage+is+calling&key=no-key";
		Request bookSearch = setupSearchBookRequest(url);
		Response response = setupSearchBookResponse("bookSearchJSON.txt");
		when(client.newCall(bookSearch).execute()).thenReturn(response);
	}

	private Request setupSearchBookRequest(String url) {
		Request request = new Request.Builder()
			.url(url)
			.get()
			.build();
		return request;
	}
	
	private Response setupSearchBookResponse(String fileName) {
		Response response = new Response.Builder()
			.body(ResponseBody.create(MediaType.get("application/json"), readJsonFile(fileName)))
			.build();
		return response;
	}

	private String readJsonFile(String fileName) {
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
			System.exit(0);
		}
		return text;
	}
	
	@Before
	private GoogleBooksImp createGoogleBooksImp() {
		try {
			setupClient();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return new GoogleBooksImp(client, new ObjectMapper());
	}

	
}
