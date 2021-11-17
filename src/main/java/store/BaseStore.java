package store;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class BaseStore<T extends Serializable> {
    public String baseName = "";

    public T putItem(String key, T item) throws IOException {

        key = key.replace("\\", "_").replace("/", "_");
        Path baseDir = new File(getClass().getClassLoader()
                .getResource("")
                .getFile()).toPath();

        //byte[] decode = Base64.getDecoder().decode(key.getBytes());


        Path newFolder = Paths.get(baseDir.toAbsolutePath() + "/" + baseName + "/");
        Files.createDirectories(newFolder);

        Path newFile = Paths.get(newFolder.toAbsolutePath() + "/" + key);

        FileOutputStream fileOutputStream
                = new FileOutputStream(newFile.toFile());
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(item);
        objectOutputStream.flush();
        objectOutputStream.close();

        return item;
    }

    public T getItem(String key) throws IOException, ClassNotFoundException {
        key = key.replace("\\", "_").replace("/", "_");
        Path baseDir = new File(getClass().getClassLoader()
                .getResource("")
                .getFile()).toPath();
        Path file = Paths.get(baseDir.toAbsolutePath() + "/" + baseName + "/" + key);

        System.out.println(file.toAbsolutePath());

        FileInputStream fileInputStream = new FileInputStream(file.toFile());

        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        T item = (T) objectInputStream.readObject();
        objectInputStream.close();
        return item;
    }

}
