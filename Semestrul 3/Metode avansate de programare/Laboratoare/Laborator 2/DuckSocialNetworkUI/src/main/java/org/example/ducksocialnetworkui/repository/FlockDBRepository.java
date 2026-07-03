package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.dto.Dto;
import org.example.ducksocialnetworkui.exception.RepositoryException;
import org.example.ducksocialnetworkui.factory.FlockFactory;
import org.example.ducksocialnetworkui.flock.Flock;
import org.example.ducksocialnetworkui.flock.FlyingFlock;
import org.example.ducksocialnetworkui.flock.SwimmingFlock;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.sql.*;
import java.util.*;

public class FlockDBRepository implements FlockRepository{
    private final String url;
    private final String username;
    private final String password;

    public FlockDBRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Optional<Flock<? extends Duck>> findOne(Connection connection,Long id) throws SQLException {
        try(PreparedStatement statement=connection.prepareStatement("SELECT * FROM flocks WHERE id = ?")) {
            statement.setLong(1,id);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                String name=resultSet.getString("name");
                String type=resultSet.getString("type");

                return Optional.of(FlockFactory.getInstance().create(type,id,name));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Flock<? extends Duck>> findOne(Long id) {
        try(Connection connection= DriverManager.getConnection(url,username,password)){
            return findOne(connection,id);
        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Iterable<Flock<? extends Duck>> findAll() {
        List<Flock<? extends Duck>> flocks=new ArrayList<>();
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM flocks");
            ResultSet resultSet=statement.executeQuery()) {

            while(resultSet.next()){
                Long id=resultSet.getLong("id");
                String name=resultSet.getString("name");
                String type=resultSet.getString("type");
                flocks.add(FlockFactory.getInstance().create(type,id,name));
            }

            return flocks;

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Flock<? extends Duck>> save(Flock<? extends Duck> flock) {
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("INSERT INTO flocks (name, type) VALUES (?, ?)")){

            String type;
            if(flock instanceof FlyingFlock) type="FLYING";
            else if(flock instanceof SwimmingFlock) type="SWIMMING";
            else type="FLYING_AND_SWIMMING";

            statement.setString(1,flock.getName());
            statement.setString(2,type);

            int response = statement.executeUpdate();

            return response == 0 ? Optional.empty() : Optional.of(flock) ;

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<Flock<? extends Duck>> delete(Long id) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        PreparedStatement statement=connection.prepareStatement("DELETE FROM flocks WHERE id = ?")){

            var flock=findOne(connection,id);

            if(flock.isEmpty())
                return Optional.empty();

            statement.setLong(1,id);

            int response = statement.executeUpdate();
            return response != 0 ? flock : Optional.empty();

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    private List<Flock<? extends Duck>> getAllPage(Connection connection,Pageable page) throws SQLException{
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM flocks OFFSET ? LIMIT ?");
        statement.setLong(1, (long) page.getPageNumber() *page.getPageSize());
        statement.setLong(2,page.getPageSize());

        ResultSet resultSet=statement.executeQuery();

        Set<Flock<? extends Duck>> flocks=new HashSet<>();

        while(resultSet.next()){
            flocks.add(FlockFactory.getInstance().create(resultSet.getString("type"),resultSet.getLong("id"),resultSet.getString("name")));
        }

        return flocks.stream().toList();
    }

    @Override
    public Page<Flock<? extends Duck>> getAllPage(Pageable page) {
        try(Connection connection=DriverManager.getConnection(url,username,password)){
            int currenCount = count(connection);

            List<Flock<? extends Duck>> flocks=currenCount > 0 ? getAllPage(connection,page) : Collections.emptyList();

            return new Page<>(flocks,currenCount);

        }catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    private int count(Connection connection) throws SQLException{
        PreparedStatement statement=connection.prepareStatement("SELECT COUNT(*) AS count FROM flocks");
        ResultSet resultSet=statement.executeQuery();

        return resultSet.next() ? resultSet.getInt("count") : 0;
    }

    @Override
    public Page<Flock<? extends Duck>> getAllPage(Pageable page, Dto filter) {
        return null;
    }
}
