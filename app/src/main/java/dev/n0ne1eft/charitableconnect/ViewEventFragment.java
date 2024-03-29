package dev.n0ne1eft.charitableconnect;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.ExecutionException;

import api.Event;
import api.UserProfile;
import layout.OutputPair;

public class ViewEventFragment extends Fragment {
    private UserProfile user;

    private Event event;
    private UserProfile organiser;

    public static FeedFragment newInstance(Event event) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putParcelable("EVENT", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();

        if (getArguments() != null) {
            event = (Event) getArguments().getParcelable("EVENT");
            GetUserTask getOrganiser = new GetUserTask(user.getToken(), event.getOrganiserID());
            getOrganiser.execute();
            try {
                organiser = getOrganiser.get();
            } catch (InterruptedException err) {

            } catch (ExecutionException err) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_event, container, false);

        setWidgets(view);

        view.findViewById(R.id.rsvpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button rsvpButton = (Button) view.findViewById(R.id.rsvpButton);

                OutputPair status;
                if ("Remove RSVP".equals(rsvpButton.getText().toString())) {
                    status = removeRSVP();
                } else {
                    status = createRSVP();
                }

                Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_LONG).show();

                if (status.isSuccess()) {
                    rsvpButton.setText(getRSVPText(event.getRsvp() != null));
                }
            }
        });

        view.findViewById(R.id.favouriteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button favouriteButton = (Button) view.findViewById(R.id.favouriteButton);

                OutputPair status;
                if ("Subscribe".equals(favouriteButton.getText().toString())) {
                    status = subscribe();
                } else {
                    status = unsubscribe();
                }

                Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_LONG).show();

                if (status.isSuccess()) {
                    favouriteButton.setText(getFavouriteText(user.getFollowedUsers().contains(event.getOrganiserID())));
                }
            }
        });

        return view;
    }

    public OutputPair createRSVP() {
        CreateRSVPTask task = new CreateRSVPTask(event);
        task.execute();
        try {
            OutputPair output = task.get();  // get return value from thread.
            return output;
        } catch (ExecutionException err) {
            return new OutputPair(false, "Execution Error");
        } catch (InterruptedException err) {
            return new OutputPair(false, "Interrupted Error");
        }
    }

    public OutputPair removeRSVP() {
        RemoveRSVPTask task = new RemoveRSVPTask(event);
        task.execute();
        try {
            OutputPair output = task.get();  // get return value from thread.
            return output;
        } catch (ExecutionException err) {
            return new OutputPair(false, "Execution Error");
        } catch (InterruptedException err) {
            return new OutputPair(false, "Interrupted Error");
        }
    }

    public OutputPair subscribe() {
        SubscribeTask task = new SubscribeTask(user, event.getOrganiserID());
        task.execute();
        try {
            OutputPair output = task.get();  // get return value from thread.
            return output;
        } catch (ExecutionException err) {
            return new OutputPair(false, "Execution Error");
        } catch (InterruptedException err) {
            return new OutputPair(false, "Interrupted Error");
        }
    }

    public OutputPair unsubscribe() {
        UnsubscribeTask task = new UnsubscribeTask(user, event.getOrganiserID());
        task.execute();
        try {
            OutputPair output = task.get();  // get return value from thread.
            return output;
        } catch (ExecutionException err) {
            return new OutputPair(false, "Execution Error");
        } catch (InterruptedException err) {
            return new OutputPair(false, "Interrupted Error");
        }
    }

    public void setWidgets(View view) {
        TextView titleBox = (TextView) view.findViewById(R.id.eventTitleText);
        TextView descriptionBox = (TextView) view.findViewById(R.id.descriptionText);
        TextView eventSummaryBox = (TextView) view.findViewById(R.id.eventSubtitleText);
        TextView organiserUsernameBox = (TextView) view.findViewById(R.id.organiserUsernameText);
        TextView organiserInfoBox = (TextView) view.findViewById(R.id.organiserInfoText);
        Button rsvpButton = (Button) view.findViewById(R.id.rsvpButton);
        Button favouriteButton = (Button) view.findViewById(R.id.favouriteButton);
        ImageView eventImage = (ImageView) view.findViewById(R.id.eventImage);
        ImageView organiserImage = (ImageView) view.findViewById(R.id.organiserAvatar);

        titleBox.setText(event.getTitle());
        descriptionBox.setText(event.getInfo());
        eventSummaryBox.setText(String.valueOf(event.getShortInfo()));
        organiserUsernameBox.setText(organiser.getUsername());
        organiserInfoBox.setText(organiser.getInfo());

        rsvpButton.setText(getRSVPText(event.getRsvp() != null));
        favouriteButton.setText(getFavouriteText(user.getFollowedUsers().contains(organiser.getID())));

        // set event images
        List<Bitmap> images = event.getImages();
        if (images != null) {
            Bitmap image;
            image = images.get(0);
            eventImage.setImageBitmap(image);
        }

        // set profile image
        Bitmap userImage = organiser.getProfilePic();
        if (userImage != null) {
            organiserImage.setImageBitmap(userImage);
        }
    }

    public String getRSVPText(boolean hasRSVP) {
        return (hasRSVP) ? "Remove RSVP": "RSVP";
    }

    public String getFavouriteText(boolean isFavourite) {
        return (isFavourite) ? "Unsubscribe": "Subscribe";
    }
}

class CreateRSVPTask extends AsyncTask<String, String, OutputPair> {
    private Event event;

    public CreateRSVPTask(Event event) {
        super();
        this.event = event;
    }
    protected OutputPair doInBackground(String... params) {
        return event.addRsvp();
    }
}

class RemoveRSVPTask extends AsyncTask<String, String, OutputPair> {
    private Event event;

    public RemoveRSVPTask(Event event) {
        super();
        this.event = event;
    }
    protected OutputPair doInBackground(String... params) {
        return event.removeRsvp();
    }
}

class SubscribeTask extends AsyncTask<String, String, OutputPair> {
    private UserProfile user;
    private int organiserID;

    public SubscribeTask(UserProfile user, int organiserID) {
        super();
        this.user = user;
        this.organiserID = organiserID;
    }
    protected OutputPair doInBackground(String... params) {
        UserProfile organiser;
        try {
            organiser = new UserProfile(user.getToken(), organiserID);
        } catch (Exception err) {
            return new OutputPair(false, "Could not get organiser");
        }
        OutputPair status = user.subscribe(organiser);

        if (status.isSuccess()) {
            status.setMessage("Subscribed to " + organiser.getUsername());
        }

        return status;
    }
}

class UnsubscribeTask extends AsyncTask<String, String, OutputPair> {
    private UserProfile user;
    private int organiserID;

    public UnsubscribeTask(UserProfile user, int organiserID) {
        super();
        this.user = user;
        this.organiserID = organiserID;
    }
    protected OutputPair doInBackground(String... params) {
        UserProfile organiser;
        try {
            organiser = new UserProfile(user.getToken(), organiserID);
        } catch (Exception err) {
            return new OutputPair(false, "Could not get organiser");
        }

        OutputPair status = user.unsubscribe(organiser);

        if (status.isSuccess()) {
            status.setMessage("Unsubscribed from " + organiser.getUsername());
        }

        return status;
    }
}
