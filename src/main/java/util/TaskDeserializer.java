package util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import model.Task;
import model.TaskStatus;
import model.TaskType;

import java.lang.reflect.Type;

public class TaskDeserializer implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObject = json.getAsJsonObject();
        String name = jObject.get("name").getAsString();
        String info = jObject.get("info").getAsString();
        TaskStatus taskStatus = TaskStatus.valueOf(jObject.get("taskStatus").getAsString());
        String startTime = jObject.get("startTime").getAsString();
        String duration = jObject.get("duration").getAsString();
        return new Task(name, info, taskStatus, startTime, duration );
    }


}
