package com.proyect.cinegrupo5.repository;

import java.util.concurrent.TimeUnit;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseClient {

	private Retrofit retrofit;
	private HttpLoggingInterceptor interceptor;
	
	public DatabaseClient(String url, Long timeout) {
		interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		
		OkHttpClient cliente = new OkHttpClient.Builder()
				.addInterceptor(interceptor)
				.connectTimeout(timeout, TimeUnit.MILLISECONDS)
				.readTimeout(timeout, TimeUnit.MILLISECONDS)
				.writeTimeout(timeout, TimeUnit.MILLISECONDS)
				.build();
		retrofit = new Retrofit.Builder()
				.client(cliente)
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
				.build();
	}
	
	public DatabaseRepository getDatabase() {
		return retrofit.create(DatabaseRepository.class);
	}
}
