package dev.n0ne1eft.charitableconnect;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View view;
    private Image temp;
    private LinearLayout linearLayout;
    private DatePickerDialog datePickerDialog;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private Button dateBut;
    private Button timeBut;
    int hour, minute;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEventFragment newInstance(String param1, String param2) {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_event, container, false);

        //Common values
        linearLayout = (LinearLayout) view.findViewById(R.id.imageLinearLayout);

        //Images
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            //Operations from here
                            if (data != null && data.getData() != null){
                                Uri selectedImagedUri = data.getData();
                                Bitmap selectedImageBitmap = null;
                                try{
                                    selectedImageBitmap
                                            = MediaStore.Images.Media.getBitmap(
                                                    getActivity().getApplicationContext().getContentResolver(),
                                                    selectedImagedUri);
                                } catch (IOException e){
                                    e.printStackTrace();
                                }
                                uploadImage(selectedImageBitmap);
                            }
                        }
                    }
                });
        
        //Upload image onclick listener
        Button uploadImageBut = view.findViewById(R.id.UploadNewButton);
        uploadImageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectImage(v);}
        });

        //Select date
        initDatePicker();
        dateBut = view.findViewById(R.id.launchDateButton);
        dateBut.setText(getTodaysDate());
        dateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDate(v);
            }
        });
        //Select time
        timeBut = view.findViewById(R.id.launchTimeButton);
        timeBut.setText(String.format(Locale.getDefault(), "%02d:%02d",23,59));
        timeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTime(v);
            }
        });

        //Add other organiser

        //Save
        Button save = view.findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch feed
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);
                //Send data back to feed
                //...
                navController.navigate(R.id.feedFragment);


            }
        });

        return view;
    }
    //Helper methods
    public void selectImage(View v){
        //Get target image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateBut.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1){return "JAN";}
        if(month == 2){return "FEB";}
        if(month == 3){return "MAR";}
        if(month == 4){return "APR";}
        if(month == 5){return "MAY";}
        if(month == 6){return "JUN";}
        if(month == 7){return "JUL";}
        if(month == 8){return "AUG";}
        if(month == 9){return "SEP";}
        if(month == 10){return "OCT";}
        if(month == 11){return "NOV";}
        return "DEC";
    }

    //Onclick methods
    public void launchDate(View v){
        datePickerDialog.show();
    }
    public void launchTime(View v){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeBut.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    public void uploadImage(Bitmap image){
        //Initialise frame layout
        RelativeLayout card = new RelativeLayout(getActivity());
        //Initialise frame layout params
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //Initialise image view
        ImageView imageView = new ImageView(getActivity());
        //Initialise new button
        ImageButton deleteBut = new ImageButton(getActivity());
        deleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {deleteImage(v);}
        });

        //Edit button
        deleteBut.setBackgroundResource(R.drawable.delete_button_icon);
        //Edit imageview
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        //Edit card
        card.setPadding(0, 0, 12, 0);

        //Add image to image view
        imageView.setImageBitmap(image);
        //Add image to card
        card.addView(imageView);
        //Add delete button to card
        card.addView(deleteBut);
        //Add to layout
        linearLayout.addView(card);
    }

    public void deleteImage(View v){
        //Get card that v belongs to
        View card = (View)v.getParent();
        //Remove it from layout
        linearLayout.removeView(card);
    }
}