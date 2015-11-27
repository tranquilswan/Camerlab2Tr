package com.example.gearbox.week11_cameralab;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraDevice;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CameraCaptureFragment extends Fragment {

    private static final int TAKE_PICTURE = 1;
    private Uri outputFileUri;
    public static final String TAG = "CameraCaptureFragment";

    public CameraCaptureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentManager fm = getFragmentManager();
        //assign the appropriate layout to the fragContainter (frame layout)
        fm.beginTransaction().replace(R.id.fragContainer, new CameraCaptureFragment(), CameraCaptureFragment.TAG);

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_camera_capture, container, false);

        //on click listener for the button
        Button btnCapture = (Button) view.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(view);
            }
        });

        ImageView imgThumbView = (ImageView) view.findViewById(R.id.imgThumbnail);
        //set onclick listener for the imageview
        imgThumbView.setOnClickListener(new View.OnClickListener() {
            FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {
                //to send the picture to the other fragment:
                //create a bundle
                final Bundle picLoc = new Bundle();
                //put the location of the file (URI) as a string in the bundle
                picLoc.putString("picLocation", outputFileUri.toString());
                //create an instance of the fragment to be called
                final CaptureDisplayFragment cdf = new CaptureDisplayFragment();
                //assign bundle to fragment via set argument
                cdf.setArguments(picLoc);
                //begin the transaction (call fragment)
                fm.beginTransaction().replace(R.id.fragContainer, cdf, CaptureDisplayFragment.TAG)
                        .commit();
            }
        });

        return view;
    }

    private boolean isIntentHandlerAvailable(Intent intent) {

        PackageManager pm = getActivity().getPackageManager();

        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }


    public void takePicture(View view) {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (isIntentHandlerAvailable(pictureIntent)) {
            outputFileUri = getFileUri();
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(pictureIntent, TAKE_PICTURE);
        } else {
            Toast.makeText(getActivity(), "Camera Handler not available", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private Uri getFileUri() {
        File folder
                = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/MyPics");
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Log.e("CameraCaptureFrag", "Couldn't create folder" + folder.toString());
                return null;
            }
        }

        if (!folder.canWrite()) {
            Log.e("CameraCaptureFrag", "Can't write to folder. " + folder.toString() + " Check <Uses-permission>");
            return null;
        }
        String fileName =
                new SimpleDateFormat("yyMMdd_hhss", Locale.CANADA)
                        .format(new Date()) + ".jpg";
        File file = new File(folder, fileName);
        return Uri.fromFile(file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            Bitmap bitmapFull = BitmapFactory.decodeFile(outputFileUri.getPath());

            ImageView imgThumbnail = (ImageView) getActivity().findViewById(R.id.imgThumbnail);
            imgThumbnail.setImageBitmap(bitmapFull.createScaledBitmap(bitmapFull, 200, 200, true));

        }
    }
}
