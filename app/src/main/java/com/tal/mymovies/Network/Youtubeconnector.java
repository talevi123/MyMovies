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
    private static Youtubeconnector instance;

    public static long MAX_RESULTS = 1;
    public static String KEY_API = "AIzaSyAcbLs3jhbd4HapqUfCpALxNzwncoHGMmI";

    public static Youtubeconnector getInstance() {
        if (instance == null) {
            instance = new Youtubeconnector();
        }

        return instance;
    }

    private Youtubeconnector() {
        youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        }).setApplicationName("MyMovies").build();
    }

    public String search(String keywords) {
        try {
            YouTube.Search.List query = youTube.search().list("id,snippet");
            query.setKey(KEY_API);
            query.setType("video");
            query.setMaxResults(MAX_RESULTS);
            query.setFields("items(id/videoId)");
            query.setQ("trailer " + keywords);
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            if (results != null && results.size() > 0) {
                SearchResult result = results.get(0);
                String ans = result.getId().getVideoId();
                return ans;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
