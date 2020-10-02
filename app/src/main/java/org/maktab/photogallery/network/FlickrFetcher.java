package org.maktab.photogallery.network;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlickrFetcher {

    public static final String BASE_PATH = "https://www.flickr.com/services/rest";
    public static final String METHOD_RECENT = "flickr.photos.getRecent";
    public static final String API_KEY = "79b5c28546b0c0fd5a0bdc65ac9eab18";

    public static String generateUrl() {
        Uri uri = Uri.parse(BASE_PATH)
                .buildUpon()
                .appendQueryParameter("method", METHOD_RECENT)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("extras", "url_s")
                .appendQueryParameter("nojsoncallback", "1")
                .build();

        return uri.toString();
    }

    public byte[] getBytes(String urlString) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream input = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new IOException(connection.getResponseMessage() + " with url: " + urlString);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readCount = 0;
            while ((readCount = input.read(buffer)) != -1) {
                output.write(buffer, 0, readCount);
            }

            byte[] result = output.toByteArray();
            output.close();
            input.close();

            return result;
        } finally {
            connection.disconnect();
        }
    }

    public String getString(String urlString) throws IOException {
        String result = new String(getBytes(urlString));
        return result;
    }
}
