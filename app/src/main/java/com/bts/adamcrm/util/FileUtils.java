package com.bts.adamcrm.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;


import com.opensooq.supernova.gligar.utils.ConstsKt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private static Uri contentUri;

    public static boolean decideToCompress(String fileName) {
        File file = new File(fileName);

        if (file.exists()){
            long bytes = file.length();
            long kilobytes = (bytes / 1024);
            long megabytes = (kilobytes / 1024);
            return megabytes > 1;
        }
        return false;
    }

    public static String getPath(Context context, Uri uri){

        if (!DocumentsContract.isDocumentUri(context, uri)){
            if ("content".equalsIgnoreCase(uri.getScheme())){
                if (isGooglePhotoUri(uri)){
                    return uri.getLastPathSegment();
                }
                if (isGoogleDriveUri(uri)){
                    return getDriveFilePath(uri, context);
                }
                if (Build.VERSION.SDK_INT == 24){
                    return getMediaFilePathForN(uri, context);
                }
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())){
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)){
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            String pathFromExtSD = getPathFromExtSD(split);
            if (pathFromExtSD != null && !pathFromExtSD.equals("")){
                return pathFromExtSD;
            }
            return null;
        } else if (isDownloadsDocument(uri)){
            try {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{"_display_name"}, null,null, null);
                if (cursor != null){
                    if (cursor.moveToFirst()){
                        String filepath = Environment.getExternalStorageDirectory().toString() + "/Download" + cursor.getString(0);
                        if (!TextUtils.isEmpty(filepath)){
                            cursor.close();
                            return filepath;
                        }
                    }
                }
                if (cursor != null){
                    cursor.close();
                }
                String documentId = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(documentId)){
                    if (documentId.startsWith("raw:")){
                        return documentId.replaceFirst("raw:", "");
                    }
                    try {
                        return getDataColumn(context, ContentUris.withAppendedId(
                                Uri.parse(new String[]{"content://downloads/public_downloads", "content://downloads/my_downloads"}[0]),
                                Long.parseLong(documentId)), null, null
                        );
                    } catch (NumberFormatException exception){
                        return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                    }
                }
            } catch (Throwable throwable){
                Log.e("junior", throwable.getMessage());
            }
        } else if (isMediaDocument(uri)){
            String [] split = DocumentsContract.getDocumentId(uri).split(":");
            String contentType = split[0];
            Uri fileUir = null;
            if ("image".equals(contentType)){
                fileUir = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(contentType)){
                fileUir = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(contentType)){
                fileUir = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, fileUir, "_id=?", new String[]{split[1]});
        } else if (isGoogleDriveUri(uri)){
            return getDriveFilePath(uri, context);
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        try {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ConstsKt.PATH_COLUMN}, str, strArr, null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    String strPath = cursor.getString(cursor.getColumnIndexOrThrow(ConstsKt.PATH_COLUMN));
                    cursor.close();
                    return strPath;
                }
            }
            if (cursor!= null)
                cursor.close();

        } catch (Throwable throwable){
            Log.e("junior", "throwable : " + throwable.getMessage());
        }
        return null;
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        int columnIndex = cursor.getColumnIndex("_display_name");
        int columnSize = cursor.getColumnIndex("_size");
        cursor.moveToFirst();
        File file = new File(context.getCacheDir(), cursor.getString(columnIndex));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[Math.min(openInputStream.available(), 1048576)];
            while (true){
                int read = openInputStream.read(bArr);
                if (read == -1){
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            openInputStream.close();
            fileOutputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file.getPath();
    }

    private static String getPathFromExtSD(String[] pathArr) {
        String primaryPath = pathArr[0];
        String extendPath = pathArr[1];
        if ("primary".equalsIgnoreCase(primaryPath)){
            String path = Environment.getExternalStorageDirectory() + "/"+ extendPath;
            if (fileExists(path)){
                return path;
            }
        }
        String secondaryPath = System.getenv("SECONDARY_STORAGE") + "/" + extendPath;
        if (fileExists(secondaryPath)){
            return secondaryPath;
        }
        String externPath = System.getenv("EXTERNAL_STORAGE") + "/" +extendPath;
        if (fileExists(externPath))
            return externPath;
        String finalPath = "/storage/" + primaryPath + "/" + extendPath;
        if (fileExists(finalPath))
            return finalPath;
        return null;
    }

    private static boolean fileExists(String path) {
        return new File(path).exists();
    }

    private static String getMediaFilePathForN(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        int columnIndex = cursor.getColumnIndex("_display_name");
        int columnSize = cursor.getColumnIndex("_size");
        cursor.moveToFirst();
        File file = new File(context.getFilesDir(), cursor.getString(columnIndex));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[Math.min(openInputStream.available(), 1048576)];
            while (true){
                int read = openInputStream.read(bArr);
                if (read == -1){
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            openInputStream.close();
            fileOutputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file.getPath();
    }


    public static Uri getImageUri(Context context, Bitmap inImage){
        inImage.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage,
                "attach", null));
    }

    public static String getRealPathFromURI(Context context, Uri uri){
        Cursor cursor;
        if (context.getContentResolver() == null || (cursor = context.getContentResolver().query(uri, null, null, null, null)) == null){
            return "";
        }
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(ConstsKt.PATH_COLUMN));
        cursor.close();
        return path;
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotoUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority())
                || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }


}
