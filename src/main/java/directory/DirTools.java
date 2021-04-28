package directory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class DirTools {

    public static List<String> getListOfSubdirs(String path){
        List<String> dirList = new LinkedList<>();
        try {
            if (Files.notExists(Paths.get(path))) return dirList;
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(path));
            for (Path dirObj : dirStream) {
                if (Files.isDirectory(dirObj)) dirList.add(dirObj.getFileName().toString());
            }
            return dirList;
        }catch (IOException e){
            e.printStackTrace();
            return dirList;
        }
    }

    public static List<String> getListOfFiles(String path){
        List<String> fileList = new LinkedList<>();
        try {
            if (Files.notExists(Paths.get(path))) return fileList;

            DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(path));
            for (Path dirObj : dirStream) {
                if (Files.isRegularFile(dirObj)) fileList.add(dirObj.getFileName().toString());
            }
            return fileList;
        }catch (IOException e){
            e.printStackTrace();
            return fileList;
        }
    }

}
