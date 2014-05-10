package com.screwattack.sgcapp.utils;

import android.app.Activity;
import android.graphics.*;
import android.provider.Settings;
import com.google.gson.Gson;
import com.screwattack.sgcapp.objects.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by juanreyes on 4/5/14.
 */
public class Utils {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
    public static final SimpleDateFormat calendarFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);

    public static Calendar stringToCalendar(String strDate) throws ParseException {
        Date date = calendarFormat.parse(strDate);

        Calendar cal = Calendar.getInstance(Locale.US);

        cal.setTime(date);

        return cal;
    }

    public static String getFormatedTime(Calendar date) {
        return dateFormat.format(date.getTime());
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static String getDeviceID(Activity activity) {
        return  "Android_" + Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static class ResultParser {
        private JSONObject jsonObject;

        public ResultParser(JSONObject _jsonObject) {
            this.jsonObject = _jsonObject;
        }

        private String checkParams(String params, JSONObject _jsonObject) {
            String returnedParam = null;

            String[] paramsArray = params.split(",");

            for(String item : paramsArray)
                if (_jsonObject.has(item))
                    returnedParam = item;

            return returnedParam;
        }

        public boolean hasValue(String paramName) {
            return hasValue(paramName, jsonObject);
        }

        public boolean hasValue(String param, JSONObject _jsonObject) {
            boolean value = false;

            param = checkParams(param, _jsonObject);

            if (param != null && _jsonObject.has(param))
                try {
                    value = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            return value;
        }

        public JSONArray getJSONArrayValue(String paramName) {
            return getJSONArrayValue(paramName, null, jsonObject);
        }

        public JSONArray getJSONArrayValue(String paramName, JSONObject _jsonObject) {
            return getJSONArrayValue(paramName, null, _jsonObject);
        }

        public JSONArray getJSONArrayValue(String paramName, JSONArray alternateValue) {
            return getJSONArrayValue(paramName, alternateValue, jsonObject);
        }

        public JSONArray getJSONArrayValue(String param, JSONArray alternateValue, JSONObject _jsonObject) {
            JSONArray value = alternateValue;

            param = checkParams(param, _jsonObject);

            if (param != null && _jsonObject.has(param))
                try {
                    value = _jsonObject.getJSONArray(param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return value;
        }

        public JSONObject getJSONObjectValue(String paramName) {
            return getJSONObjectValue(paramName, jsonObject);
        }

        public JSONObject getJSONObjectValue(String param, JSONObject _jsonObject) {
            JSONObject value = null;

            param = checkParams(param, _jsonObject);

            if (param != null && _jsonObject.has(param))
                try {
                    value = _jsonObject.getJSONObject(param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return value;
        }

        public String getStringValue(String paramName) {
            return getStringValue(paramName, null, jsonObject);
        }

        public String getStringValue(String paramName, String alternateValue) {
            return getStringValue(paramName, alternateValue, jsonObject);
        }

        public String getStringValue(String paramName, JSONObject _jsonObject) {
            return getStringValue(paramName, null, _jsonObject);
        }

        public String getStringValue(String param, String alternateValue, JSONObject _jsonObject) {
            String value = alternateValue;

            param = checkParams(param, _jsonObject);

            if (param != null && _jsonObject.has(param))
                try {
					/*Object unidentifiedValue = _jsonObject.get(param);

					if (unidentifiedValue != null && unidentifiedValue instanceof String)
						value = (String) unidentifiedValue;
					else
						value = null;*/
                    value = _jsonObject.getString(param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return value;
        }

        public ArrayList<String> getStringArray(
                ResultParser resultParser,
                String arrayParam,
                String childParam,
                String childParamOld) {
            ArrayList<String> result = null;

            JSONArray jsonArray =
                    resultParser.getJSONArrayValue(arrayParam);

            if(jsonArray != null) {
                result = new ArrayList<String>();

                for (int x = 0; x < jsonArray.length(); x++)
                    try {
                        result.add(resultParser.getStringValue(childParam, childParamOld, new JSONObject(jsonArray.getString(x))));
                    } catch (Exception e) { e.printStackTrace(); }
            }

            return result;
        }

        public int getIntValue(String paramName) {
            return getIntValue(paramName, 0, jsonObject);
        }

        public int getIntValue(String paramName, JSONObject _jsonObject) {
            return getIntValue(paramName, 0, _jsonObject);
        }

        public int getIntValue(String paramName, int alternateValue) {
            return getIntValue(paramName, alternateValue, jsonObject);
        }

        public int getIntValue(String param, int alternateValue, JSONObject _jsonObject) {
            int value = alternateValue;

            param = checkParams(param, _jsonObject);

            if (param != null && _jsonObject.has(param))
                try {
					/*Object unidentifiedValue = _jsonObject.get(param);

					if (unidentifiedValue != null && unidentifiedValue instanceof Integer)
						value = (Integer) unidentifiedValue;
					else
						value = 0;*/
                    value = _jsonObject.getInt(param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return value;
        }

        public double getDoubleValue(String paramName) {
            return getDoubleValue(paramName, 0.0D);
        }

        public double getDoubleValue(String param, double alternateValue) {
            double value = alternateValue;

            param = checkParams(param, jsonObject);

            if (param != null && jsonObject.has(param))
                try {
					/*Object unidentifiedValue = jsonObject.get(param);

					if (unidentifiedValue != null && unidentifiedValue instanceof Double)
						value = (Double) unidentifiedValue;
					else
						value = 0.0D;*/
                    value = jsonObject.getDouble(param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return value;
        }

        public long getLongValue(String paramName) {
            return getLongValue(paramName, 0L);
        }

        public long getLongValue(String param, long alternateValue) {
            long value = alternateValue;

            param = checkParams(param, jsonObject);

            if (param != null && jsonObject.has(param))
                try {
					/*Object unidentifiedValue = jsonObject.get(param);

					if (unidentifiedValue != null && unidentifiedValue instanceof Long)
						value = (Long) unidentifiedValue;
					else
						value = 0L;*/
                    value = jsonObject.getLong(param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return value;
        }

        public boolean getBoolValue(String paramName) {
            return getBoolValue(paramName, false);
        }

        public boolean getBoolValue(String param, boolean alternateValue) {
            boolean value = alternateValue;

            param = checkParams(param, jsonObject);

            if (param != null && jsonObject.has(param))
                try {
                    String valueString = jsonObject.getString(param);

                    if (valueString.equalsIgnoreCase("true") || valueString.equalsIgnoreCase("1"))
                        value = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return value;
        }
    }

    public static float getDistanceFromPoints (float lat_a, float lng_a, float lat_b, float lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    public static void saveUser(User user, Activity activity) {
        if (user != null) {
            Gson gson = new Gson();

            String json = gson.toJson(user);

            activity.getSharedPreferences("SGC_USER", Activity.MODE_PRIVATE).edit().putString("CURRENT_SGC_USER", json).commit();
        }
    }

    public static User loadUser(Activity activity) {
        Gson gson = new Gson();

        String json = activity.getSharedPreferences("SGC_USER", Activity.MODE_PRIVATE).getString("CURRENT_SGC_USER", null);

        if (json != null)
            return gson.fromJson(json, User.class);

        return null;
    }

    public static void removeUser(Activity activity) {
        activity.getSharedPreferences("SGC_USER", Activity.MODE_PRIVATE).edit().remove("CURRENT_SGC_USER").commit();
    }

    public static boolean userIsLogued(Activity activity) {
        if (activity.getSharedPreferences("SGC_USER", Activity.MODE_PRIVATE).contains("CURRENT_SGC_USER")) {
            String json = activity.getSharedPreferences("SGC_USER", Activity.MODE_PRIVATE).getString("CURRENT_SGC_USER", null);

            if (json != null && json.trim().length() > 10)
                return true;
            else {
                removeUser(activity);

                return false;
            }
        } else
            return false;
    }
}
