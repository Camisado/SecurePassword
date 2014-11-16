package com.example.Secure_Password;

/**
 * Created by Camisado on 26.05.2014.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.*;

public class LoaderFileWithDropbox extends AsyncTask<Void, Long, Boolean> {

    private final String FILE_NAME = DataBaseHelper.DATABASE_NAME;

    private DropboxAPI<?> dropbox;
    private String filePath;
    private String dir;
    private Context context;
    private Boolean download;
    private ProgressDialog progressDialog;
    private long totalBytes;

    public LoaderFileWithDropbox(Context context, DropboxAPI<?> dropbox,
                                 String filePath, String dir, Boolean download) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.filePath = filePath;
        this.dir = dir;
        this.download = download;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Загружается...");
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        File dataBase;
        dataBase = new File(filePath);
        totalBytes = dataBase.getTotalSpace();
        progressDialog.setMax((int)totalBytes);
        FileOutputStream fileOutputStream = null;
        FileInputStream fileInputStream = null;
        if(download) {
            try {
                fileOutputStream = new FileOutputStream(dataBase);
                try {
                    dropbox.getFile(dir + FILE_NAME, null, fileOutputStream, null);
                    return true;
                } catch (DropboxException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                fileInputStream = new FileInputStream(dataBase);
                try {
                    dropbox.putFileOverwrite(dir + FILE_NAME, fileInputStream, dataBase.length(), null);
                    return true;
                } catch (DropboxException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Long... progress) {
        progressDialog.setProgress(Integer.parseInt(""+progress[0]));
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        if (download) {
            if (result) {
                Toast.makeText(context, "Пароли успешно загружены из Dropbox!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Ошибка загрузки базы из Dropbox!", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            if (result) {
                Toast.makeText(context, "Пароли успешно загружены в Dropbox!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Ошибка загрузки базы в Dropbox!", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}