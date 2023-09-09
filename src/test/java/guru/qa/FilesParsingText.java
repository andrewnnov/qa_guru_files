package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FilesParsingText {

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
}
