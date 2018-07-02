import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {


    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + File.separator +"FENDEC80_061201_MMRG.DOC"));
        IConverter converter = LocalConverter.builder()
                .baseFolder(new File(System.getProperty("user.dir") + File.separator +""))
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();

        Future<Boolean> conversion = converter
                .convert(in).as(DocumentType.DOC)
                .to(bo).as(DocumentType.DOCX)
                .prioritizeWith(1000) // optional
                .schedule();
        conversion.get();
        try (OutputStream outputStream = new FileOutputStream("out.pdf")) {
            bo.writeTo(outputStream);
        }
        in.close();
        bo.close();
    }


}
