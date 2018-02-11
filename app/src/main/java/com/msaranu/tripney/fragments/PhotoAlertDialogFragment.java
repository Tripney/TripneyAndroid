package com.msaranu.tripney.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.msaranu.tripney.R;
import com.msaranu.tripney.properties.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAlertDialogFragment extends DialogFragment {

    @BindView(R.id.btnSelectPhoto) public Button btnSelectPhoto;
    @BindView(R.id.btnTakePhoto)public Button btnTakePhoto;
    @BindView(R.id.btnCancel) public Button btnCancel;

    public PhotoAlertDialogFragment() {

    }

    public interface PhotoAlertDialogFragmentListener {
        void onFinishAlertDialog(String photoType);
    }

    public static PhotoAlertDialogFragment newInstance() {
        PhotoAlertDialogFragment frag = new PhotoAlertDialogFragment();
        return frag;
    }


    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        // Call super onResume after sizing
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fView = inflater.inflate(R.layout.fragment_photo_alert_dialog, container);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.layout_rounded_corners);
        fView.setBackgroundColor(Color.TRANSPARENT);
        Bundle bundle = this.getArguments();
        ButterKnife.bind(this,fView);
        return fView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSelectPhoto = (Button) getView().findViewById(R.id.btnSelectPhoto);
        btnTakePhoto = (Button) getView().findViewById(R.id.btnTakePhoto);
        btnCancel = (Button) getView().findViewById(R.id.btnCancel);


        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callParentFragment(Properties.PHOTO_SELECT);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callParentFragment(Properties.PHOTO_TAKE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void callParentFragment(String PhotoType) {
        PhotoAlertDialogFragmentListener listener = (PhotoAlertDialogFragmentListener) getTargetFragment();
        listener.onFinishAlertDialog(PhotoType);
        dismiss();

    }
}


