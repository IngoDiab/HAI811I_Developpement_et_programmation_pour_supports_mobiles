package com.mobile.ex3;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFromWeb extends AsyncTask<String, Void, String> {

    public TextView mFeedbackField;

    @Override
    protected String doInBackground(String... urls) {
        return download(urls[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mFeedbackField.setText(result);
    }

    private String download(String _urlString) {
        URL _url;
        HttpURLConnection _connexion;
        InputStream _inputStream;

        try {
            _url = new URL(_urlString);
            _connexion = (HttpURLConnection) _url.openConnection();
            _connexion.setReadTimeout(10000);
            _connexion.setConnectTimeout(15000);
            _connexion.setRequestMethod("GET");
            _connexion.setDoInput(true);
            _connexion.connect(); //Connect to target
            _inputStream = _connexion.getInputStream(); //Get stream with target
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int _size = 700;
        String _content = ReadStream(_inputStream, _size); //Read stream
        return _content;
    }

    public String ReadStream(InputStream _inputStream, int _size){
        Reader _reader;
        char[] _buffer = new char[_size];

        try {
            _reader = new InputStreamReader(_inputStream, "UTF-8");
            _reader.read(_buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String(_buffer);
    }
}
