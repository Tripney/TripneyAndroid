package com.msaranu.tripney.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.msaranu.tripney.TripneyApplication;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by msaranu on 3/10/18.
 */

public class ImageService {

    private static ImageService instance;

    public static ImageService getInstance() {
        if (instance == null) {
            instance = new ImageService();
        }
        return instance;
    }


    public String saveParseFile(Bitmap bitmap, Event event) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                stream);
        byte[] imageBytes = stream.toByteArray();
        final ParseFile file = new ParseFile("event_img_file", imageBytes);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    event.setObjectId(event.eventID);
                    event.put("eventImage", file.getUrl());
                    event.saveInBackground();
                }
            }
        });
        return (file.getUrl());

    }

    public String saveParseFile(Bitmap bitmap, Trip trip) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                stream);
        byte[] imageBytes = stream.toByteArray();
        final ParseFile file = new ParseFile("event_img_file", imageBytes);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    trip.setObjectId(trip.tripID);
                    trip.put("mbckgrndUrl", file.getUrl());
                    trip.saveInBackground();
                }
            }
        });
        return (file.getUrl());

    }


    @TargetApi(24)
    public Bitmap getBitMap(Context context, String filePath, Uri uri) {
        Bitmap bitmap = null;
        int rotate = 0;
        InputStream in;

        try {
            in = context.getContentResolver().openInputStream(uri);
            ExifInterface exif = new ExifInterface(in);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, options);
            Matrix matrix = new Matrix();
            if (rotate != 0) {
                matrix.preRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public  String getPath(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static Uri getOutputMediaFileUri(Context context) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TripneyApplication.APP_TAG);
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(TripneyApplication.APP_TAG, "failed to create directory");
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

            // wrap File object into a content provider
            // required for API >= 24
            // See http://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            return FileProvider.getUriForFile(context, "com.tripney.fileprovider", file);
        }
        return null;
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


}
