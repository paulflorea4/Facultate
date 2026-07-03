package Rest;

import Repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public JucatorRepository jucatorRepository() {
        return new JucatorHibernateRepository();
    }

    @Bean
    public ConfiguratieRepository configuratieRepository() {
        return new ConfiguratieHibernateRepository();
    }

    @Bean
    public JocRepository jocRepository() {
        return new JocHibernateRepository();
    }

    @Bean
    public CuvantRepository cuvantRepository() { return new CuvantHibernateRepository(); }

    @Bean
    public RundaRepository rundaRepository() { return new RundaHibernateRepository(); }
}
