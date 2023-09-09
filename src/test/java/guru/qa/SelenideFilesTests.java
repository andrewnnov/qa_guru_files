package guru.qa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SelenideFilesTests {

    //если по кнопке нет ссылки href
//    static {
//        Configuration.fileDownload = FileDownloadMode.PROXY;
//    }

    @Disabled
    @Test
    void selenideDownloadTest() throws Exception {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadFile = $("[data-testid=raw-button]").download();

        try(InputStream inputStream = new FileInputStream(downloadFile)) {
            byte[] bytes = inputStream.readAllBytes();
            String textContent = new String(bytes, StandardCharsets.UTF_8);
            assertThat(textContent).contains("This repository is the home of _JUnit 5_.");
        }
    }

    @Test
    void selenideUploadFile() {
        Selenide.open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("goal.png");
        $("div .qq-file-name").shouldHave(Condition.text("goal.png"));

    }
}
