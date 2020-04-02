package hibSerializerApp.dao;

import hibSerializerApp.dao.abstraction.FileSystemDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileSystemDaoImpl implements FileSystemDao {

    @Override
    public byte[] getFileBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
