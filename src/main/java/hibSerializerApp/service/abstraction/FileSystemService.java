package hibSerializerApp.service.abstraction;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileSystemService {

    byte[] getFileBytes(File file) throws IOException;

    List<byte[]> getFilesBytes(List<File> files) throws IOException;
}
