package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LivesIn {
    public final String map;
    public final String monster;
    public final String game;
    public LivesIn(String map, String monster, String game) {
        this.map = map;
        this.monster = monster;
        this.game = game;
    }
    public final class DAO {
        public static List<LivesIn> listLives(Connection connection) {
            var livesList = new ArrayList<LivesIn>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_LIVES);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var map = resultSet.getString("Map_Name");
                    var monster = resultSet.getString("Monster_Name");
                    var game = resultSet.getString("Game_Name");
                    var lives = new LivesIn(map, monster, game);
                    livesList.add(lives);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return livesList;
        }
    }
}
