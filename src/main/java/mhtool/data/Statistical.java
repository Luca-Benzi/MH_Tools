package mhtool.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Statistical {
    public final class DAO {
            public static List<String> getTopHuntedMonsters(Connection connection) {
                // The list that will store the result strings
                List<String> topMonsters = new ArrayList<>();
                
                
                // Execute the query
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(Queries.getHuntedTop)) {
        
                    // Process the result set
                    while (rs.next()) {
                        String monsterName = rs.getString("Monster_Name");
                        int huntCount = rs.getInt("Hunt_Count");
                        
                        // Format the result string and add it to the list
                        topMonsters.add("Monster: " + monsterName + " - Hunt Count: " + huntCount);
                    }
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle the exception
                }
                
                return topMonsters; // Return the list of top hunted monsters
            }
        public static List<String> getBotHuntedMonsters(Connection connection) {
                // The list that will store the result strings
                List<String> topMonsters = new ArrayList<>();
                
                
                // Execute the query
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(Queries.getHuntedBot)) {
        
                    // Process the result set
                    while (rs.next()) {
                        String monsterName = rs.getString("Monster_Name");
                        int huntCount = rs.getInt("Hunt_Count");
                        
                        // Format the result string and add it to the list
                        topMonsters.add("Monster: " + monsterName + " - Hunt Count: " + huntCount);
                    }
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle the exception
                }
                
                return topMonsters; // Return the list of top hunted monsters
        }
        public static List<String> getTopPlayedGames(Connection connection) {
            List<String> topGames = new ArrayList<>();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(Queries.getTopPlayedGames)) {
    
                while (rs.next()) {
                    String gameName = rs.getString("Game_Name");
                    int runCount = rs.getInt("Run_Count");
                    
                    // Format result and add to list
                    topGames.add("Game: " + gameName + " - Run Count: " + runCount);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle database errors
            }
            
            return topGames;
        }
        public static List<String> getBotPlayedGames(Connection connection) {
            List<String> topGames = new ArrayList<>();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(Queries.getBotPlayedGames)) {
    
                while (rs.next()) {
                    String gameName = rs.getString("Game_Name");
                    int runCount = rs.getInt("Run_Count");
                    
                    // Format result and add to list
                    topGames.add("Game: " + gameName + " - Run Count: " + runCount);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle database errors
            }
            
            return topGames;
        }
     
        public static List<String> getTopQuestSuccessRates(Connection connection) {
            List<String> results = new ArrayList<>();
        
        
            try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(Queries.getTopQuestSuccessRates)) {
        
                while (rs.next()) {
                    String questName = rs.getString("Quest_Name");
                    int totalRuns = rs.getInt("Total_Runs");
                    int successfulRuns = rs.getInt("Successful_Runs");
                    double successPercentage = rs.getDouble("Success_Percentage");
        
                    results.add(String.format("Quest: %s | Total Runs: %d | Success Rate: %.2f%%",
                            questName, totalRuns, successPercentage));
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQL errors
            }
        
            return results;
        }
        public static List<String> getBotQuestSuccessRates(Connection connection) {
            List<String> results = new ArrayList<>();
        
        
            try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(Queries.getTopQuestSuccessRates)) {
        
                while (rs.next()) {
                    String questName = rs.getString("Quest_Name");
                    int totalRuns = rs.getInt("Total_Runs");
                    int successfulRuns = rs.getInt("Successful_Runs");
                    double successPercentage = rs.getDouble("Success_Percentage");
        
                    results.add(String.format("Quest: %s | Total Runs: %d | Success Rate: %.2f%%",
                            questName, totalRuns, successPercentage));
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQL errors
            }
        
            return results;
        }
    }
}
