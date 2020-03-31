package hib.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import hib.model.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BookJsonSerializer {
    private final ObjectMapper mapper;

    public BookJsonSerializer() {
        mapper = new ObjectMapper();
    }

    public void serialize(Book book, File target) throws IOException {
        OutputStream outputStream = new FileOutputStream(target);
        mapper.writeValue(outputStream, book);
        outputStream.close();
    }

    public Book deserialize(File file) throws IOException, ClassNotFoundException {
        return mapper.readValue(file, Book.class);
    }
}
