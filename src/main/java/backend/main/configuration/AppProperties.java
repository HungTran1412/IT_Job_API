package backend.main.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private String baseUrl;

    private Verify verify = new Verify();

    @Getter
    @Setter
    public static class Verify {
        private String candidate;
        private String employer;
    }
}

