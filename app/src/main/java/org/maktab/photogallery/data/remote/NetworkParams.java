package org.maktab.photogallery.data.remote;

import java.util.HashMap;
import java.util.Map;

public class NetworkParams {

    public static final String BASE_PATH = "https://www.flickr.com/services/rest/";
    public static final String METHOD_RECENT = "flickr.photos.getRecent";
    public static final String METHOD_POPULAR = "flickr.photos.getPopular";
    public static final String METHOD_SEARCH = "flickr.photos.search";
    public static final String API_KEY = "79b5c28546b0c0fd5a0bdc65ac9eab18";
    private static final String USER_ID = "34427466731@N01";

    public static Map<String, String> BASE_OPTIONS = new HashMap<String, String>() {{
        put("api_key", API_KEY);
        put("format", "json");
        put("extras", "url_s");
        put("user_id", USER_ID);
        put("nojsoncallback", "1");
    }};

    public static Map<String, String> getPopularOptions() {
        Map<String, String> queryOptions = new HashMap<>();
        queryOptions.putAll(BASE_OPTIONS);
        queryOptions.put("method", METHOD_POPULAR);

        return queryOptions;
    }

    public static Map<String, String> getSearchOptions(String query) {
        Map<String, String> searchOptions = new HashMap<>();
        searchOptions.putAll(BASE_OPTIONS);
        searchOptions.put("method", METHOD_SEARCH);
        searchOptions.put("text", query);

        return searchOptions;
    }
}
