package com.marchal.christophe.phoresttechtest.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserialize dates coming from CSV with non standard format
 */
public class OffsetDateTimeDeserializer extends StdScalarDeserializer<OffsetDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    protected OffsetDateTimeDeserializer() {
        super(OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        TextNode node = jp.getCodec().readTree(jp);
        return OffsetDateTime.from((formatter.parse(node.textValue())));
    }
}
