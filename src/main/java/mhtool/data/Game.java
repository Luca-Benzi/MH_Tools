package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public final class Game {

    public final String name;
    public final String releaseDate;
    public Game(String name, String releaseDate){
        this.name = name;
        this.releaseDate = releaseDate;
    }
    public final class DAO {
        public static List<Game> listGame(Connection connection) {
            var gameList = new ArrayList<Game>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_GAMES);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var name = resultSet.getString("Game_Name");
                    var releaseDate = resultSet.getString("Release_Date");
                    var game = new Game(name, releaseDate);
                    gameList.add(game);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return gameList;
        }   
    }
}
