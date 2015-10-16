package ru.kraynov.app.ssaknitu.events.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.EventModel;
import ru.kraynov.app.ssaknitu.events.view.activity.EvFragmentContainerActivity;
import ru.kraynov.app.ssaknitu.events.view.fragment.EventFragment;
import ru.kraynov.app.ssaknitu.events.view.widget.EvTextView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>  {

    private ArrayList<EventModel> events;
    private final Context context;

    public EventsAdapter(Context context, ArrayList<EventModel> events){
        this.context = context;
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String date = getItem(position).date;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getItem(position).date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.title.setText(getItem(position).title.replace("&#171;","\"").replace("&#187;","\""));
        holder.organisation.setText(getItem(position).organisation_name);
        holder.subtitle.setHTMLTrimmed(getItem(position).description_short.replace("&#171;", "\"").replace("&#187;", "\""));
        holder.date.setText(date);
        Picasso.with(context).load("http://" + getItem(position).event_cover).error(R.color.material_blue_grey_100).placeholder(R.color.material_blue_grey_100).into(holder.cover);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        new Intent(context, EvFragmentContainerActivity.class)
                                .putExtra(EvFragmentContainerActivity.ARG_FRAGMENT_ID, 1)
                                .putExtra(EventFragment.ARG_EVENT_DATA,getItem(position))
                                .putExtra(EvFragmentContainerActivity.ARG_IS_FADING, true)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public EventModel getItem(int position) {
        return events.get(position);
    }

    public void updateItems(ArrayList<EventModel> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title) EvTextView title;
        @InjectView(R.id.organisation) EvTextView organisation;
        @InjectView(R.id.subtitle) EvTextView subtitle;
        @InjectView(R.id.date) EvTextView date;
        @InjectView(R.id.cover) ImageView cover;
        View root;

        public ViewHolder(View v) {
            super(v);
            root = v;
            ButterKnife.inject(this, v);
        }
    }
}

