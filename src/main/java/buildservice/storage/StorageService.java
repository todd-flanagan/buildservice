package buildservice.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;
import java.io.File;

public interface StorageService {

    void init();

    void storeTbx(MultipartFile file);
    void storeCtf(File file, String filename);

    Path loadCtf(String filename);
    Path loadTbx(String filename);

    void deleteAll();

}
