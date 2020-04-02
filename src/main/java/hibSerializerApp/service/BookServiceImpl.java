package hibSerializerApp.service;

import hibSerializerApp.dao.BookJsonSerializer;
import hibSerializerApp.model.Book;
import hibSerializerApp.model.BookDTO;
import hibSerializerApp.service.abstraction.BookService;

import java.io.File;
import java.io.IOException;

public class BookServiceImpl implements BookService {
    private final BookJsonSerializer bookJsonSerializer;

    public BookServiceImpl() {
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
