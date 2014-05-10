package com.screwattack.sgcapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.screwattack.sgcapp.DetailsActivity;
import com.screwattack.sgcapp.R;
import com.screwattack.sgcapp.objects.SGCActivity;
import com.screwattack.sgcapp.utils.IImageLoader;
import com.screwattack.sgcapp.utils.ImageLoader;
import com.screwattack.sgcapp.utils.Utils;

import java.util.List;

/**
 * Created by juanreyes on 4/23/14.
 */
public class SchedulePageAdapter extends PagerAdapter implements IImageLoader, View.OnClickListener {

    Context context;
    private List<SGCActivity> sgcActivityList;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public SchedulePageAdapter(Context context, int resource, List<SGCActivity> objects) {
        this.sgcActivityList = objects;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.imageLoader = new ImageLoader(context, this);
    }

    @Override
    public int getCount() {
        if (sgcActivityList != null)
            return sgcActivityList.size();
        else
            return 0;
    }

    public void updateData(List<SGCActivity> sgcActivityList) {
        this.sgcActivityList = sgcActivityList;
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void onImageLoaded(ImageView imageView, Bitmap bitmap) {

    }

    @Override
    public void onClick(View view) {
        try {
            Intent intent = new Intent(context, DetailsActivity.class);

            intent.putExtra(DetailsActivity.activityParam, (SGCActivity) view.findViewById(R.id.image_imageView).getTag());

            context.startActivity(intent);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private class ViewHolder {
        TextView name;
        TextView description;
        TextView location;
        TextView time;
        ImageView image;
        ImageView stream;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder viewHolder;

        // inflate the layout
        View convertView = inflater.inflate(R.layout.activity_window_layout, container, false);

        // well set up the ViewHolder
        viewHolder = new ViewHolder();

        if (convertView != null) {
            viewHolder.name = (TextView) convertView.findViewById(R.id.name_textView);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description_textView);
            viewHolder.location = (TextView) convertView.findViewById(R.id.location_textView);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time_textView);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image_imageView);
            viewHolder.stream = (ImageView) convertView.findViewById(R.id.stream_indicator_imageView);
        }

        SGCActivity activity = sgcActivityList.get(position);

        if (activity != null) {
            viewHolder.name.setText(activity.getActivityName());
            viewHolder.image.setTag(activity);
            viewHolder.description.setText(activity.getActivityDesc());
            viewHolder.location.setText(activity.getLocation());

            String activityTime =
                    Utils.getFormatedTime(activity.getStartDate()) +
                            " - " +
                            Utils.getFormatedTime(activity.getEndDate());

            viewHolder.time.setText(activityTime);

            imageLoader.DisplayImage(activity.getImageURL(), viewHolder.image);
            viewHolder.image.setOnClickListener(this);

            if (activity.getTwitch() != null && activity.getTwitch().length() > 0)
                viewHolder.stream.setVisibility(ImageView.VISIBLE);
            else
                viewHolder.stream.setVisibility(ImageView.GONE);
        }

        if (convertView != null) {
            if (activity != null && activity.getActivityID() != null)
                convertView.setOnClickListener(this);

            container.addView(convertView, 0);
        }

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
