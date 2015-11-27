package com.example.gearbox.week11_cameralab;


import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureDisplayFragment extends Fragment {

    public static final String TAG = "CaptureDisplayFragment";

    public CaptureDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capture_display, container, false);

        //get the passed argument- the string
        String picLocation = getArguments().getString("picLocation");
        //parse the string received back to a URI
        Uri picLoc = Uri.parse(picLocation);

        //get the bitmap
        Bitmap bitMapFull = BitmapFactory.decodeFile(picLoc.getPath());

        //assign bitmap to imageview
        ImageView imgFull = (ImageView) view.findViewById(R.id.imgFullSize);
        imgFull.setImageBitmap(bitMapFull);


        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        //set on click listener for back button,
        btnBack.setOnClickListener(new View.OnClickListener() {
            FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragContainer, new CameraCaptureFragment(), CameraCaptureFragment.TAG)
                        .commit();

            }
        });


        return view;

    }


}
