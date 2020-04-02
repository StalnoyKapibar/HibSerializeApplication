package hibSerializerApp.dao.abstraction;

import hibSerializerApp.model.Book;
import hibSerializerApp.model.BookDTO;

import java.io.File;
import java.io.IOException;

public interface BookSerializer {
    void serialize(Book book, File target) throws IOException;

    Book deserialize(File file) throws IOException, ClassNotFoundException;

    BookDTO getBookDTO(File file);
}
