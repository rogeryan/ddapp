package cn.edu.scujcc.diandian;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义的JSON日期格式转换类。
 * 目标日期格式：2020-07-16 19:03:53
 */
public class MyDateAdapter {
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ToJson
    String toJson(Date dt) {
        return dateFormat.format(dt);
    }

    @FromJson
    Date fromJson(String jsonDt) throws ParseException {
        return dateFormat.parse(jsonDt);
    }
}
