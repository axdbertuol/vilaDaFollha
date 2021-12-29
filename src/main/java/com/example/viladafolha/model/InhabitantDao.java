package com.example.viladafolha.model;

import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InhabitantDao {

    Connection connection;

    @Autowired
    public InhabitantDao(Connection connection) {
        this.connection = connection;
    }

    public Optional<InhabitantDTO> getById(Long id) {
        InhabitantDTO inhabitant = null;
        try (PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM inhabitants WHERE id=?")) {
            pStmt.setLong(1, id);
            pStmt.execute();
            ResultSet resultSet = pStmt.getResultSet();
            while (resultSet.next()) {
                inhabitant = extractInhabitant(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(inhabitant);
    }


    public Optional<InhabitantDTO> getByEmail(String email) {
        InhabitantDTO inhabitant = null;
        try (PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM inhabitants WHERE email=?")) {
            pStmt.setString(1, email);
            pStmt.execute();
            ResultSet resultSet = pStmt.getResultSet();
            while (resultSet.next()) {
                inhabitant = extractInhabitant(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(inhabitant);
    }

    public List<InhabitantDTO> getAll() {
        List<InhabitantDTO> inhabitants = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SELECT id, name FROM inhabitants");
            ResultSet resultSet = stmt.getResultSet();
            while (resultSet.next()) {
                InhabitantDTO inhabitant = extractInhabitant(resultSet);
                inhabitants.add(inhabitant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inhabitants;
    }

    public List<InhabitantDTO> getAllByFilter(String name) {
        List<InhabitantDTO> inhabitants = new ArrayList<>();
        try (PreparedStatement pStmt =
                     connection.prepareStatement("SELECT id, name FROM inhabitants WHERE name LIKE ?")) {
            pStmt.setString(1, name + "%");
            pStmt.execute();
            ResultSet resultSet = pStmt.getResultSet();
            while (resultSet.next()) {
                inhabitants.add(extractInhabitant(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inhabitants;
    }
    public List<InhabitantDTO> getAllByFilter(Integer month) {
        List<InhabitantDTO> inhabitants = new ArrayList<>();
        try (PreparedStatement pStmt =
                     connection.prepareStatement("SELECT id, name, date_part('month', birthday) m FROM inhabitants WHERE m=?")) {
            pStmt.setInt(1, month);
            pStmt.execute();
            ResultSet resultSet = pStmt.getResultSet();
            while (resultSet.next()) {
                inhabitants.add(extractInhabitant(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inhabitants;
    }


    public void save(Inhabitant inhabitant) {
//        inhabitants.put(inhabitant.getId(), inhabitant);
    }


    public void update(Inhabitant inhabitant, String[] params) {
//        inhabitant.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
//        inhabitant.setSurname(Objects.requireNonNull(params[1], "Surname cannot be null"));
//        inhabitant.setAge(Integer.parseInt(params[2]));
//        inhabitant.setCost(Double.parseDouble(params[3]));
//        inhabitants.put(inhabitant.getId(), inhabitant);
    }

    public void delete(Inhabitant inhabitant) {
//        inhabitants.remove(inhabitant.getId());
    }


    private InhabitantDTO extractInhabitant(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String lastName = resultSet.getString("lastName");
        String cpf = resultSet.getString("cpf");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        Date birthday = resultSet.getDate("birthday");
        Double balance = resultSet.getDouble("balance");

        String[] roles_arr = (String[]) resultSet.getArray("roles").getArray();
        Set<String> roles = Arrays.stream(roles_arr).filter(Objects::nonNull).map(String.class::cast).collect(Collectors.toSet());


        InhabitantDTO inhabitant = new InhabitantDTO(
                name, lastName, cpf, email, password, birthday, balance, roles
        );
        inhabitant.setId(id);
        return inhabitant;
    }
}
