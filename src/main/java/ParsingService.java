import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.List;

public class ParsingService {

    public static void parse() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String fileName = "/Users/vedantmarulkar/Documents/New_Query_2022_02_17.csv";

        String output = "/Users/vedantmarulkar/Documents/output.csv";

        List<LinkDTO> beans = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(LinkDTO.class)
                .build()
                .parse();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        int i = 1;
        for (LinkDTO bean : beans) {
            Request request = new Request.Builder()
                    .url(bean.getImage_url())
                    .method("HEAD", null)
                    .build();
            Response response = client.newCall(request).execute();
            String sizeInBytes = response.header("content-length");
            bean.setSize_in_bytes(sizeInBytes);
            System.out.println(i++);
        }

        Writer writer  = new FileWriter(output);

        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

        beanToCsv.write(beans);
        writer.close();
    }
}
