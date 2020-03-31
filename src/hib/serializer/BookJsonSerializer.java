package hib.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import hib.model.Book;

import java.io.*;

public class BookJsonSerializer {
    public void serialize(Book book, File target) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OutputStream outputStream = new FileOutputStream(target);
        mapper.writeValue(outputStream, book);
    }

    public Book deserialize(File file) throws IOException, ClassNotFoundException {
        return null;
    }
}
