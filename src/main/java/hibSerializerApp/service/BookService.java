package hibSerializerApp.service;

import hibSerializerApp.model.Book;
import hibSerializerApp.model.BookDTO;
import hibSerializerApp.serializer.BookJsonSerializer;

import java.io.File;
import java.io.IOException;

public class BookService {
    private final BookJsonSerializer bookJsonSerializer;

    public BookService() {
        this.bookJsonSerializer = new BookJsonSerializer();
    }

    public void saveBook(Book book, File target) throws IOException {
        bookJsonSerializer.serialize(book, target);
    }

    public Book getBook(File file) throws IOException, ClassNotFoundException {
        return bookJsonSerializer.deserialize(file);
    }

    public BookDTO getBookDTO(File file) {
        return bookJsonSerializer.getBookDTO(file);
    }
}
