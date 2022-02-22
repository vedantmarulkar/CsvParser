import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CsvParser  {
    public static void main(String[] args) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        ParsingService.parse();
        //ParsingService.parse();
        //CompressionService.resizeImage();

        String resizedFolder = "/Users/vedantmarulkar/Documents/Resized Images/";

        File dir = new File(resizedFolder);
        File[] directoryListing = dir.listFiles();
        int i = 0;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.out.println(child.getAbsolutePath());
                if (child.getAbsolutePath().contains("#")) {
                    String newPath = child.getAbsolutePath().split("#")[0].replaceAll("Resized Images", "To Upload");
                    //System.out.println(newPath);
                    new File(newPath).mkdirs();// to make a new directory
                    String newName = child.getAbsolutePath().split("#")[1];
                    String newAbsPath = newPath + "/" + newName;
                    File dest = new File(newAbsPath);
                    try {
                        FileUtils.copyFile(child, dest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(newAbsPath);

                    //System.out.println(child.getName());
                    i++;
                    System.out.println(i);
                    //if (i==2) break;
                }
            }
        }
        System.out.println(i);
    }
}
