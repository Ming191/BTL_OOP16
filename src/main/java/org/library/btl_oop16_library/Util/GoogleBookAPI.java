package org.library.btl_oop16_library.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.library.btl_oop16_library.Model.Book;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GoogleBookAPI {
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String API_KEY = "AIzaSyB_Ye1f7z4oQnMk_9Aaf0olRcTpElc8pHs";

    public static List<Book> searchBooks(String query) {
        List<Book> books = new ArrayList<>();
        try {
            JSONArray items = getObjects(query);

            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject bookJson = items.getJSONObject(i).getJSONObject("volumeInfo");

                    String title = bookJson.optString("title");
                    String author = bookJson.optJSONArray("authors") != null ? bookJson.getJSONArray("authors").getString(0) : "Unknown";
                    String category = bookJson.optJSONArray("categories") != null ? bookJson.getJSONArray("categories").getString(0) : "N/A";
                    String language = bookJson.optString("language", "N/A");

                    String imageUrl = bookJson.has("imageLinks") ? bookJson.getJSONObject("imageLinks").optString("thumbnail", "") : "";
                    String rating = bookJson.has("averageRating") ? String.valueOf(bookJson.getDouble("averageRating")) : "Unknown rating";
                    String description = bookJson.has("description") ? bookJson.getString("description") : "Unknown description";
                    String previewURL = bookJson.has("previewLink") ? bookJson.getString("previewLink") : "";

                    Book book = new Book(title, author, category, language, imageUrl, rating, description, previewURL);
                    books.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    private static JSONArray getObjects(String query) throws IOException, InterruptedException {
        String urlString = GOOGLE_BOOKS_API_URL + query.replace(" ", "+") + "&key=" + API_KEY;
        URI uri = URI.create(urlString);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP Response Code: " + response.statusCode());
        }

        JSONObject jsonObject = new JSONObject(response.body());
        return jsonObject.optJSONArray("items");
    }
}
