package mhtool;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

import mhtool.data.*;
import mhtool.data.Character;
import mhtool.model.Model;


public final class Controller {

    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        Objects.requireNonNull(model, "Controller created with null model");
        Objects.requireNonNull(view, "Controller created with null view");
        this.view = view;
        this.model = model;
     }

//Initial View 
     public void loginForm() {
        this.view.loading();
        this.view.loginForm();
     }
//Admin View
     void admin() {
        this.view.loading();
        this.view.admin();
    }
//User view
    void user(String username) {
        this.view.loading();
        this.view.user(username);
    }
//Registration page
    void register() {
        this.view.loading();
        this.view.register();
    }

//Various view to show all the information in the game, chaining from eachother thanks to clickable panels
    public void monsterView(String monster) {
        this.view.loading();
        this.view.monsterView(monster);
    }
    public void gameView(String game) {
        this.view.loading();
        this.view.gameView(game);
    }
    public void mapView(String map) {
        this.view.loading();
        this.view.mapView(map);
    }
    public void materialView(String material) {
        this.view.loading();
        this.view.materialView(material);
    }

    public void questView(String quest) {
        this.view.loading();
        this.view.questView(quest);
    }

    public void categoryView(String category) {
        this.view.loading();
        this.view.categoryView(category);
    }

    public void deviceView(String device) {
        this.view.loading();
        this.view.deviceView(device);
    }

    public void addCharView(String user) {
        this.view.loading();
        this.view.addCharView(user);
    }
    public void viewCharView(String user) {
        this.view.loading();
        this.view.viewCharView(user);
    }

    public void createRequestView(String user) {
        this.view.loading();
        this.view.createRequestView(user);
    }
    public void joinRequestView(String user) {
        this.view.loading();
        this.view.joinRequestView(user);
    }
    public void statisticalView() {
        this.view.loading();
        this.view.statisticalView();
    }


    
//All Interactions to the the Entity Monster
    public List<String> getMonsters() {
        return this.model.getMonstersList();
    }
    public String getCategory(String monster) {
        return this.model.getCategoryFromMonster(monster);
    }
    public Boolean getVariant(String monster) {
        return this.model.getVariant(monster);
    }
    public List<String> getMonstersFromcategory(String category) {
        return this.model.getMonstersFromcategory(category);
    }
    public List<String> getCategoryList() {
        return this.model.getCategoryList();
    }

//All interactions to the entity Game
    public List<String> getGames() {
        return this.model.getGamesList();
    }
    public String getReleaseDate(String game) {
        return this.model.getReleaseDate(game);
    }
//Device
    public List<String> getDeviceList() {
        return this.model.getDeviceList();
    }
//Released on
    public List<String> getReleasedOnList(String game){
        return this.model.getReleasedOnList(game);
    }
    public List<String> getGamesFromDevice(String device) {
        return this.model.getGamesFromDevice(device);
    }


//Maps
    public List<String> getMaps() {
        return this.model.getMapsList();                 
    }
    public List<String> getMapsfromGame(String game) {
        return this.model.getMapsfromGame(game);
    }
    public String getGamefromMap(String map) {
        return this.model.getGamefromMap(map);
    }

//Materials
    public List<String> getMaterials() {
        return this.model.getMaterialsList();           
    }
    public List<String> getMaterialsFromMonster(String monster) {
        return this.model.getMaterialsFromMonster(monster);
    }
    public String getMonsterFromMaterial(String material) {
        return this.model.getMonsterFromMaterial(material);
    }
    public Integer getRarityfromMaterial(String material) {
        return this.model.getRarityfromMaterial(material);
    }

//Quests
    public List<String> getQuests() {
        return this.model.getQuestsList();       
    }
    public List<String> getQuestsfromGame(String game){
        return this.model.getQuestsfromGame(game);
    }
    public String getGameFromQuest(String quest){
        return this.model.getGameFromQuest(quest);
    }
    public List<String> getQuestsFromMap(String map){
        return this.model.getQuestsFromMap(map);
    }
    public String getMapFromQuest(String quest){
        return this.model.getMapFromQuest(quest);
    }
    
//Monsters that exist in certain Games
    public List<String> getMonstersIn(String game) { 
        return this.model.getMonstersIn(game);
    }
    public List<String> getGamesWith(String monster) {
        return this.model.getGamesWith(monster);
    }

//Monsters that live in certain Maps
    public List<String> getMonstersInMap(String map){
        return this.model.getMonstersInMap(map);
    }
    public List<String> getMapsWith(String monster) {
        return this.model.getMapsWith(monster);
    }

//Target
    public List<String> getQuestsWithMonster(String monster) {
        return this.model.getQuestsWithMonster(monster);
    }
    public List<String> getMonstersInQuest(String quest) {
        return this.model.getMonstersInQuest(quest);
    }

//Reward
    public List<String> getQuestsFromMaterial(String material) {
        return this.model.getQuestsFromMaterial(material);
    }
    public List<String> getMaterialsFromQuest(String quest){
        return this.model.getMaterialsFromQuest(quest);
    }

    

    


//User
    public List<User> getUser(){
        return this.model.getUser();
    }
    public boolean userExists(String user) {
        return this.model.userExists(user);
    }
    public boolean insertUser(String username,String name,String surname, String email, String password) {
        return this.model.insertUser(username, name, surname,  email,  password);
    }

//Character
    public List<Character> getChar(){
        return this.model.getChar();
    }
    public boolean insertCharacter(String username,String game,String ingameName) {
        return this.model.insertCharacter(username, game, ingameName);
    }

//Hunt Request
    public void createRequest(String username, String gameName, List<String> monsterNames, String ign) {
        this.model.createRequest(username,  gameName,  monsterNames,  ign);
    }
    public List<String> getHuntRequestsByGame( String gameName) {
        return this.model.getHuntRequestsByGame(gameName);
    }
    public void addCharacterOwnerToGroup(String ign, Integer Id) {
        this.model.addCharacterOwnerToGroup(ign, Id);
    }
    
//Statistical
    public List<String> getTopHuntedMonsters() {
        return this.model.getTopHuntedMonsters();
    }
    public List<String> getBotHuntedMonsters() {
        return this.model.getBotHuntedMonsters();
    }
    public List<String> getTopPlayedGames() {
        return this.model.getTopPlayedGames();
    }
    public List<String> getBotPlayedGames() {
        return this.model.getBotPlayedGames();
    }
    public List<String> getTopQuestSuccessRates() {
        return this.model.getTopQuestSuccessRates();
    }
    public List<String> getBotQuestSuccessRates() {
        return this.model.getBotQuestSuccessRates();
    }
    
    public List<String> getTopMonsterSuccessRates(){
        return this.model.getTopMonsterSuccessRates();
    }
    public List<String> getBotMonsterSuccessRates(){
        return this.model.getBotMonsterSuccessRates();
    }
    public List<String> getTopSuccessfulHunters(){
        return this.model.getTopSuccessfulHunters();
    }
    public List<String> getBotSuccessfulHunters(){
        return this.model.getBotSuccessfulHunters();
    }
    
//ADMIN INPUTS
    public void addMonster(String monster,String category,Boolean variant)
    {
        this.model.addMonster( monster, category, variant);
    }
    public  void addGame(String game,String date) {
         this.model.addGame(game, date);
    }
    public  void addMap(String map,String game) {
        this.model.addMap(map, game);
    }
    public  void addDevice(String device) {
        this.model.addDevice(device);
    }
    public void addGameDev(String game, String Device) {
        this.model.addGameDev(game, Device);
    }
    public void addMongame(String mon, String game) {
        this.model.addMongame(mon, game);
    }
    public void addMonMap( String mon,String map, String game){
        this.model.addMonMap(mon, map, game);
    }
    public void addMaterial(String mat, String mon, Integer rar) {
        this.model.addMaterial(mat, mon, rar);
    }
    public void addQuest(String questName, String gameName, String mapName, List<String> targets)
    {
        this.model.addQuest(questName, gameName, mapName, targets);
    }

}

  