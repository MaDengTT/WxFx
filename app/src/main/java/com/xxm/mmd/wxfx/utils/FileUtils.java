package com.xxm.mmd.wxfx.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.xxm.mmd.wxfx.BuildConfig;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.ui.ZxingActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by MaDeng on 2018/3/18.
 */

public class FileUtils {



    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null ||
                    files.length == 0) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static void upload(Context context, String path) {
        BmobFile file = new BmobFile(new File(path));
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    public static void loadFiles(final List<String> urls,Consumer<List<String>> consumer) {
        Observable.fromIterable(urls)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                Log.d("FileUtils", s);
                                bmobLoadfile(s,emitter);
                            }
                        });
                    }
                }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                if (BuildConfig.DEBUG) Log.d("FileUtils", s);
                return s;
            }
        }).buffer(urls.size())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("tt", "accept: ",throwable );
                    }
                });
    }

    private static void bmobLoadfile(String url, final ObservableEmitter<String> emitter) throws IOException {
        BmobFile bFile = new BmobFile(System.currentTimeMillis() + ".jpg","",url);

        File file = new File(getFilePath());
        bFile.download(file, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    emitter.onNext(s);
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });

    }

    public static Observable<String>  saveQRCode(Bitmap bitmap) {
        return Observable.just(bitmap)
                .map(new Function<Bitmap, String>() {
                    @Override
                    public String apply(Bitmap bitmap) throws Exception {
                        String fileName = "";
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/fxQR";
                            fileName = filePath + "/" + System.currentTimeMillis() + ".jpg";
                            File dir1 = new File(filePath);
                            if (!dir1.exists()) {
                                dir1.mkdirs();
                            }
                            FileOutputStream output = new FileOutputStream(fileName);
//                            output.write(resource);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output);
                            //将bytes写入到输出流中
                            output.close();
                        }

                        return fileName;
                    }
                });
    }

    private static String getFilePath() throws IOException {
        String fileName = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/fx";
            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            fileName = filePath + "/" + System.currentTimeMillis() + ".jpg";
        }
        return fileName;
    }


    @TargetApi(19)
    public static String formatUri(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();

//            Log.d("FileUtils", uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        Log.d("FileUtils", uri.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            path = formatUri(context, uri);
            Log.d("FileUtils", path.toString());
            return path;
        }

        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String saveImageToGallery(Context context, File file) {
//        // 首先保存图片
//        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        String fileName = System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 其次把文件插入到系统图库
         String s="";
        try {
            s = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            Log.d("FileUtils", s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        return s;
    }

}
