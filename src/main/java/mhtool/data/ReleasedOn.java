package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReleasedOn {
    public final String game;
    public final String device;
    public ReleasedOn(String game, String device) {
        this.game = game;
        this.device = device;
    }
    public final class DAO {
        public static List<ReleasedOn> listReleased(Connection connection) {
            var releasedList = new ArrayList<ReleasedOn>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_RELEASED);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var device = resultSet.getString("Device_name");
                    var game = resultSet.getString("Game_Name");
                    var released = new ReleasedOn(game, device);
                    releasedList.add(released);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return releasedList;
        }
    }
}
