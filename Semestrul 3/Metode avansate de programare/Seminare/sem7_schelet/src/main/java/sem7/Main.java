package sem7;




import sem7.config.Config;
import sem7.domain.Movie;
import sem7.repository.MovieDBRepository;
import sem7.repository.MovieRepository;
import sem7.service.MovieService;
import sem7.util.paging.Page;
import sem7.util.paging.Pageable;
import sem7.validator.MovieValidator;

import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1. Config DB
        Properties props = Config.getProperties();
        String dbUrl = props.getProperty("db.url");
        String dbUsername = props.getProperty("db.username");
        String dbPassword = props.getProperty("db.password");

        // 2. Repository + Service
        MovieRepository movieRepository = new MovieDBRepository(dbUrl, dbUsername, dbPassword);
        MovieService movieService = new MovieService(movieRepository, new MovieValidator());

        // 3. Paginare simplă (2 filme/pagină)
        int pageSize = 2;
        int currentPage = 0;
        
    }
}
