package hibSerializerApp.service.abstraction;

import hibSerializerApp.model.Book;
import hibSerializerApp.model.BookDTO;

import java.io.File;
import java.io.IOException;

public interface BookService {
    void saveBook(Book book, File target) throws IOException;

    Book getBook(File file) throws IOException, ClassNotFoundException;

    BookDTO getBookDTO(File file);
}
