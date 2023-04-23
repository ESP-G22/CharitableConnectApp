package dev.n0ne1eft.charitableconnect;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.icu.util.Output;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import api.EventCreate;
import api.UserGet;
import api.UserProfile;
import api.Util;
import layout.OutputPair;

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
    private List<Bitmap> allImagesAttached;
    private Map<View, Bitmap> allCards;
    private UserProfile user;
    private LinearLayout linearLayout;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
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
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();

        allImagesAttached = new LinkedList<>();
        allCards = new HashMap<>();
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
                                    return;
                                }
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                //Uri tempUri = getImageUri(getContext(), selectedImageBitmap);

                                // CALL THIS METHOD TO GET THE ACTUAL PATH
                                //File finalFile = new File(getRealPathFromURI(selectedImagedUri));

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

        // Set dropdown for event types
        Spinner eventTypesWidget = view.findViewById(R.id.eventTypeDropdown);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(
                getContext(), R.array.categories, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        eventTypesWidget.setAdapter(adapter);

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
                OutputPair output = createNewEvent();

                if (!(output.isSuccess())) {
                    Toast.makeText(getActivity(), output.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                // Resetting inputs
                EditText titleBox = (EditText) view.findViewById(R.id.titleEditText);
                EditText descriptionBox = (EditText) view.findViewById(R.id.descriptionText);
                EditText address1Box = (EditText) view.findViewById(R.id.address1EditText);
                EditText postcodeBox = (EditText) view.findViewById(R.id.address1EditText);
                titleBox.setText("");
                descriptionBox.setText("");
                address1Box.setText("");
                postcodeBox.setText("");

                Toast.makeText(getActivity(), output.getMessage(), Toast.LENGTH_LONG).show();
                //Launch feed
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_main_activity);
                //Send data back to feed
                //...
                navController.navigate(R.id.feedFragment);


            }
        });

        return view;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
        timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);
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

        allImagesAttached.add(image);
        allCards.put(card, image);

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

        Bitmap image = allCards.get(card);
        allImagesAttached.remove(image);
        allCards.remove(card);

        //Remove it from layout
        linearLayout.removeView(card);
    }

    public OutputPair createNewEvent() {
        EditText titleBox = (EditText) view.findViewById(R.id.titleEditText);
        EditText descriptionBox = (EditText) view.findViewById(R.id.descriptionText);
        EditText address1Box = (EditText) view.findViewById(R.id.address1EditText);
        EditText postcodeBox = (EditText) view.findViewById(R.id.postcodeEditText);
        Spinner eventTypeSpinner = (Spinner) view.findViewById(R.id.eventTypeDropdown);

        List<Bitmap> images = allImagesAttached;
        String title = titleBox.getText().toString();
        String description = descriptionBox.getText().toString();
        String address1 = address1Box.getText().toString();
        String postcode = postcodeBox.getText().toString();
        String eventType = eventTypeSpinner.getSelectedItem().toString();

        // TODO: Need input boxes for these
        String address2 = null;

        // Get date
        DatePicker dateWidget = datePickerDialog.getDatePicker();
        int day = dateWidget.getDayOfMonth();
        int month = dateWidget.getMonth() + 1;
        int year = dateWidget.getYear();

        // Get time inputted from time button that is updated to the event time.
        String[] time = timeBut.getText().toString().split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        Date datetime;
        try {
            datetime = Util.valuesToDate(year, month, day, hour, minute);
        } catch (ParseException err) {
            return new OutputPair(false, getString(R.string.PARSING_DATETIME_ERROR));
        }

        NewEventTask create = new NewEventTask(
                eventType, title, description, datetime, address1, address2,
                postcode, images, user
        );
        create.execute();
        try {
            OutputPair output_create = create.get();  // get return value from thread.
            return output_create;
        } catch (ExecutionException err) {
            return new OutputPair(false, "ExecutionError");
        } catch (InterruptedException err) {
            return new OutputPair(false, "InterruptedError");
        }
    }
}

class NewEventTask extends AsyncTask<String, String, OutputPair> {
    private String eventType;
    private String title;
    private String description;
    private Date datetime;
    private String address1;
    private String address2;
    private String postcode;
    private UserProfile organiser;

    private List<Bitmap> images;

    public NewEventTask(String eventType, String title, String description, Date datetime, String address1, String address2, String postcode, List<Bitmap> images,
                        UserProfile organiser) {
        super();
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.address1 = address1;
        this.address2 = address2;
        this.postcode = postcode;
        this.images = images;
        this.organiser = organiser;
    }
    protected OutputPair doInBackground(String... params) {
        return organiser.createEvent(
                eventType, title, description, datetime,
                address1, address2, postcode, images
        );
    }
}
