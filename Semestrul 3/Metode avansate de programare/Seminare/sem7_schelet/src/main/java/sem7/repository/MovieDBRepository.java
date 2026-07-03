package sem7.repository;




import sem7.domain.Movie;
import sem7.util.paging.Page;
import sem7.util.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDBRepository implements MovieRepository {

    private String url;
    private String username;
    private String password;

    public MovieDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Optional<Movie> findOne(Connection connection, Long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from movies where id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String director = resultSet.getString("director");
                int year = resultSet.getInt("year");
                Movie movie = new Movie(title, director, year);
                movie.setId(id);
                return Optional.of(movie);
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Movie> findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
             return findOne(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from movies");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String director = resultSet.getString("director");
                int year = resultSet.getInt("year");
                Movie movie = new Movie(title, director, year);
                movie.setId(id);
                movies.add(movie);
            }
            return movies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> save(Movie entity) {
        String insertSQL = "insert into movies (title, director, year) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDirector());
            statement.setInt(3, entity.getYear());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        String deleteSQL = "delete from movies where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setLong(1, id);
            Optional<Movie> foundUser = findOne(connection, id);
            int response = 0;
            if (foundUser.isPresent()) {
                response = statement.executeUpdate();
            }
            return response == 0 ? Optional.empty() : foundUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> update(Movie entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String updateSQL = "update movies set title = ?, director = ?, year = ? where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateSQL);) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDirector());
            statement.setInt(3, entity.getYear());
            statement.setLong(4, entity.getId());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
    private int count(Connection connection) throws SQLException {
        String sql = "select count(*) as count from movies";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            int totalNumberOfMovies = 0;
            if (result.next()) {
                totalNumberOfMovies = result.getInt("count");
            }
            return totalNumberOfMovies;
        }
    }

    private List<Movie> findAllOnPage(Connection connection, Pageable pageable) throws SQLException {
        List<Movie> moviesOnPage = new ArrayList<>();
        String sql = "select * from movies limit ? offset ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String title = resultSet.getString("title");
                    String director = resultSet.getString("director");
                    int year = resultSet.getInt("year");
                    Movie movie = new Movie(title, director, year);
                    movie.setId(id);
                    moviesOnPage.add(movie);
                }
            }
        }
        return moviesOnPage;
    }

    @Override
    public Page<Movie> findAllOnPage(Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            int totalNumberOfMovies = count(connection);
            List<Movie> moviesOnPage;
            if (totalNumberOfMovies > 0) {
                moviesOnPage = findAllOnPage(connection, pageable);
            } else {
                moviesOnPage = new ArrayList<>();
            }
            return new Page<>(moviesOnPage, totalNumberOfMovies);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

@Override
    public List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select distinct year from movies order by year");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int year = resultSet.getInt("year");
                years.add(year);
            }
            return years;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
