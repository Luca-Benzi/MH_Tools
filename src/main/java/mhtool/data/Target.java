package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Target {
    public final String quest;
    public final String monster;
    public final String game;
    public Target(String quest, String monster, String game) {
        this.quest = quest;
        this.monster = monster;
        this.game = game;
    }
    public final class DAO {
        public static List<Target> listTarget(Connection connection) {
            var targetList = new ArrayList<Target>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_TARGET);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var quest = resultSet.getString("Quest_Name");
                    var monster = resultSet.getString("Monster_Name");
                    var game = resultSet.getString("Game_Name");
                    var target = new Target(quest, monster, game);
                    targetList.add(target);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return targetList;
        }
    }
}
