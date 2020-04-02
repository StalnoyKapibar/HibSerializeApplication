package hibSerializerApp.dao.abstraction;

import java.io.File;
import java.io.IOException;

public interface FileSystemDao {
    byte[] getFileBytes(File file) throws IOException;
}
