package cn.edu.scujcc.diandian;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateAdapter {
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ToJson
    String toJson(Date d) {
        return dateFormat.format(d);
    }

    @FromJson
    Date fromJson(String s) throws ParseException {
        return dateFormat.parse(s);
    }
}
