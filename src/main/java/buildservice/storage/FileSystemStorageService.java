package buildservice.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import java.io.FileInputStream;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path tbxLocation;
    private final Path ctfLocation;

    @Autowired
    public FileSystemStorageService(@Value("${tbxlocation}") Path tbxLocation,
                                    @Value("${ctflocation}") Path ctfLocation) {
        this.tbxLocation =tbxLocation;
        this.ctfLocation = ctfLocation;
    }

    @Override
    public void storeTbx(MultipartFile tbx) {
        String filename = StringUtils.cleanPath(tbx.getOriginalFilename());
        try {
            if (tbx.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = tbx.getInputStream()) {
                Files.copy(inputStream, this.tbxLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public void storeCtf(File ctf, String filename) {
            try (InputStream inputStream = new FileInputStream(ctf)) {
                Files.copy(inputStream, this.ctfLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }


    @Override
    public Path loadCtf(String filename) {
        return ctfLocation.resolve(filename);
    }

    @Override
    public Path loadTbx(String filename) {
        return tbxLocation.resolve(filename);
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(tbxLocation.toFile());
        FileSystemUtils.deleteRecursively(ctfLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(ctfLocation);
            Files.createDirectories(tbxLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
