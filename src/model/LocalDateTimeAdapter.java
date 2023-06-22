package model;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

    public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException, NullPointerException {
            jsonWriter.value((localDateTime.format(formatter)));

        }
        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException, NullPointerException  {
            return LocalDateTime.parse(jsonReader.nextString(), formatter);
        }
    }
