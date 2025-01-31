package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public final class Monster {
    public final String name;
    public final String category;
    public final Boolean variant;

    public Monster(String name, String category, Boolean variant) {
        this.name = name;
        this.category = category;
        this.variant = variant;
    }

    public final class DAO {
        public static List<Monster> listMonster(Connection connection) {
            var monsterList = new ArrayList<Monster>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_MONSTER);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var name = resultSet.getString("Monster_Name");
                    var variant = resultSet.getBoolean("variant");
                    var category = resultSet.getString("category");
                    var monster = new Monster(name, category, variant);
                    monsterList.add(monster);
                } 
            }  catch (Exception e) {
                throw new DAOException(e);
            }
            return monsterList;
        }
    }
}