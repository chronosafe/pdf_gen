
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormatFields {

    public static final String SRC = "/Users/chronosafe/Documents/Development/Personal/decodethis/public/pdf_templates/BLANK.pdf";
    public static final String DEST = "/Users/chronosafe/Documents/Development/Personal/decodethis/public/window_stickers/FillFormField.pdf";
    public static final String FDF = "/Users/chronosafe/output.fdf";


    public static void main(String[] args) throws DocumentException, IOException {
        String src = args[0];
        String fdf = args[1];
        String dest = args[2];

        File file = new File(src);
        file.getParentFile().mkdirs();
        new FormatFields().manipulatePdf(src, dest, fdf);
    }

    public void manipulatePdf(String src, String dest, String fdf) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        FdfReader fdfReader = new FdfReader(fdf);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        String name = "";

        AcroFields form = stamper.getAcroFields();
        form.setFields(fdfReader);
        stamper.setFormFlattening(true);
        form.setGenerateAppearances(true);
        Map<String, String> info = reader.getInfo();
        if (form.getField("FullName_1") != null) {
            String name1 = form.getField("FullName_1");
            String name2 = form.getField("FullName_2");
            String name3 = form.getField("FullName_3");
            name = nameOrBlank(name1) + " " + nameOrBlank(name2) + " " + nameOrBlank(name3);
        } else {
            name = form.getField("FullName");
        }

        if (name == null) name = ""; // Prevent nulls from showing up

        info.put("Title", "Window Sticker for VIN " + form.getField("VIN"));
        info.put("Subject", "Window Sticker for " + name);
        info.put("Keywords", "Window Sticker, " + form.getField("VIN") + ", " + name);
        info.put("Creator", "https://www.decodethis.com/window_stickers");
        info.put("Author", nameOrBlank(form.getField("ContactEmail")));
        stamper.setMoreInfo(info);
        stamper.flush();
        stamper.close();
        reader.close();

    }

    public String nameOrBlank(String name) {
        return name == null ? "" : name;
    }

}