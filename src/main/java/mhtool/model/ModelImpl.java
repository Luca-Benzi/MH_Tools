package mhtool.model;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import mhtool.data.*;
import mhtool.data.Character;


public class ModelImpl implements Model{

    private final Connection connection;

    public ModelImpl(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
        
    }
//All Interactions to the the Entity Monster
    public List<Monster> getMonsters() {
        return Monster.DAO.listMonster(connection);
    }
    public List<String> getMonstersList() {
        return this.getMonsters()
                        .stream()
                        .map(e -> e.name)
                        .collect(Collectors.toList());
    }
    public String getCategoryFromMonster(String monster) {
        return this.getMonsters()
                    .stream()
                    .filter(m -> m.name.equals(monster))
                    .map(e -> e.category)
                    .findFirst()
                    .orElse("category not found");
    }
    public List<String> getMonstersFromcategory(String category) {
        return this.getMonsters()
                    .stream()
                    .filter(m -> m.category.equals(category))
                    .map(e -> e.name)
                    .collect(Collectors.toList());
                    
    }
    public Boolean getVariant(String monster) {
        return this.getMonsters()
                            .stream()
                            .filter(m -> m.name.equals(monster))
                            .map(e -> e.variant)
                            .findFirst()
                            .orElse(false);
    }
    public List<String> getCategoryList() {
        return this.getMonsters()
                        .stream()
                        .map(e -> e.category)
                        .distinct()
                        .collect(Collectors.toList());
    }

//All interactions to the entity Game
    public List<Game> getGames() {
        return Game.DAO.listGame(connection);
    }
    public List<String> getGamesList() {
        return this.getGames()
                        .stream()
                        .map(e -> e.name)
                        .collect(Collectors.toList());
    }
    public String getReleaseDate(String game) {
        return this.getGames()
                    .stream()
                    .filter(g -> g.name.equals(game))
                    .map(e -> e.releaseDate)
                    .findFirst()
                    .orElse("error: missing data");
    }
//Device
public List<String> getDeviceList() {
    return Device.DAO.listDevice(connection)
                    .stream()
                    .map(e -> e.name)
                    .collect(Collectors.toList());
}

//Released on
    public List<ReleasedOn> getReleasedOn() {
        return ReleasedOn.DAO.listReleased(connection);
    }
    public List<String> getReleasedOnList(String game) {
        return this.getReleasedOn()
                    .stream()
                    .filter(g -> g.game.equals(game))
                    .map(e -> e.device)
                    .collect(Collectors.toList());
    }
    public List<String> getGamesFromDevice(String device) {
        return this.getReleasedOn()
                    .stream()
                    .filter(g -> g.device.equals(device))
                    .map(e -> e.game)
                    .collect(Collectors.toList());
    }

//Maps
    public List<Maps> getMaps() {
        return Maps.DAO.listMap(connection);
    }
    public List<String> getMapsList() {
        return this.getMaps()
                        .stream()
                        .map(e -> e.name)
                        .collect(Collectors.toList());
    }
    public List<String> getMapsfromGame(String game) {
        return this.getMaps()
                    .stream()
                    .filter(e -> e.game.equals(game))
                    .map(e -> e.name)
                    .collect(Collectors.toList());
    }
    public String getGamefromMap(String map) {
        return this.getMaps()
                    .stream()
                    .filter(e -> e.name.equals(map))
                    .map(e -> e.game)
                    .findFirst()
                    .orElse("Data not found");
    }

//Materials
    public List<Material> getMaterials() {
        return Material.DAO.listMaterial(connection);
    }
    public List<String> getMaterialsList() {
        return this.getMaterials()
                        .stream()
                        .map(e -> e.name)
                        .collect(Collectors.toList());
    }
    public List<String> getMaterialsFromMonster(String monster) {
        return this.getMaterials()
                        .stream()
                        .filter(e -> e.monster.equals(monster))
                        .map(e -> e.name)
                        .collect(Collectors.toList());
    }
    public String getMonsterFromMaterial(String material) {
        return this.getMaterials()
                        .stream()
                        .filter(e -> e.name.equals(material))
                        .map(e -> e.monster)
                        .findFirst()
                        .orElse("Data not found");
    }
    public Integer getRarityfromMaterial(String material) {
        return this.getMaterials()
                    .stream()
                    .filter(e -> e.name.equals(material))
                    .map(e -> e.rarity)
                    .findFirst()
                    .orElse(0);
    }

//Quests
    public List<Quest> getQuests() {
        return Quest.DAO.listQuest(connection);
    }
    public List<String> getQuestsList() {
        return this.getQuests()
                        .stream()
                        .map(e -> e.name)
                        .collect(Collectors.toList());
    }
    public List<String> getQuestsfromGame(String game) {
        return this.getQuests()
                    .stream()
                    .filter(e -> e.game.equals(game))
                    .map( e -> e.name )
                    .collect(Collectors.toList());
    }
    public String getGameFromQuest(String quest) {
        return this.getQuests()
                    .stream()
                    .filter(e -> e.name.equals(quest))
                    .map( e -> e.game )
                    .findFirst()
                    .orElse("data not found");
    }
    public List<String> getQuestsFromMap(String map) {
        return this.getQuests()
                    .stream()
                    .filter(e -> e.map.equals(map))
                    .map( e -> e.name )
                    .collect(Collectors.toList());
    }
    public String getMapFromQuest(String quest) {
        return this.getQuests()
                    .stream()
                    .filter(e -> e.name.equals(quest))
                    .map( e -> e.map )
                    .findFirst()
                    .orElse("data not found");
    }

//Exists in
    public List<ExistsIn> getExistsIn() {
        return ExistsIn.DAO.listExists(connection);
    }
    public List<String> getMonstersIn(String game) {
        return this.getExistsIn()
                    .stream()
                    .filter(m -> m.gameName.equals(game))
                    .map(e -> e.monsterName)
                    .collect(Collectors.toList());
    }
    public List<String> getGamesWith(String monster) {
        return this.getExistsIn()
                    .stream()
                    .filter(m -> m.monsterName.equals(monster))
                    .map(e -> e.gameName)
                    .collect(Collectors.toList());
    }

//Lives in
    public List<LivesIn> getLivesIn() {
        return LivesIn.DAO.listLives(connection);
    }
    public List<String> getMonstersInMap(String map) {
        return this.getLivesIn()
                        .stream()
                        .filter(m -> m.map.equals(map))
                        .map(e -> e.monster)
                        .collect(Collectors.toList());
    }
    public List<String> getMapsWith(String monster) {
        return this.getLivesIn()
                        .stream()
                        .filter(m -> m.monster.equals(monster))
                        .map(e -> e.map)
                        .collect(Collectors.toList());
    }

//Target
    public List<Target> getTarget() {
        return Target.DAO.listTarget(connection);
    }
    public List<String> getQuestsWithMonster(String monster) {
        return this.getTarget()
                    .stream()
                    .filter(m -> m.monster.equals(monster))
                    .map(e -> e.quest)
                    .collect(Collectors.toList());
    }
    public List<String> getMonstersInQuest(String quest) {
        return this.getTarget()
                    .stream()
                    .filter(m -> m.quest.equals(quest))
                    .map(e -> e.monster)
                    .collect(Collectors.toList());
    }


//Reward
    public List<Reward> getReward(){
        return Reward.DAO.listReward(connection);
    }
    public List<String> getQuestsFromMaterial(String material){
        return this.getReward()
                    .stream()
                    .filter(e -> e.material.equals(material))
                    .map(e -> e.quest)
                    .collect(Collectors.toList());
    }
    public List<String> getMaterialsFromQuest(String quest){
        return this.getReward()
                    .stream()
                    .filter(e -> e.quest.equals(quest))
                    .map(e -> e.material)
                    .collect(Collectors.toList());
    }

//User
    public List<User> getUser()
    {
        return User.DAO.listUser(connection);
    }
    public boolean userExists(String user) {
        if(this.getUser()
            .stream()
            .filter(e -> e.username.equals(user))
            .findFirst()
            .isEmpty()) {
                return false;
            } else {
                return true;
            }
    }
    public boolean insertUser(String username, String name, String surname, String email, String password) {
        return User.DAO.addUser(connection, username, name, surname,  email,  password);
    }  

//character
    public List<Character> getChar() {
        return Character.DAO.listCharacter(connection);
    }
    public boolean insertCharacter(String username, String game, String ingameName) {
        return Character.DAO.addCharacter(connection, username, game, ingameName);
    }

//Hunt Request
    public void createRequest(String username, String gameName, List<String> monsterNames, String ign) {
        Request.DAO.insertHuntRequest(connection,username, gameName, monsterNames, ign);
    }
    public List<String> getHuntRequestsByGame(String gameName){
        return Request.DAO.getHuntRequestsByGame(connection, gameName);
    }
    public void addCharacterOwnerToGroup(String ign, Integer Id) {
        Request.DAO.addCharacterOwnerToGroup(connection, Id, ign);
    }
//Statistical
    public List<String> getTopHuntedMonsters() {
        return Statistical.DAO.getTopHuntedMonsters(connection);
    }
    public List<String> getBotHuntedMonsters() {
        return Statistical.DAO.getBotHuntedMonsters(connection);
    }
    public List<String> getTopPlayedGames() {
        return Statistical.DAO.getTopPlayedGames(connection);
    }
    public List<String> getBotPlayedGames() {
        return Statistical.DAO.getBotPlayedGames(connection);
    }
    public List<String> getTopQuestSuccessRates() {
        return Statistical.DAO.getTopQuestSuccessRates(connection);
    }
    public List<String> getBotQuestSuccessRates() {
        return Statistical.DAO.getBotQuestSuccessRates(connection);
    }
    public List<String> getTopMonsterSuccessRates() {
        return Statistical.DAO.getTopMonsterSuccessRates(connection);
    }
    public List<String> getBotMonsterSuccessRates() {
        return Statistical.DAO.getBotMonsterSuccessRates(connection);
    }

    
   
}