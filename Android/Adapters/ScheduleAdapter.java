package com.screwattack.sgcapp.adapters;

import android.app.AlarmManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.screwattack.sgcapp.BaseActivity;
import com.screwattack.sgcapp.R;
import com.screwattack.sgcapp.objects.Autor;
import com.screwattack.sgcapp.objects.SGCActivity;
import com.screwattack.sgcapp.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by juanreyes on 4/4/14.
 */
public class ScheduleAdapter extends ArrayAdapter<SGCActivity> {

    private List<SGCActivity> sgcActivityList;
    private List<SGCActivity> filteredList;
    private LayoutInflater inflater;

    public static String dayFilter;

    private static int rowCount;

    public static enum validScopes {
        NAME,
        TIME,
        GUEST,
        ROOM;
    }

    public static validScopes scope;

    public ScheduleAdapter(Context context, int layoutResourceId, List<SGCActivity> objects) {
        super(context, layoutResourceId, objects);

        //this.layoutResourceId = layoutResourceId;
        this.inflater = LayoutInflater.from(context);
        this.sgcActivityList = objects;
        this.filteredList = objects;
        rowCount = objects.size();

        dayFilter = "All";
    }

    public void updateData(List<SGCActivity> sgcActivityList) {
        if (this.filteredList.size() == rowCount) {
            this.filteredList = sgcActivityList;
            rowCount = sgcActivityList.size();
        }

        this.sgcActivityList = sgcActivityList;

        applyFilter(true, this.sgcActivityList);

        this.notifyDataSetChanged();
    }

    public void applyFilter(boolean isFromService, List<SGCActivity> sgcActivityList) {
        if (!dayFilter.equalsIgnoreCase("All")) {
            List<SGCActivity> dayFilteredList = new ArrayList<SGCActivity>(sgcActivityList.size());

            for (SGCActivity sgcActivity : sgcActivityList) {
                if (sgcActivity.getActivityDay().equalsIgnoreCase(dayFilter))
                    dayFilteredList.add(sgcActivity);
            }

            this.sgcActivityList = dayFilteredList;

            if (!isFromService) {
                this.filteredList = dayFilteredList;

                this.notifyDataSetChanged();
            }
        } else {
            if (!isFromService) {
                this.sgcActivityList = sgcActivityList;
                this.filteredList = sgcActivityList;

                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getCount() {
        if (filteredList != null)
            return filteredList.size();
        else
            return 0;
    }

    @Override
    public SGCActivity getItem(int position) {
        if (filteredList != null && filteredList.size() > position)
            return filteredList.get(position);
        else
            return null;
    }

    private class ViewHolder {
        TextView header;
        TextView name;
        TextView description;
        TextView location;
        TextView time;
        ImageView alert;
        ImageView stream;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // inflate the layout
            convertView = inflater.inflate(R.layout.schedule_row, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();

            if (convertView != null) {
                viewHolder.header = (TextView) convertView.findViewById(R.id.header_textView);
                viewHolder.name = (TextView) convertView.findViewById(R.id.activity_name_textView);
                viewHolder.description = (TextView) convertView.findViewById(R.id.activity_desc_textView);
                viewHolder.location = (TextView) convertView.findViewById(R.id.activity_location_textView);
                viewHolder.time = (TextView) convertView.findViewById(R.id.activity_time_textView);
                viewHolder.alert = (ImageView) convertView.findViewById(R.id.reminder_imageView);
                viewHolder.stream = (ImageView) convertView.findViewById(R.id.stream_imageView);

                // store the holder with the view.
                convertView.setTag(viewHolder);
            }
        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SGCActivity activity = filteredList.get(position);

        if (activity != null) {
            if (activity.isHeader() && position > 0) {
                if (convertView != null)
                    convertView.findViewById(R.id.schedule_header_include).setVisibility(RelativeLayout.VISIBLE);

                viewHolder.header.setText(activity.getActivityDay());
            } else
                if (convertView != null)
                    convertView.findViewById(R.id.schedule_header_include).setVisibility(RelativeLayout.GONE);

            viewHolder.name.setText(activity.getActivityName());

            if (activity.isCurrent())
                viewHolder.name.setTextColor(getContext().getResources().getColor(R.color.default_red));
            else
                viewHolder.name.setTextColor(getContext().getResources().getColor(R.color.default_white));

            viewHolder.description.setText(activity.getActivityDesc());
            viewHolder.location.setText(activity.getLocation());

            String activityTime =
                    Utils.getFormatedTime(activity.getStartDate()) +
                            " - " +
                            Utils.getFormatedTime(activity.getEndDate());

            viewHolder.time.setText(activityTime);

            if (getContext().getSharedPreferences("SGC_ALERT", Context.MODE_PRIVATE).getBoolean(activity.getActivityID(), false))
                viewHolder.alert.setVisibility(ImageView.VISIBLE);
            else
                viewHolder.alert.setVisibility(ImageView.GONE);

            if (activity.getTwitch() != null && activity.getTwitch().length() > 0)
                viewHolder.stream.setVisibility(ImageView.VISIBLE);
            else
                viewHolder.stream.setVisibility(ImageView.GONE);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if (charSequence == null || charSequence.length() == 0) {
                    results.values = sgcActivityList;
                    results.count = sgcActivityList.size();
                } else {
                    ArrayList<SGCActivity> filterResultsData = new ArrayList<SGCActivity>();

                    Autor autor;

                    for (SGCActivity activity : sgcActivityList) {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional

                        if (scope == null)
                            scope = validScopes.NAME;

                        switch (scope) {
                            case TIME:
                                if (String.valueOf(activity.getStartDate().get(Calendar.HOUR)).equalsIgnoreCase(charSequence.toString()))
                                    filterResultsData.add(activity);
                                break;
                            case GUEST:
                                autor = BaseActivity.findAutorFromID(activity.getActivityAutorID());

                                if (autor.getAutorName().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    filterResultsData.add(activity);
                                break;
                            case ROOM:
                                if (activity.getLocation().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    filterResultsData.add(activity);
                                break;
                            default:
                                if (activity.getActivityName().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    filterResultsData.add(activity);
                                break;
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.values instanceof ArrayList<?>)
                    filteredList = (ArrayList<SGCActivity>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }
}
