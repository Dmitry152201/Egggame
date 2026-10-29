package com.egggame.storage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Адаптер Gson для сериализации/десериализации LocalDateTime.
 * Решает проблему модульной системы Java 9+, которая блокирует рефлексию к java.time.
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    /** Формат ISO-8601 для хранения дат в JSON */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Записывает LocalDateTime в JSON как строку.
     *
     * @param out   поток записи
     * @param value дата для записи (может быть null)
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(FORMATTER));
        }
    }

    /**
     * Читает LocalDateTime из JSON-строки.
     *
     * @param in поток чтения
     * @return десериализованная дата или null
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return LocalDateTime.parse(in.nextString(), FORMATTER);
    }
}