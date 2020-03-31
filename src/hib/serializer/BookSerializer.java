package hib.serializer;

import hib.model.Book;

import java.io.*;

public class BookSerializer {
    public void serialize(Book book, File target) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(target));
        objectOutputStream.writeObject(book);
        objectOutputStream.close();
    }

    public Book deserialize(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        Book book =  (Book) objectInputStream.readObject();
        objectInputStream.close();
        return book;
    }
}
