package util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Task;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class TaskSerializer implements JsonSerializer<Task> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        @Override
        public JsonElement serialize
                (Task src, Type typeOfSrc, JsonSerializationContext context) {

            JsonObject jObject = new JsonObject();
            jObject.addProperty("type", src.getType());
            jObject.addProperty("name", src.getName());
            jObject.addProperty("info", src.getInfo());
            jObject.addProperty("id", src.getId());
            jObject.addProperty("taskStatus", src.getTaskStatus().toString());
            jObject.addProperty("startTime", src.getStartTime().format(formatter));
            jObject.addProperty("endTime", src.getEndTime().format(formatter));
            return jObject;
        }
    }

