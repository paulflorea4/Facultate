package scs.map.sem9_paging.repository;


import scs.map.sem9_paging.domain.Movie;
import scs.map.sem9_paging.dto.MovieFilterDTO;
import scs.map.sem9_paging.util.Pair;
import scs.map.sem9_paging.util.paging.Page;
import scs.map.sem9_paging.util.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import scs.map.sem9_paging.domain.Movie;
import scs.map.sem9_paging.dto.MovieFilterDTO;
import scs.map.sem9_paging.util.Pair;
import scs.map.sem9_paging.util.paging.Page;
import scs.map.sem9_paging.util.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
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

    private Pair<String, List<Object>> toSql(MovieFilterDTO filter) {
        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        filter.getYear().ifPresent(yearFilter -> {
            conditions.add("year = ?");
            params.add(yearFilter);
        });
        filter.getYearAfter().ifPresent(yearAfterFilter -> {
            conditions.add("year > ?");
            params.add(yearAfterFilter);
        });
        filter.getDirector().ifPresent(directorFilter -> {
            // the parameter placeholder (?) should be outside the quotes
            conditions.add("director like ?");
            params.add("%" + directorFilter  + "%");
        });
        filter.getTitle().ifPresent(titleFilter -> {
            conditions.add("title like ?");
            params.add("%" + titleFilter + "%");
        });
        String sql = String.join(" and ", conditions);
        return new Pair<>(sql, params);
    }

    private int count(Connection connection, MovieFilterDTO filter) throws SQLException {
        String sql = "select count(*) as count from movies";
        Pair<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.getFirst().isEmpty()) {
            sql += " where " + sqlFilter.getFirst();
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getSecond()) {
                statement.setObject(++paramIndex, param);
            }
            try (ResultSet result = statement.executeQuery()) {
                int totalNumberOfMovies = 0;
                if (result.next()) {
                    totalNumberOfMovies = result.getInt("count");
                }
                return totalNumberOfMovies;
            }
        }
    }

    private List<Movie> findAllOnPage(Connection connection, Pageable pageable, MovieFilterDTO filter) throws SQLException {
        List<Movie> moviesOnPage = new ArrayList<>();
        // Using StringBuilder rather than "+" operator for concatenating Strings is more performant
        // since Strings are immutable, so every operation applied on a String will create a new String
        String sql = "select * from movies";
        Pair<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.getFirst().isEmpty()) {
            sql += " where " + sqlFilter.getFirst();
        }
        sql += " limit ? offset ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getSecond()) {
                statement.setObject(++paramIndex, param);
            }
            statement.setInt(++paramIndex, pageable.getPageSize());
            statement.setInt(++paramIndex, pageable.getPageSize() * pageable.getPageNumber());
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


    public Page<Movie> findAllOnPage(Pageable pageable, MovieFilterDTO filter) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            int totalNumberOfMovies = count(connection, filter);
            List<Movie> moviesOnPage;
            if (totalNumberOfMovies > 0) {
                moviesOnPage = findAllOnPage(connection, pageable, filter);
            } else {
                moviesOnPage = new ArrayList<>();
            }
            return new Page<>(moviesOnPage, totalNumberOfMovies);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Movie> findAllOnPage(Pageable pageable) {
        return findAllOnPage(pageable, null);
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
