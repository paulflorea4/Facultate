package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.*;
import org.example.ducksocialnetworkui.dto.Dto;
import org.example.ducksocialnetworkui.dto.UserDto;
import org.example.ducksocialnetworkui.exception.RepositoryException;
import org.example.ducksocialnetworkui.utils.PasswordUtils;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;
import org.example.ducksocialnetworkui.utils.pair.Pair;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class UserDBRepository implements UserRepository {

    private final String url;
    private final String username;
    private final String password;

    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Optional<User> findOne(Connection connection,Long id) throws SQLException{
        try(PreparedStatement statement=connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setLong(1,id);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(createUser(connection,id,resultSet));
            }
        }
        return Optional.empty();
    }

    static User createUser(Connection connection, Long id, ResultSet resultSet) throws SQLException {
        String username=resultSet.getString("username");
        String email=resultSet.getString("email");
        String password=resultSet.getString("password");
        String type=resultSet.getString("type");
        if(type.equals("Persoana"))
            return createPersoana(connection,id,username,email,password);
        return createDuck(connection,id,username,email,password);
    }

    private static Duck createDuck(Connection connection, Long id, String username, String email, String password) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM ducks WHERE user_id = ?");
        statement.setLong(1,id);
        ResultSet resultSet=statement.executeQuery();
        if(resultSet.next()){
            String tipRata = resultSet.getString("tip");
            double viteza = resultSet.getDouble("viteza");
            double rezistenta = resultSet.getDouble("rezistenta");
            long cardId = resultSet.getLong("card_id");

            TipRata tip = TipRata.valueOf(tipRata.toUpperCase());

            switch (tip) {
                case FLYING -> {
                    return new FlyingDuck(id,username,email,password,viteza,rezistenta,cardId);
                }
                case SWIMMING -> {
                    return new SwimmingDuck(id,username,email,password,viteza,rezistenta,cardId);
                }
                case FLYING_AND_SWIMMING -> {
                    return new FlyingAndSwimmingDuck(id,username,email,password,viteza,rezistenta,cardId);
                }
            }
        }
        return null;
    }

    private static Persoana createPersoana(Connection connection,Long id,String username,String email,String password) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM persoane WHERE user_id = ?");
        statement.setLong(1,id);
        ResultSet resultSet=statement.executeQuery();
        if(resultSet.next()){
            String nume=resultSet.getString("nume");
            String prenume=resultSet.getString("prenume");
            String ocupatie=resultSet.getString("ocupatie");
            LocalDate dataNasterii=resultSet.getDate("data_nasterii").toLocalDate();
            double nivelEmpatie=resultSet.getDouble("nivel_empatie");

            return new Persoana(id,username,email,password,nume,prenume,dataNasterii,ocupatie,nivelEmpatie);
        }
        return null;
    }

    @Override
    public Optional<User> findOne(Long id){
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            return findOne(connection,id);
        }
        catch(SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users=new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet=statement.executeQuery()){

            while(resultSet.next()){
                users.add(createUser(connection,resultSet.getLong("id"),resultSet));
            }

            return users;

        } catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<User> save(User user) {
        try {
            if (user instanceof Persoana p) {
                return savePersoana(p) == 0 ? Optional.empty() : Optional.of(user);
            } else if (user instanceof Duck d) {
                return saveDuck(d) == 0 ? Optional.empty() : Optional.of(user);
            }

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
        return Optional.empty();
    }

    private int saveDuck(Duck d) throws SQLException {
        String hashedPassword = PasswordUtils.hash(d.getPassword());

        String userSql =
                "INSERT INTO users (username, email, password, type) " +
                        "VALUES (?, ?, ?, 'Duck') RETURNING id";

        String duckSql =
                "INSERT INTO ducks (user_id, tip, viteza, rezistenta, card_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        String flockSql = "SELECT type FROM flocks WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement userStatement = connection.prepareStatement(userSql);
             PreparedStatement duckStatement = connection.prepareStatement(duckSql);
             PreparedStatement flockStatement = connection.prepareStatement(flockSql)) {

            // Validate flock type
            flockStatement.setLong(1, d.getCardId());
            ResultSet flockResult = flockStatement.executeQuery();

            if (!flockResult.next()) {
                throw new SQLException("Nu exista card cu id " + d.getCardId());
            }

            String flockTypeString = flockResult.getString("type");
            TipRata flockType = TipRata.valueOf(flockTypeString.toUpperCase());

            if (flockType != d.getTip()) {
                throw new SQLException("Tipurile sunt diferite!");
            }

            userStatement.setString(1, d.getUsername());
            userStatement.setString(2, d.getEmail());
            userStatement.setString(3, hashedPassword);

            ResultSet rs = userStatement.executeQuery();
            if (!rs.next()) {
                throw new SQLException("No ID returned from INSERT users");
            }

            long generatedId = rs.getLong(1);
            d.setId(generatedId);

            duckStatement.setLong(1, d.getId());
            duckStatement.setString(2, d.getTip().toString());
            duckStatement.setDouble(3, d.getViteza());
            duckStatement.setDouble(4, d.getRezistenta());
            duckStatement.setLong(5, d.getCardId());

            return duckStatement.executeUpdate();
        }
    }



    private int savePersoana(Persoana p) throws SQLException {
        String hashedPassword = PasswordUtils.hash(p.getPassword());

        String userSql =
                "INSERT INTO users (username, email, password, type) " +
                        "VALUES (?, ?, ?, 'Persoana') RETURNING id";

        String persoanaSQL =
                "INSERT INTO persoane (user_id, nume, prenume, data_nasterii, ocupatie, nivel_empatie) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement userStatement = connection.prepareStatement(userSql);
             PreparedStatement persoanaStatement = connection.prepareStatement(persoanaSQL)) {

            // Save user with hashed password
            userStatement.setString(1, p.getUsername());
            userStatement.setString(2, p.getEmail());
            userStatement.setString(3, hashedPassword);

            ResultSet rs = userStatement.executeQuery();
            if (!rs.next()) {
                throw new SQLException("No ID returned from INSERT users");
            }

            long generatedId = rs.getLong(1);
            p.setId(generatedId);

            // Save Persoana details
            persoanaStatement.setLong(1, generatedId);
            persoanaStatement.setString(2, p.getNume());
            persoanaStatement.setString(3, p.getPrenume());
            persoanaStatement.setDate(4, Date.valueOf(p.getDataNasterii()));
            persoanaStatement.setString(5, p.getOcupatie());
            persoanaStatement.setDouble(6, p.getNivelEmpatie());

            return persoanaStatement.executeUpdate();
        }
    }



    @Override
    public Optional<User> delete(Long id) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement("DELETE FROM users WHERE id = ?");) {
            var user=findOne(id);
            if(user.isEmpty())
                return Optional.empty();

            statement.setLong(1, id);

            int response = statement.executeUpdate();
            return response!=0 ? user : Optional.empty();
        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    private List<User> getAllPage(Connection connection,Pageable page,UserDto filter) throws SQLException{
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Pair<String, List<Object>> sqlFilter = toSql(filter);

        if (!sqlFilter.getFirst().isEmpty())
            sql += " WHERE " + sqlFilter.getFirst();

        sql += " LIMIT ? OFFSET ?";
        try (PreparedStatement statement=connection.prepareStatement(sql);) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getSecond()) {
                statement.setObject(++paramIndex, param);
            }
            statement.setInt(++paramIndex, page.getPageSize());
            statement.setInt(++paramIndex, page.getPageSize() * page.getPageNumber());

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(createUser(connection, resultSet.getLong("id"), resultSet));
                }
            }

        }
        return users;
    }

    @Override
    public Page<User> getAllPage(Pageable page, Dto filter) {
        try(Connection connection=DriverManager.getConnection(url,username,password)){
            int currenCount = count(connection,(UserDto) filter);

            List<User> users=currenCount > 0 ? getAllPage(connection,page,(UserDto) filter) : Collections.emptyList();

            return new Page<>(users,currenCount);

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Page<User> getAllPage(Pageable page) {
        return getAllPage(page,null);
    }


    private int count(Connection connection,UserDto filter) throws SQLException {
        String sql="SELECT COUNT(*) AS count FROM users";
        Pair<String,List<Object>> sqlFilter= toSql(filter);
        if (!sqlFilter.getFirst().isEmpty()) {
            sql += " WHERE " + sqlFilter.getFirst();
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getSecond()) {
                statement.setObject(++paramIndex, param);
            }
            try (ResultSet result = statement.executeQuery()) {
                int totalNumberOfUsers = 0;
                if (result.next()) {
                    totalNumberOfUsers = result.getInt("count");
                }
                return totalNumberOfUsers;
            }
        }
    }

    private Pair<String,List<Object>> toSql(UserDto filter) {
        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if(filter.getType()!=null){
            conditions.add("type = ?");
            params.add(filter.getType());
        }

        if(filter.getDuckType()!=null){
            conditions.add("id IN (SELECT user_id FROM ducks WHERE tip = ?)");
            params.add(filter.getDuckType());
        }
        String sql = String.join(" AND ", conditions);
        return new Pair<>(sql, params);
    }

    public Optional<User> findByUsername(String usernameUser) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {

            statement.setString(1,usernameUser);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                long id=resultSet.getLong("id");
                return Optional.of(createUser(connection,id,resultSet));
            }
        }catch (SQLException e ){
            throw new RepositoryException(e.getMessage());
        }
        return Optional.empty();
    }

    private List<User> findFriends(Connection connection,Pageable page,Long id) {
        String sql = "SELECT * FROM users u JOIN friendships f ON (u.id=f.user_id1 OR u.id=f.user_id2) WHERE (f.user_id1=? OR f.user_id2=?) AND u.id!=? OFFSET ? LIMIT ?";

        List<User> friends=new ArrayList<>();

        try(PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1,id);
            statement.setLong(2,id);
            statement.setLong(3,id);
            statement.setLong(4, (long) page.getPageNumber() *page.getPageSize());
            statement.setLong(5,page.getPageSize());
            ResultSet resultSet=statement.executeQuery();

            while (resultSet.next()){
                friends.add(createUser(connection,resultSet.getLong("id"),resultSet));
            }

            return friends;

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    public Page<User> findFriends(Pageable page,Long id) {
        try(Connection connection=DriverManager.getConnection(url,username,password)){

            int currentCount=countFriends(connection,id);

            List<User> users=findFriends(connection,page,id);

            return new Page<>(users, currentCount);

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    private int countFriends(Connection connection,Long id) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("SELECT COUNT(*) AS count FROM users u JOIN friendships f ON (u.id=f.user_id1 OR u.id=f.user_id2) WHERE (f.user_id1=? OR f.user_id2=?) AND u.id!=?");
        statement.setLong(1,id);
        statement.setLong(2,id);
        statement.setLong(3,id);
        ResultSet resultSet=statement.executeQuery();

        return resultSet.next() ? resultSet.getInt("count") : 0;
    }
}
