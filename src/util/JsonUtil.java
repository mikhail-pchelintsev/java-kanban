package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.DurationAdapter;
import http.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class JsonUtil {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
}
