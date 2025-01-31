package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class Character {
    public final String ingameName;
    public final String username;
    public final String gameName;
    public Character(String ingameName, String username, String gameName) {
        this.ingameName = ingameName;
        this.username = username;
        this.gameName = gameName;
    }
    public String toString(){
        return "IGN: " + ingameName + ", Game: " + gameName;
    }
    public final class DAO {
        public static List<Character> listCharacter(Connection connection) {
            var characterList = new ArrayList<Character>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_CHARACTER);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var ingameName = resultSet.getString("IGN");
                    var username = resultSet.getString("Username");
                    var gameName = resultSet.getString("Game_Name");
                    var character = new Character(ingameName, username, gameName);
                    characterList.add(character);
                } 
            }  catch (Exception e) {
                throw new DAOException(e);
            }
            return characterList;
        }
        public static boolean addCharacter(Connection connection, String username,String game,String ingameName) {
            boolean result;
            try (
                var statement = DAOUtils.prepare(connection, Queries.INSERT_CHARACTER, username, game, ingameName);
                
            ) { result = statement.execute();
                JOptionPane.showMessageDialog(null,  "Successfully created new Character");
            }
                catch (Exception e) {
                JOptionPane.showMessageDialog(null,  "Failed to add character, Ingame name already in use");
                throw new DAOException(e);
            }
            return result;
        }
    }
}
