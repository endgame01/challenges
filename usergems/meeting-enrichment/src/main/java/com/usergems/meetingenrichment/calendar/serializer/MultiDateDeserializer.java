package com.usergems.meetingenrichment.calendar.serializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class MultiDateDeserializer extends StdDeserializer<LocalDateTime> {
    private static final Logger log = LoggerFactory.getLogger(MultiDateDeserializer.class);
    private static final long serialVersionUID = 1L;

    private static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-d HH:mm:ss"
    };

    public MultiDateDeserializer() {
        this(null);
    }

    public MultiDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        final String date = node.textValue();

        for (String DATE_FORMAT : DATE_FORMATS) {
            try {
                return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
            } catch (Exception e) {
                log.warn("failed to deserialize date {} with format {}", date, DATE_FORMAT);
            }
        }
        throw new JsonParseException(jp, "Unparseable date: \"" + date +
                "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}

