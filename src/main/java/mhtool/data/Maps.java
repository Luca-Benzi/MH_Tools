package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Maps {
    public final String name;
    public final String game;
    public Maps(String name, String game) {
        this.name = name;
        this.game = game;
    }

    public final class DAO {
        public static List<Maps> listMap(Connection connection) {
            var mapList = new ArrayList<Maps>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_MAP);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var name = resultSet.getString("Map_Name");
                    var game = resultSet.getString("Game_Name");
                    var map = new Maps(name, game);
                    mapList.add(map); 
                }
            } catch (Exception e) {
                throw new DAOException(e); 
            }
            return mapList;
        }
    }
}
