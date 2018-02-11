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
import com.msaranu.tripney.databinding.FragmentUserProfileBinding;
import com.msaranu.tripney.models.User;
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


public class UserService {

    private static UserService instance;
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    boolean userUpdated;

    private UserService() {
    }

    public String saveParseFile(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                stream);
        byte[] imageBytes = stream.toByteArray();
        final ParseFile file = new ParseFile("profile_picture_file", imageBytes);
        final ParseUser finalPUser = ParseUser.getCurrentUser();
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    finalPUser.put("profilePicture", file.getUrl());
                    finalPUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("USER_SERVICE", "Profile Picture Saved successfully");
                        }
                    });


                }
            }
        });
        setUserUpdated(true);
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


    public String getPath(Uri uri, Context context) {
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
            return FileProvider.getUriForFile(context, "com.fomono.fomono.fileprovider", file);
        }
        return null;
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    public User retriveUserFromParseUser(ParseUser pUser) {
        User user = new User();
        if (pUser != null) {
            if (pUser.get("firstName") != null) {
                user.setFirstName(pUser.get("firstName").toString());
            }
            if (pUser.get("lastName") != null) {
                user.setLastName(pUser.get("lastName").toString());
            }
            if (pUser.get("email") != null) {
                user.setEmail(pUser.get("email").toString());
            }
            if (pUser.get("phone") != null && pUser.get("phone")!= "") {
                user.setPhone(pUser.get("phone").toString());
            }

            if (pUser.get("address1") != null && pUser.get("address1")!= "") {
                user.setAddress1(pUser.get("address1").toString());
            }
            if (pUser.get("address2") != null && pUser.get("address2")!= "") {
                user.setAddress2(pUser.get("address2").toString());
            }

            if (pUser.get("city") != null && pUser.get("city")!= "") {
                user.setCity(pUser.get("city").toString());
            }
            if (pUser.get("state") != null && pUser.get("state")!= "") {
                user.setState(pUser.get("state").toString());
            }
            if (pUser.get("zip") != null && pUser.get("zip")!= "") {
                user.setZip(pUser.get("zip").toString());
            }
            if (pUser.get("phone") != null && pUser.get("phone")!= "") {
                user.setPhone(pUser.get("phone").toString());
            }

            if (pUser.get("profilePicture") != null) {
                user.setProfilePicture(pUser.get("profilePicture").toString());
            }
        }
        return user;
    }

    public void saveParseUser(User u){
        ParseUser pUser = ParseUser.getCurrentUser();
        if(u.getFirstName() !=null){
            pUser.put("firstName", u.getFirstName());
        }
        if(u.getLastName() !=null){
            pUser.put("lastName", u.getLastName());
        }
        if(u.getLastName() !=null){
            pUser.put("email", u.getLastName());
        }
        if(u.getPhone() !=null){
            pUser.put("phone", u.getPhone());
        }

        if(u.getEmail() !=null){
            pUser.put("email", u.getEmail());
        }
        if(u.getAddress1() !=null){
            pUser.put("address1", u.getAddress1());
        }

        if(u.getAddress2() !=null){
            pUser.put("address2", u.getAddress2());
        }
        if(u.getCity() !=null){
            pUser.put("city", u.getCity());
        }

        if(u.getState() !=null){
            pUser.put("state", u.getState());
        }

        if(u.getZip() !=null){
            pUser.put("zip", u.getZip());
        }
        if(u.getProfilePicture() !=null){
            pUser.put("profilePicture", u.getProfilePicture());
        }


        pUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("USER_SERVICE", "User Saved successfully");
            }
        });
    }

    public void saveParseUser(FragmentUserProfileBinding fView) {
        ParseUser pUser = ParseUser.getCurrentUser();
        if (fView.etFirstName.getText() != null && !(fView.etFirstName.getText().toString().equals(pUser.get("firstName")))) {
            pUser.put("firstName", fView.etFirstName.getText().toString());
        }

        if (fView.etLastName.getText() != null && !(fView.etLastName.getText().toString().equals(pUser.get("lastName")))) {
            pUser.put("lastName", fView.etLastName.getText().toString());
        }
        if (fView.etEmail.getText() != null && !(fView.etEmail.getText().toString().equals(pUser.get("email")))) {
            pUser.put("email", fView.etEmail.getText().toString());
        }
        if (fView.etPhone.getText() != null && !(fView.etPhone.getText().toString().equals(pUser.get("phone")))) {
            pUser.put("phone", fView.etPhone.getText().toString());
        }
        if (fView.etAddress1.getText() != null && !(fView.etAddress1.getText().toString().equals(pUser.get("address1")))) {
            pUser.put("address1", fView.etAddress1.getText().toString());
        }

        if (fView.etAddress2.getText() != null && !(fView.etAddress2.getText().toString().equals(pUser.get("address2")))) {
            pUser.put("address2", fView.etAddress2.getText().toString());
        }

        if (fView.etCity.getText() != null && !(fView.etCity.getText().toString().equals(pUser.get("city")))) {
            pUser.put("city", fView.etCity.getText().toString());
        }

        if (fView.etState.getText() != null && !(fView.etState.getText().toString().equals(pUser.get("state")))) {
            pUser.put("state", fView.etState.getText().toString());
        }

        if (fView.etZip.getText() != null && !(fView.etZip.getText().toString().equals(pUser.get("zip")))) {
            pUser.put("zip", fView.etZip.getText().toString());
        }


        pUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("USER_SERVICE", "User Saved successfully");
            }
        });

    }

    public boolean isUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(boolean userUpdated) {
        this.userUpdated = userUpdated;
    }
}
