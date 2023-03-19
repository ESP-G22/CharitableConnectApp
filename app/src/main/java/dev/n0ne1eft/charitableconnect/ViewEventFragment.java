package dev.n0ne1eft.charitableconnect;

import android.icu.util.Output;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutionException;

import api.Event;
import api.UserProfile;
import kotlinx.coroutines.scheduling.Task;
import layout.OutputPair;
import layout.Pair;

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

    public void setWidgets(View view) {
        TextView titleBox = (TextView) view.findViewById(R.id.eventTitleText);
        TextView descriptionBox = (TextView) view.findViewById(R.id.descriptionText);
        TextView eventSummaryBox = (TextView) view.findViewById(R.id.eventSubtitleText);
        TextView organiserUsernameBox = (TextView) view.findViewById(R.id.organiserUsernameText);
        TextView organiserInfoBox = (TextView) view.findViewById(R.id.organiserInfoText);
        Button rsvpButton = (Button) view.findViewById(R.id.rsvpButton);
        Button favouriteButton = (Button) view.findViewById(R.id.favouriteButton);

        titleBox.setText(event.getTitle());
        descriptionBox.setText(event.getInfo());
        eventSummaryBox.setText(String.valueOf(event.getShortInfo()));
        organiserUsernameBox.setText(organiser.getUsername());
        organiserInfoBox.setText(organiser.getInfo());

        rsvpButton.setText(getRSVPText(event.getRsvp() != null));
        favouriteButton.setText(getFavouriteText(user.getFollowedUsers().contains(organiser.getID())));
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
