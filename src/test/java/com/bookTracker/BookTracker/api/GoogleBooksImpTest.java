package com.bookTracker.BookTracker.api;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;

import okhttp3.OkHttpClient;

public class GoogleBooksImpTest {

	private GoogleBooksImp googleBooksImp;
	private OkHttpClient client;

	@Before
	private GoogleBooksImp createGoogleBooksImp() {
		client = mock(OkHttpClient.class);
		return new GoogleBooksImp(client, new ObjectMapper());
	}
}
