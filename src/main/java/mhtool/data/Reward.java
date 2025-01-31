package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Reward {
    public final String material;
    public final String quest;
    public Reward(String material, String quest) {
        this.material = material;
        this.quest = quest;
    }
    public final class DAO {
        public static List<Reward> listReward(Connection connection) {
            var rewardList = new ArrayList<Reward>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_REWARD);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var quest = resultSet.getString("Quest_Name");
                    var material = resultSet.getString("Material_Name");
                    var reward = new Reward(material, quest);
                    rewardList.add(reward);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return rewardList;
        }
    }
}
