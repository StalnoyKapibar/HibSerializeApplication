package hibSerializerApp.dao;


import com.fasterxml.jackson.databind.ObjectMapper;
import hibSerializerApp.dao.abstraction.BookSerializer;
import hibSerializerApp.model.Book;
import hibSerializerApp.model.BookDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BookJsonSerializer implements BookSerializer {
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

    public BookDTO getBookDTO(File file) {
        try {
            BookDTO result = mapper.readValue(file, BookDTO.class);
            result.setLocation(file);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
