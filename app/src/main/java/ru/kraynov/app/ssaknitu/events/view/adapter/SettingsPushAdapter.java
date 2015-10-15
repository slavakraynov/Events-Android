package ru.kraynov.app.ssaknitu.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.OrganisationModel;
import ru.kraynov.app.ssaknitu.events.view.widget.EvTextView;

public class SettingsPushAdapter extends RecyclerView.Adapter<SettingsPushAdapter.ViewHolder> implements View.OnClickListener {

    private final Context context;
    ArrayList<OrganisationModel> organisations;
    private ArrayList<Integer> includedOrganisations;

    public SettingsPushAdapter(Context context, ArrayList<OrganisationModel> organisations, ArrayList<Integer> includedOrganisations){
        this.context = context;
        this.organisations = organisations;
        this.includedOrganisations = includedOrganisations;

        updateData();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.list_item_toggle, null);
        ViewHolder holder = new ViewHolder(view,this);
        return holder;
    }
    public OrganisationModel getSource(int pos){
        return organisations.get(pos);
    }

    public boolean isIncluded(int id){
        if (includedOrganisations==null || includedOrganisations.size()==0) return false;

        for (int includedId : includedOrganisations){
            if (includedId==id) {
                return true;
            }
        }

        return false;
    }

    public void updateData(){
        for (OrganisationModel organisation : organisations) {
            organisation.active = isIncluded(organisation.id);
        }

    }

    public void addItems(ArrayList<OrganisationModel> organisations, ArrayList<Integer> includedOrganisations) {
        this.organisations = organisations;
        this.includedOrganisations = includedOrganisations;

        updateData();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkBox.setChecked(getSource(position).active);
        holder.checkBox.setOnClickListener(this);
        holder.checkBox.setTag(position);
        holder.title.setText(getSource(position).name);

        holder.root.setTag(getSource(position).id);
    }

    public ArrayList<Integer> getActiveSources(){
        ArrayList<Integer> excludedList = new ArrayList<>();

        for (OrganisationModel organisation: organisations){
            if (organisation.active) excludedList.add(organisation.id);
        }

        return excludedList;
    }

    @Override
    public int getItemCount() {
        return organisations.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkBox:
                getSource((Integer) v.getTag()).active = ((CheckBox)v).isChecked();
                break;
            default:
                CheckBox checkBox = ((CheckBox) v.findViewById(R.id.checkBox));
                checkBox.setChecked(!checkBox.isChecked());
                getSource((Integer) checkBox.getTag()).active = checkBox.isChecked();
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        @InjectView(R.id.title)
        EvTextView title;
        @InjectView(R.id.checkBox)
        CheckBox checkBox;

        public ViewHolder(View view, View.OnClickListener clickListener) {
            super(view);
            root = view;
            root.setOnClickListener(clickListener);
            ButterKnife.inject(this, view);
        }
    }
}
