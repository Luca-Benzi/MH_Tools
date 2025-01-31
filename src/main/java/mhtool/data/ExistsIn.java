package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ExistsIn {

    public final String gameName;
    public final String monsterName;
    public final boolean firstAppearance;
    public ExistsIn(String gameName, String monsterName, boolean firstAppearance) {
        this.gameName = gameName;
        this.monsterName = monsterName;
        this.firstAppearance = firstAppearance;
    }
    public final class DAO {
        public static List<ExistsIn> listExists(Connection connection) {
            var existsList = new ArrayList<ExistsIn>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_EXISTS);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var game = resultSet.getString("Game_Name");
                    var monster = resultSet.getString("Monster_Name");
                    var first = resultSet.getBoolean("first_appearance");
                    var exists = new ExistsIn(game, monster, first);
                    existsList.add(exists);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return existsList;
        }
    }
}
