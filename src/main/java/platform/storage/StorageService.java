package platform.storage;

import platform.model.DataDefinition;
import platform.model.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    File storeProjectFile(long experimentId, DataDefinition dataDefId, MultipartFile file);

    File storeTmpFile(MultipartFile file);

    String genFilePathWithRoot(String filePath);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    boolean deleteTmpFile(String filePath);

    void deleteAll();

}
