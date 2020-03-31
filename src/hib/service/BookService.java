package hib.service;

import hib.model.Book;
import hib.serializer.BookSerializer;

import java.io.File;
import java.io.IOException;

public class BookService {
    private final BookSerializer bookSerializer;

    public BookService() {
        this.bookSerializer = new BookSerializer();
    }

    public void saveBook(Book book, File target) throws IOException {
        bookSerializer.serialize(book, target);
    }

    public Book getBook(File file) throws IOException, ClassNotFoundException {
        return bookSerializer.deserialize(file);
    }
}
