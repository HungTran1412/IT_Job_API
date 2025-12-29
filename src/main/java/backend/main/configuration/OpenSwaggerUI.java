package backend.main.configuration;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class OpenSwaggerUI {
    private final String baseUrl;

    public OpenSwaggerUI(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openSwaggerURL() {
        String url = baseUrl + "/swagger-ui/index.html";
        String os = System.getProperty("os.name").toLowerCase();

        try {
            // Trường hợp 1: Hỗ trợ Desktop API (Chuẩn Java)
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(url));
                log.info("Opened Swagger URL via Desktop API: {}", url);
            }
            // Trường hợp 2: Ép buộc chạy trên Windows (Nếu Desktop API fail)
            else if (os.contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                log.info("Opened Swagger URL via Windows Command: {}", url);
            }
            // Trường hợp 3: Ép buộc chạy trên Mac/Linux (Tùy chọn thêm)
            else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("xdg-open " + url);
            }
            else {
                log.warn("Can not Open Swagger URL automatically. Please visit: {}", url);
            }
        } catch (Exception e) {
            log.error("Failed to open Swagger URL", e);
        }
    }
}