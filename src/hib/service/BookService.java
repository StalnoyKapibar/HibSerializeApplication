package hib.service;

import hib.model.Book;
import hib.serializer.BookJsonSerializer;

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
}
