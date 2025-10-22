package pe.edu.upeu.sysasistencia.configuracion;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name:ddzr7juty}")
    private String cloudName;

    @Value("${cloudinary.api-key:387965542137594}")
    private String apiKey;

    @Value("${cloudinary.api-secret:a4jyAIoTix4SjqlAeVqrHhSWw9w}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }
}