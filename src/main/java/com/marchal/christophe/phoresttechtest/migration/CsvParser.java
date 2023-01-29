package com.marchal.christophe.phoresttechtest.migration;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class CsvParser {

    public <T> List<T> parseCsv(Class<T> clazz, InputStream inputStream) throws IOException {
        CsvSchema headerSchema = CsvSchema.emptySchema().withHeader();
        final CsvMapper mapper = new CsvMapper();
        MappingIterator<T> it = mapper
                .readerFor(clazz)
                .with(headerSchema)
                .readValues(inputStream);
        return it.readAll();
    }
}
