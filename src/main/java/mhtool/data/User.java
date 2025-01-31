package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class User {
    public final String username;
    public final String name;
    public final String surname;
    public final String email;
    public final String password;
    public User(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
    public final class DAO {
        public static List<User> listUser(Connection connection) {
            var userList = new ArrayList<User>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_USER);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var username = resultSet.getString("Username");
                    var name = resultSet.getString("name");
                    var surname = resultSet.getString("Surname");
                    var email = resultSet.getString("EMail");
                    var password = resultSet.getString("Password");
                    var user = new User(username, name, surname, email, password);
                    userList.add(user);
                } 
            }  catch (Exception e) {
                throw new DAOException(e);
            }
            return userList;
        }
        public static boolean addUser(Connection connection, String username,String name,String surname, String email, String password) {
            boolean result;
            try (
                var statement = DAOUtils.prepare(connection, Queries.INSERT_USER, username, name, surname, email, password);
                
            ) { result = statement.execute();
                JOptionPane.showMessageDialog(null,  "New Account successfully created");
            }
                catch (Exception e) {
                JOptionPane.showMessageDialog(null,  "Failed to add user, Username already in use");
                throw new DAOException(e);
            }
            return result;
        }
    }
}
