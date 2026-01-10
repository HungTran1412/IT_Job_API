package backend.main.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private String baseUrl;

    private Verify verify = new Verify();
    private Frontend frontend = new Frontend();
    private Admin admin = new Admin();
    @Getter
    @Setter
    public static class Verify {
        private String candidate;
        private String employer;
    }

    @Getter
    @Setter
    public static class Frontend {
        private String verifiedCandidateUrl;
        private String verifiedCompanyUrl;
        private String verifiedUrl;
        private String failedUrl;
        private String feUrl;
        
    }
    @Getter
    @Setter
    public static class Admin {
        private String email;
        private String password;
    }
    

}

