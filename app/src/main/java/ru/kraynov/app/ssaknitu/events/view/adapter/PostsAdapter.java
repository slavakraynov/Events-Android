package ru.kraynov.app.ssaknitu.events.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostModel;
import ru.kraynov.app.ssaknitu.events.view.activity.EvFragmentContainerActivity;
import ru.kraynov.app.ssaknitu.events.view.fragment.PostWebFragment;
import ru.kraynov.app.ssaknitu.events.view.widget.EvTextView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>  {

    private ArrayList<PostModel> items;
    private final Context context;

    public PostsAdapter(Context context, ArrayList<PostModel> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post_big, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PostModel post = getItem(position);

        holder.tv_title.setText(Html.fromHtml(post.title));
        if (post.excerpt.length()>0) {
            holder.tv_subtitle.setHTMLTrimmed(post.excerpt);
            holder.tv_subtitle.setVisibility(View.VISIBLE);
        } else holder.tv_subtitle.setVisibility(View.GONE);
        holder.tv_watchs.setText(""+post.custom_fields.views.get(0));
        holder.tv_comments.setText("" + post.comment_count);
        String image_url = post.thumbnail_images!=null ? (post.thumbnail_images.medium!=null ? post.thumbnail_images.medium.url : post.thumbnail_images.full.url) : "http://none.ru/none.jpg";
        Picasso.with(context).load(image_url).error(R.color.material_blue_grey_100).placeholder(R.color.material_blue_grey_100).into(holder.iv_image);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        new Intent(context, EvFragmentContainerActivity.class)
                                .putExtra(EvFragmentContainerActivity.ARG_FRAGMENT_ID, 2)
                                .putExtra(PostWebFragment.ARG_POST_DATA, getItem(position))
                                .putExtra(EvFragmentContainerActivity.ARG_IS_FADING, false)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public PostModel getItem(int position) {
        return items.get(position);
    }

    public void updateItems(ArrayList<PostModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title) EvTextView tv_title;
        @InjectView(R.id.subtitle) EvTextView tv_subtitle;
        @InjectView(R.id.comments) EvTextView tv_comments;
        @InjectView(R.id.watchs) EvTextView tv_watchs;
        @InjectView(R.id.image) ImageView iv_image;
        View root;

        public ViewHolder(View v) {
            super(v);
            root = v;
            ButterKnife.inject(this, v);
        }
    }
}

