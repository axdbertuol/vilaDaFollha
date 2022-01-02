package com.example.viladafolha.model;

import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InhabitantDao {

    Connection connection;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public InhabitantDao(Connection connection, ApplicationContext context) {
        this.connection = connection;
    }


    public Optional<Inhabitant> create(InhabitantDTO inhabDto) {
        inhabDto.setPassword(encoder.encode(inhabDto.getPassword()));
        Long id = null;

        try (PreparedStatement pStmt = connection.prepareStatement(
                "insert into inhabitants(name, last_name, cpf, email, password, birthday, balance, roles) values (?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)
        ) {
            Array roles = connection.createArrayOf("VARCHAR", new Object[]{"ROLE_USER"});
            pStmt.setString(1, inhabDto.getName());
            pStmt.setString(2, inhabDto.getLastName());
            pStmt.setString(3, inhabDto.getCpf());
            pStmt.setString(4, inhabDto.getEmail());
            pStmt.setString(5, inhabDto.getPassword());
            pStmt.setDate(6, new java.sql.Date(inhabDto.getBirthday().getTime()));
            pStmt.setDouble(7, inhabDto.getBalance());
            pStmt.setArray(8, roles);
            pStmt.execute();
            ResultSet resultSet = pStmt.getGeneratedKeys();
            while (resultSet.next()) {
                id = resultSet.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        inhabDto.setId(id);
        return Optional.of(new Inhabitant(inhabDto));
    }

    public Optional<InhabitantDTO> getById(Long id) {
        InhabitantDTO inhabitant = null;
        try (PreparedStatement pStmt = connection.prepareStatement(
                "SELECT name, last_name, cpf, email, password, birthday, balance, roles FROM inhabitants WHERE id=?")
        ) {
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
        try (PreparedStatement pStmt = connection.prepareStatement(
                "SELECT id, name, birthday FROM inhabitants WHERE date_part('month', birthday)=?"
        )) {
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


    public List<InhabitantDTO> getAllByThatAgeOrOlder(int age) {
        List<InhabitantDTO> inhabitants = new ArrayList<>();
        try (PreparedStatement pStmt = connection.prepareStatement(
                "SELECT id, name, birthday FROM inhabitants WHERE extract(year from AGE(birthday)) >= ?"
        )) {
            pStmt.setInt(1, age);
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

    // TODO
    public InhabitantDTO remove(InhabitantDTO inhabitantDto) {
        try (PreparedStatement pStmt = connection.prepareStatement(
                "delete from inhabitants where email=?")
        ) {
            pStmt.setString(1, inhabitantDto.getEmail());
//            pStmt.setLong(2, inhabitantDto.getId());
            pStmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inhabitantDto;
    }


    private InhabitantDTO extractInhabitant(ResultSet resultSet) throws SQLException {
        Long id = null;
        String name = "";
        String lastName = "";
        String cpf = "";
        String email = "";
        String password = "";
        Date birthday = null;
        Double balance = null;
        Set<String> roles = new HashSet<>();

        try {
            id = resultSet.getLong("id");
        } catch (SQLException ignored) {
        }

        try {
            name = resultSet.getString("name");
        } catch (SQLException ignored) {

        }
        try {
            lastName = resultSet.getString("last_name");
        } catch (SQLException ignored) {

        }
        try {
            cpf = resultSet.getString("cpf");
        } catch (SQLException ignored) {

        }
        try {
            email = resultSet.getString("email");
        } catch (SQLException ignored) {
        }
        try {
            password = resultSet.getString("password");
        } catch (SQLException ignored) {
        }
        try {
            birthday = new Date(resultSet.getDate("birthday").getTime());
        } catch (SQLException ignored) {
        }
        try {
            balance = resultSet.getDouble("balance");
        } catch (SQLException ignored) {
        }
        try {
            String[] roles_arr = (String[]) resultSet.getArray("roles").getArray();
            roles = Arrays.stream(roles_arr).filter(Objects::nonNull).map(String.class::cast).collect(Collectors.toSet());
        } catch (SQLException ignored) {
        }

        InhabitantDTO inhabitant = new InhabitantDTO(
                name, lastName, cpf, email, password, birthday, balance, roles
        );
        inhabitant.setId(id);

        return inhabitant;
    }

}
