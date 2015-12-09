package com.spontaneous.android.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.spontaneous.android.R;
import com.spontaneous.android.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * This is adapter auto completes places from the Google Places API.
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private final Context context;

    private static final String BASE_PLACES_API_URL = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTO_COMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private List<String> mResultList;

    public PlacesAutoCompleteAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mResultList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public String getItem(int index) {
        return mResultList.get(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    // Retrieve the autocomplete mResultList.
                    mResultList = autoComplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = mResultList;
                    filterResults.count = mResultList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private List<String> autoComplete(String input) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {

            input = URLEncoder.encode(input, "utf8");

            final String API_KEY = context.getString(R.string.google_api_key);

            URL url = new URL(BASE_PLACES_API_URL + TYPE_AUTO_COMPLETE + OUT_JSON + "?key=" + API_KEY + "&input=" + input);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the mResultList into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

        } catch (MalformedURLException e) {
            Logger.error("Error processing Places API URL");
        } catch (IOException e) {
            Logger.error("Error connecting to Places API");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the mResultList
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the mResultList
            mResultList = new ArrayList<>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                mResultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Logger.error("Cannot process JSON mResultList");
        }

        return mResultList;
    }
}
