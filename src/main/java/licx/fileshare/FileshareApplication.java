package licx.fileshare;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class FileshareApplication {

    public static void main(String[] args) throws IOException {
        String userHomePath = System.getProperty("user.home");
        String fileShareConfigPath = userHomePath + "/fileshare/";
        File fileShareConfigDir = new File(fileShareConfigPath);
        boolean flag = false;
        if (!fileShareConfigDir.exists()){
            flag = fileShareConfigDir.mkdir();
        }

        String fileShareListPath = fileShareConfigPath + "filesharelist.txt";
        File fileShareList = new File(fileShareListPath);
        if (!fileShareList.exists()){
            flag = fileShareList.createNewFile();
        }

        SpringApplication.run(FileshareApplication.class, args);

    }


    @Bean
    public TomcatServletWebServerFactory tomcatEmbedded() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return tomcat;
    }
}
