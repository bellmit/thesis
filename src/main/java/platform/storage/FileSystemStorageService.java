package platform.storage;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.stream.Stream;

import platform.model.DataDefinition;
import platform.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    /**
     * Returns the passed filepath with the root directory prefixed.
     * @param filePath  File path relative to the root.
     * @return A filepath with the root directory prefixed.
     */
    @Override
    public String genFilePathWithRoot(String filePath) {
        return this.rootLocation.resolve(filePath).toString();
    }

    /**
     * Stores a project file related to a project and data subject.
     * @param experimentId
     * @param dataDef
     * @param file
     * @return
     */
    @Override
    public File storeProjectFile(long experimentId, DataDefinition dataDef, MultipartFile file) {
        String fileDirPath = "" + experimentId + "/";

        String fullFilename = StringUtils.cleanPath(file.getOriginalFilename());

        int firstPeriod = fullFilename.indexOf(".");

        String fileExtension = "";

        if(firstPeriod != -1) {
            fileExtension = fullFilename.substring(firstPeriod);
        }

        fullFilename = StringUtils.cleanPath(dataDef.getName() + fileExtension);

        int version = findFileVersion(fileDirPath, fullFilename);
        String filePath = genVersionFilePath(fileDirPath, version, fullFilename);

        return storeFile(file, fileDirPath, fullFilename, version, filePath);
    }

    /**
     * Stores a temporary file, i.e. reading of real-time data.
     * @param file
     * @return
     */
    @Override
    public File storeTmpFile(MultipartFile file) {
        String fileDirPath = StorageConstants.TEMPORARY_DIRECTORY + "/";
        String fullFilename = StringUtils.cleanPath(file.getOriginalFilename());

        int firstPeriod = fullFilename.indexOf(".");

        String fileExtension = "";

        if(firstPeriod != -1) {
            fileExtension = fullFilename.substring(firstPeriod);
        }

        fullFilename = StringUtils.cleanPath("tmp" + fileExtension);

        int version = findFileVersion(fileDirPath, fullFilename);
        String filePath = genVersionFilePath(fileDirPath, version, fullFilename);

        return storeFile(file, fileDirPath, fullFilename, version, filePath);
    }


    /**
     * Stores the passed file with given name, path and version in the file system and generates a File object
     * that is returned.
     * @param file The actual file.
     * @param fileDirPath The file directory path.
     * @param filename The full name of the file.
     * @param version The version.
     * @param filePath The full file path excluding the root.
     * @return A File object that has the file path, version and uploaded date time set.
     */
    private File storeFile(MultipartFile file, String fileDirPath, String filename, int version, String filePath) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to storeProjectFile empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot storeProjectFile file with relative path outside current directory "
                                + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                new java.io.File(getAbsolutePath(fileDirPath)).mkdirs();

                Files.copy(inputStream, this.rootLocation.resolve(filePath),
                        StandardCopyOption.REPLACE_EXISTING);

                File fileData = new File();

                fileData.setFilePath(filePath); // The file path excluding the root
                fileData.setVersion(version);
                fileData.setUploadDateTime(new Date());

                return fileData;
            }
        }
        catch (IOException e) {
            try {
                Files.deleteIfExists(this.rootLocation.resolve(filePath));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            throw new StorageException("Failed to storeProjectFile file " + filename, e);
        }
    }

    private String getAbsolutePath(String relativePath) {
        return System.getProperty("user.dir") + "/"
                + this.rootLocation.toString() + "/"
                + relativePath;
    }

    /**
     * Returns a file version number. If the file already exists, it's next version number is returned.
     * @param fileDirPath
     * @param fullFilename
     * @return
     */
    private int findFileVersion(String fileDirPath, String fullFilename) {
//        int version = 1;

//        while(Files.exists(this.rootLocation.resolve(genVersionFilePath(fileDirPath, version, fullFilename)))) {
//            version++;
//        }

        int firstPeriod = fullFilename.indexOf(".");

        String passedFilename = fullFilename.substring(0, firstPeriod);

        java.io.File root = new java.io.File(getAbsolutePath(fileDirPath));
        FilenameFilter beginsWith = new FilenameFilter()
        {
            public boolean accept(java.io.File directory, String filename) {
                return filename.startsWith(passedFilename);
            }
        };

        java.io.File[] files = root.listFiles(beginsWith);

        return files == null ? 1 : files.length + 1;
    }

    /**
     * Generates the file path to store the file. Adds versioning to the filename.
     * @param dirPath
     * @param version
     * @param fullFilename
     * @return
     */
    private String genVersionFilePath(String dirPath, int version, String fullFilename) {
        int firstPeriod = fullFilename.indexOf(".");

        String filename = fullFilename.substring(0, firstPeriod);
        String extension = fullFilename.substring(firstPeriod);

        return dirPath + filename + " v" + version + extension;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    /**
     * Deletes a file from the tmp folder.
     * @param filePath The filepath excluding the root directory.
     * @return Whether the file has been deleted.
     */
    @Override
    public boolean deleteTmpFile(String filePath) {
        try {
            return Files.deleteIfExists(this.rootLocation.resolve(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
