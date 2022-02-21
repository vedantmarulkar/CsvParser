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
        String inputFilePath = "input file path";

        String outputFilePath = "output file path";

        List<CsvColumns> beans = new CsvToBeanBuilder(new FileReader(inputFilePath))
                .withType(CsvColumns.class)
                .build()
                .parse();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        int i = 1;
        for (CsvColumns bean : beans) {//Parsing each row of CSV
            Request request = new Request.Builder()
                    .url(bean.getImage_url())
                    .method("HEAD", null)
                    .build();
            Response response = client.newCall(request).execute();
            String sizeInBytes = response.header("content-length");
            bean.setSize_in_bytes(sizeInBytes);
        }

        //Write to output file
        Writer writer  = new FileWriter(outputFilePath);

        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

        beanToCsv.write(beans);
        writer.close();
    }
}
