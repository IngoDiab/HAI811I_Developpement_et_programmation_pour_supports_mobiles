package com.mobile.ex4;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    public static void SaveJson(Context _context, JSONObject _json, String _fileName)
    {
        try {
            FileOutputStream _streamOut = _context.openFileOutput(_fileName, Context.MODE_PRIVATE);
            _streamOut.write(_json.toString().getBytes());
            _streamOut.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject LoadJson(Context _context, File _filePath, String _fileName)
    {
        JSONObject _jsonRead;
        File _file = new File(_filePath, _fileName);
        if(!_file.exists()) return new JSONObject();
        byte[] _data = new byte[(int)_file.length()];
        try {
            FileInputStream _streamIn = _context.openFileInput(_fileName);
            _streamIn.read(_data);
            _streamIn.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            _jsonRead = new JSONObject(new String(_data));
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
        return _jsonRead;
    }
}
