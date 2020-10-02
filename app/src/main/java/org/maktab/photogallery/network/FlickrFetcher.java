package org.maktab.photogallery.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlickrFetcher {

    public byte[] getBytes(String urlString) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
    }

    public String getString(String urlString) throws IOException {
        String result = new String(getBytes(urlString));
        return result;
    }
}
