package mhtool.model;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import mhtool.data.*;
import mhtool.data.Character;


public interface Model {

//All Interactions to the the Entity Monster
    public List<Monster> getMonsters();
    public List<String> getMonstersList();
    public String getCategoryFromMonster(String monster);
    public List<String> getMonstersFromcategory(String category);
    public Boolean getVariant(String monster);
    public List<String> getCategoryList();

//All interactions to the entity Game
    public List<Game> getGames();
    public List<String> getGamesList();
    public String getReleaseDate(String game);
//Device
    public List<String> getDeviceList();
//Released on
    public List<ReleasedOn> getReleasedOn();
	public List<String> getReleasedOnList(String game);
    public List<String> getGamesFromDevice(String device);

//Maps
    public List<Maps> getMaps();
    public List<String> getMapsList();
    public List<String> getMapsfromGame(String game);
    public String getGamefromMap(String map);
    
//Materials
    public List<Material> getMaterials();
    public List<String> getMaterialsList();
    public List<String> getMaterialsFromMonster(String monster);
    public String getMonsterFromMaterial(String material);
    public Integer getRarityfromMaterial(String material);

//Quests
    public List<Quest> getQuests();
    public List<String> getQuestsList();
    public List<String> getQuestsfromGame(String game);
    public String getGameFromQuest(String quest);
    public List<String> getQuestsFromMap(String map);
    public String getMapFromQuest(String quest);

//Exists in
    public List<ExistsIn> getExistsIn();
    public List<String> getMonstersIn(String game);
    public List<String> getGamesWith(String monster);
//Lives in
    public List<LivesIn> getLivesIn();
    public List<String> getMonstersInMap(String map);
    public List<String> getMapsWith(String monster);

//Target
    public List<Target> getTarget();
    public List<String> getQuestsWithMonster(String monster);
    public List<String> getMonstersInQuest(String quest);

//Reward
    public List<Reward> getReward();
    public List<String> getQuestsFromMaterial(String material);
    public List<String> getMaterialsFromQuest(String quest);

    public static Model fromConnection(Connection connection) {
        return new ModelImpl(connection);
    }
//User
    public List<User> getUser();
    public boolean userExists(String user);
    public boolean insertUser(String username, String name, String surname, String email, String password);


//Character
    public boolean insertCharacter(String username, String game, String ingameName);
    public List<Character> getChar();
    
 
    
//Hunt Request
    public void createRequest(String username, String gameName, List<String> monsterNames, String ign);
    public List<String> getHuntRequestsByGame(String gameName);
	public void addCharacterOwnerToGroup(String ign, Integer Id);


//Statistical
    public List<String> getTopHuntedMonsters();
    public List<String> getBotHuntedMonsters();
	public List<String> getTopPlayedGames();
    public List<String> getBotPlayedGames();
    public List<String> getTopQuestSuccessRates();
    public List<String> getBotQuestSuccessRates();
    public List<String> getTopMonsterSuccessRates();
    public List<String> getBotMonsterSuccessRates();
    public List<String> getTopSuccessfulHunters();
    public List<String> getBotSuccessfulHunters();

//ADMIN INPUTS
    public void addMonster(String monster,String category,Boolean variant);
	public void addGame(String name,String release);
    public  void addMap(String map,String game);
    public  void addDevice(String device);
    public void addGameDev(String game, String Device);
	public void addMongame(String mon, String game);
	public void addMonMap(String mon, String map, String game);
	public void addMaterial(String mat, String mon, Integer rar);
	public void addQuest(String questName, String gameName, String mapName, List<String> targets);
    
    
    

    
}