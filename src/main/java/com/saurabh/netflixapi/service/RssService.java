package com.saurabh.netflixapi.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssService {

    private static final String RSS2JSON_API_URL = "https://api.rss2json.com/v1/api.json?rss_url=";

    public List<String> getTopStories() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(RSS2JSON_API_URL + "https://timesofindia.indiatimes.com/rssfeedstopstories.cms")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResponse = new JSONObject(response.body().string());
        JSONArray items = jsonResponse.getJSONArray("items");
        List<String> topStories = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title");
            topStories.add(title);
        }
        return topStories;
    }
}
