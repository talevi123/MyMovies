package com.tal.mymovies.Network;


import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.List;


/**
 * Created by tal on 12/09/16.
 */
public class Youtubeconnector {
    private YouTube youTube;
    private YouTube.Search.List query;

    public static long MAX_RESULTS = 1;
    public static String KEY_API = "AIzaSyBI7gz3jbGZdbS9MjlRjnv4Ur-s1s9HckQ";

    public Youtubeconnector(){
        youTube = new YouTube.Builder(new NetHttpTransport(),new JacksonFactory(),new HttpRequestInitializer() {

            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        }).setApplicationName("MyMovies").build();

        try {
            query = youTube.search().list("id");
            query.setKey(KEY_API);
            query.setType("video");
            query.setMaxResults(MAX_RESULTS);
            query.setFields("items(id/videoId)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String search(String keywords){
        query.setQ("trailer "+keywords);
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            SearchResult result = results.get(0);
            String ans = result.getId().getVideoId();
            return ans;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
