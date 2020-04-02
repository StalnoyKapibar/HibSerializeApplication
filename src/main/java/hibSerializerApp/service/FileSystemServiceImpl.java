package hibSerializerApp.service;

import hibSerializerApp.dao.FileSystemDaoImpl;
import hibSerializerApp.dao.abstraction.FileSystemDao;
import hibSerializerApp.service.abstraction.FileSystemService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileSystemServiceImpl implements FileSystemService {
    private final FileSystemDao fileSystemDao;

    public FileSystemServiceImpl() {
        fileSystemDao = new FileSystemDaoImpl();
    }

    @Override
    public byte[] getFileBytes(File file) throws IOException {
        return fileSystemDao.getFileBytes(file);
    }

    @Override
    public List<byte[]> getFilesBytes(List<File> files) throws IOException {
        if (files.isEmpty()) return Collections.emptyList();
        List<byte[]> bytes = new ArrayList<>();
        for (File file : files) {
            bytes.add(fileSystemDao.getFileBytes(file));
        }
        return bytes;
    }
}
