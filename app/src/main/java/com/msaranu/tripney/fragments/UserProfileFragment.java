package com.msaranu.tripney.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.TripneyApplication;
import com.msaranu.tripney.databinding.FragmentUserProfileBinding;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.properties.Properties;
import com.msaranu.tripney.services.UserService;
import com.msaranu.tripney.utilities.BitmapScaler;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends android.support.v4.app.Fragment implements
        PhotoAlertDialogFragment.PhotoAlertDialogFragmentListener {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public final static int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    @BindView(R.id.ivUserImage)
    ImageView ivUserImage;
    FragmentUserProfileBinding fragmentUserProfile;
    User user;
    Uri file;
    private String photoType;
    ParseUser pUser;
    UserService uService;
    int screenSize;


    public interface UserProfileUpdateListener {
        void onUpdateComplete();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uService = UserService.getInstance();
        pUser = ParseUser.getCurrentUser();
        user = uService.retriveUserFromParseUser(pUser);
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle args = new Bundle();
        userProfileFragment.setArguments(args);
        return userProfileFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        screenSize = (int) (pxWidth / displayMetrics.density);
        if( pUser.get("profilePicture") !=null) {
            String fileUrl = pUser.get("profilePicture").toString();
            setImageUrl(fragmentUserProfile.ivUserImage, fileUrl,screenSize);
        }
    }


    private static void setImageUrl(ImageView view, String imageUrl,int screenSize) {
        Glide.with(view.getContext()).load(imageUrl)
                .placeholder(R.drawable.default_user_image)
                .into(view);
    }

    private static void setImageUrlFile(ImageView view, Uri imageUrl,int screenSize) {
        screenSize = screenSize+200;
        // imageUrl = (new File(imageUrl)).getAbsolutePath();
        Glide.with(view.getContext()).load(imageUrl)
                .placeholder(R.drawable.default_user_image)
                .into(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentUserProfile = DataBindingUtil.inflate(
                inflater, R.layout.fragment_user_profile, parent, false);
        View view = fragmentUserProfile.getRoot();
        view.setBackgroundColor(Color.TRANSPARENT);
        fragmentUserProfile.setUser(user);
        ButterKnife.bind(this, view);
        fragmentUserProfile.executePendingBindings();

        //set selection to end of text
        EditText etName = fragmentUserProfile.etFirstName;
        int length = etName.getText().length();
        etName.setSelection(length);

        fragmentUserProfile.ivCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                PhotoAlertDialogFragment fdf = PhotoAlertDialogFragment.newInstance();
                fdf.setTargetFragment(UserProfileFragment.this, 300);
                fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                fdf.show(fm, "FRAGMENT_MODAL_ALERT");
            }
        });

        fragmentUserProfile.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uService.saveParseUser(fragmentUserProfile);
                Toast.makeText(getActivity(), "Successfully Saved",
                        Toast.LENGTH_LONG).show();
                uService.setUserUpdated(true);

                if (getActivity() instanceof UserProfileUpdateListener) {
                    ((UserProfileUpdateListener) getActivity()).onUpdateComplete();
                }
            }
        });

        fragmentUserProfile.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof UserProfileUpdateListener) {
                    ((UserProfileUpdateListener) getActivity()).onUpdateComplete();
                }
            }
        });

        return fragmentUserProfile.getRoot();
    }

    public void clickPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = UserService.getOutputMediaFileUri(getContext());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void saveProfileImage(int requestCode) {
        String filePath = null;
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            filePath = file.getPath();
        } else if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
            filePath = getPath(file);
        }

        Bitmap bitmap = uService.getBitMap(getContext(), filePath, file);
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(bitmap, screenSize);

        fragmentUserProfile.ivUserImage.setImageBitmap(resizedBitmap);

        uService.saveParseFile(bitmap);
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void pickPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    public void onFinishAlertDialog(String photoType) {
        this.photoType = photoType;
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]
                    {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TripneyApplication.PERM_CAM_PROFILE_REQ_CODE);
        } else {

            callCameraPhotoGallery();
        }
    }

    public void callCameraPhotoGallery() {
        if (photoType.equals(Properties.PHOTO_SELECT)) {
            pickPicture();

        } else if (photoType.equals(Properties.PHOTO_TAKE)) {
            clickPicture();

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
                file = imageReturnedIntent.getData();
            }
            saveProfileImage(requestCode);
        }
    }

}