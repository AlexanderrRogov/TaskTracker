package model;

import java.util.Arrays;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Сохранение в файл не удалось. Ошибка: " + getMessage() + "СтакТрейс: " + Arrays.toString(getStackTrace());
    }
}
