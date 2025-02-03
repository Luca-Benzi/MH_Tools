package mhtool.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Request {
    public final String username;
    public final String game;
    public final List<String> objective;
    public Request(String username, String game, List<String> objective) {
        this.username = username;
        this.game = game;
        this.objective = objective;
    }

    public final class DAO {
        public static void insertHuntRequest(Connection connection, String username, String gameName, List<String> monsterNames, String ign) {
            try {
        // Start transaction
        connection.setAutoCommit(false);

        int lastRequestId = -1;
        int lastGroupId = -1;

        // Step 1: Insert into HUNT_REQUEST and get generated Request ID
        try (PreparedStatement huntStmt = connection.prepareStatement(Queries.insertHuntSQL, Statement.RETURN_GENERATED_KEYS)) {
            huntStmt.setString(1, username);
            huntStmt.setString(2, gameName);
            huntStmt.setBoolean(3, false);
            huntStmt.executeUpdate();

            try (ResultSet rs = huntStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lastRequestId = rs.getInt(1); // Get generated ID
                }
            }
        }

        // Step 2: Insert into REQUEST_OBJECTIVE for each provided monster (up to 3)
        try (PreparedStatement objStmt = connection.prepareStatement(Queries.insertObjectiveSQL)) {
            for (int i = 0; i < Math.min(monsterNames.size(), 3); i++) {
                objStmt.setInt(1, lastRequestId);
                objStmt.setString(2, monsterNames.get(i));
                objStmt.executeUpdate();
            }
        }

        // Step 3: Insert into GROUP and get generated Group ID
        try (PreparedStatement groupStmt = connection.prepareStatement(Queries.insertGroupSQL, Statement.RETURN_GENERATED_KEYS)) {
            groupStmt.setInt(1, lastRequestId);
            groupStmt.executeUpdate();

            try (ResultSet rs = groupStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lastGroupId = rs.getInt(1); // Get generated Group ID
                }
            }
        }

        // Step 4: Insert into PART_OF using lastGroupId
        try (PreparedStatement partStmt = connection.prepareStatement(Queries.insertPartOfSQL)) {
            partStmt.setString(1, username);
            partStmt.setString(2, gameName);
            partStmt.setString(3, ign);
            partStmt.setInt(4, lastGroupId);
            partStmt.executeUpdate();
        }

        // Commit transaction
        connection.commit();
        JOptionPane.showMessageDialog(null,  "Successfully created new Hunt Request");

        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback in case of failure
                System.err.println("Transaction failed! Rolling back...");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Restore default behavior
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            }
        }

        public final static List<String> getHuntRequestsByGame(Connection connection, String gameName) {
            List<String> huntRequests = new ArrayList<>();
            try (PreparedStatement huntStmt = connection.prepareStatement(Queries.huntSQL)) {
                huntStmt.setString(1, gameName);
        
                try (ResultSet huntRs = huntStmt.executeQuery()) {
                    while (huntRs.next()) {
                        int requestId = huntRs.getInt("Request_Id");
                        String creator = huntRs.getString("Username");
        
                        // Fetch target monsters
                        List<String> monsters = new ArrayList<>();
                        try (PreparedStatement monsterStmt = connection.prepareStatement(Queries.monstersSQL)) {
                            monsterStmt.setInt(1, requestId);
                            try (ResultSet monsterRs = monsterStmt.executeQuery()) {
                                while (monsterRs.next()) {
                                    monsters.add(monsterRs.getString("Monster_Name"));
                                }
                            }
                        }
        
                        // Fetch group members (excluding creator)
                        List<String> members = new ArrayList<>();
                        try (PreparedStatement memberStmt = connection.prepareStatement(Queries.membersSQL)) {
                            memberStmt.setInt(1, requestId);
                            try (ResultSet memberRs = memberStmt.executeQuery()) {
                                while (memberRs.next()) {
                                    String member = memberRs.getString("Username");
                                    if (!member.equals(creator)) {
                                        members.add(member);
                                    }
                                }
                            }
                        }
        
                        // Format the Hunt Request
                        String huntDetails = String.format(requestId + ":    Hunt Request by: %s\n   Target Monsters: %s\n  Group Members: %s",
                                creator,
                                monsters.isEmpty() ? "None" : String.join(", ", monsters),
                                members.isEmpty() ? "No other members" : String.join(", ", members)
                        );
        
                        huntRequests.add(huntDetails);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
            return huntRequests;

        }
        public static void addCharacterOwnerToGroup(Connection connection, int requestId, String ign) {
            try (PreparedStatement findOwnerStmt = connection.prepareStatement(Queries.findOwnerQuery);
                 PreparedStatement findGroupStmt = connection.prepareStatement(Queries.findGroupQuery);
                 PreparedStatement insertStmt = connection.prepareStatement(Queries.insertIntoPartOfQuery)) {
        
                //  Find the owner of the character
                findOwnerStmt.setString(1, ign);
                try (ResultSet ownerResult = findOwnerStmt.executeQuery()) {
                    if (!ownerResult.next()) {  
                        JOptionPane.showMessageDialog(null, "You dont have characters in this game");
                        return;
                    }
                    String username = ownerResult.getString("Username");
                    String gameName = ownerResult.getString("Game_Name");
        
        
                    //  Find the Group ID linked to the Request ID
                    findGroupStmt.setInt(1, requestId);
                    try (ResultSet groupResult = findGroupStmt.executeQuery()) {
                        if (!groupResult.next()) {  
                            System.err.println(" Error: No group found for Request ID: " + requestId);
                            return;
                        }
                        int groupId = groupResult.getInt("Group_Id");
        
                        //  Insert the owner into the "PART_OF" table
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, ign);
                        insertStmt.setString(3, gameName);
                        insertStmt.setInt(4, groupId);
                        int rowsInserted = insertStmt.executeUpdate();
        
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(null,  "Successfully Joined group " + groupId);
                        } else {
                            System.err.println(" Error: Insert operation failed.");
                        }
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,  "Failed to Join, already part of it");
                
            }
        }
    }
}
