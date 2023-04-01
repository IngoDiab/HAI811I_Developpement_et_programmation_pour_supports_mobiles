package com.mobile.ex4;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;

        //Get download path
        String _dlPath = intent.getExtras().getString("DLPath");

        //Get Fragment's Receiver
        ResultReceiver _onDLFinished = intent.getParcelableExtra("OnDLFinishedReceiver");

        JSONObject _json;

        //If the file already exists, no need to download
        File _file = new File(getFilesDir(), _dlPath);
        if(_file.exists()) {
            //Load json
            Toast.makeText(this, "aaaaa", Toast.LENGTH_SHORT).show();
            _json = FileManager.LoadJson(this, getFilesDir(), _dlPath);
        }

        //Get file from web
        _json = GetFromWeb(_dlPath);

        //Send to Fragment's Receiver
        Bundle _bundle = new Bundle();
        _bundle.putString("DLData",_json.toString());
        _onDLFinished.send(1,_bundle);
    }

    private JSONObject GetFromWeb(String _dlPath)
    {
        //Connect
        ConnectivityManager _connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(_connectManager == null) return new JSONObject();
        NetworkInfo _info = _connectManager.getActiveNetworkInfo();
        if(_info == null || !_info.isConnected()) return new JSONObject();
        String _resultDL = Download(_dlPath);

        JSONObject _json = new JSONObject();
        try {
            //Save as json
            _json = new JSONObject(_resultDL);
            FileManager.SaveJson(this, _json, _dlPath);
        } catch (JSONException e) {
        }

        return _json;
    }

    private String Download(String _urlString) {
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

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service créé", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service terminé", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}