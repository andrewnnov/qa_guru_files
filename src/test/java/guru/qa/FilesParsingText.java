package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.model.Glossary;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class FilesParsingText {

    ClassLoader cl = FilesParsingText.class.getClassLoader();

    @Disabled
    @Test
    void pdfParseTest() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File downloadPdf =  $("a[href='junit-user-guide-5.10.0.pdf']").download();
        PDF content = new PDF(downloadPdf);
        assertThat(content.author).contains("Sam Brannen");
    }

    @Test
    void xlsxParseTest() throws Exception {
        Selenide.open("https://89.edu.ru/food/");
        File xlsxFile =  $("a[href='2021-05-19-sm.xlsx']").download();
        XLS content = new XLS(xlsxFile);
        assertThat(content.excel.getSheetAt(0).getRow(1).getCell(3).getStringCellValue())
                .contains("Something");
    }

    @Test
    void csvParseTest() throws Exception {
        try(InputStream resource = cl.getResourceAsStream("qa_guru.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(resource))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)[1]).contains("lessons");
        }
    }

    @Test
    void zipParseTest() throws Exception {
        try(InputStream resource = cl.getResourceAsStream("goal.zip");
            ZipInputStream zis = new ZipInputStream(resource);
            ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).isEqualTo("goal.png");
            }
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        Gson gson = new Gson();
        try(
            InputStream resource = cl.getResourceAsStream("glossary.json");
            InputStreamReader reader = new InputStreamReader(resource)) {
               JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
               assertThat(jsonObject.get("title").getAsString()).isEqualTo("example glossary");
               assertThat(jsonObject.get("GlossDiv").getAsJsonObject().get("title").getAsString()).isEqualTo("S");
               assertThat(jsonObject.get("GlossDiv").getAsJsonObject().get("flag").getAsBoolean()).isTrue();
        }
    }

    @Test
    void jsonParseImprovedTest() throws Exception {
        Gson gson = new Gson();
        try(
                InputStream resource = cl.getResourceAsStream("glossary.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            Glossary jsonObject = gson.fromJson(reader, Glossary.class);
            assertThat(jsonObject.title).isEqualTo("example glossary");
            assertThat(jsonObject.glossDiv.title).isEqualTo("S");
            assertThat(jsonObject.glossDiv.flag).isTrue();
        }
    }
}
