package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Quest {
    public final String name;
    public final String game;
    public final String map;

    public Quest(String name, String game, String map) {
        this.name = name;
        this.game = game;
        this.map = map;
    }
    public final class DAO {
        public static List<Quest> listQuest(Connection connection) {
            var questList = new ArrayList<Quest>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_QUEST);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var name = resultSet.getString("Quest_Name");
                    var game = resultSet.getString("Game_name");
                    var map = resultSet.getString("Map_Name");
                    var quest = new Quest(name, game, map);
                    questList.add(quest);
                } 
            }  catch (Exception e) {
                throw new DAOException(e);
            }
            return questList;
        }
    }
}
