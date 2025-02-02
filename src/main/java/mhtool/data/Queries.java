package mhtool.data;

public class Queries {

    public static final String FIND_MONSTER =
        """
        select variant, category
        from MONSTERS
        where Monster_Name = ?
        """;

    public static final String LIST_MONSTER = 
        """
        select Monster_Name, category, variant
        from MONSTER
        """;

    public static final String LIST_GAMES = 
        """
        select Game_Name, Release_Date
        from GAME
        """;

    public static final String LIST_MAP = 
        """
        select Map_Name, Game_Name
        from MAP
        """;

    public static final String LIST_MATERIAL = 
        """
        select Material_name, Monster_name, rarity
        from Material
        """;

    public static final String LIST_DEVICE = 
        """
        select Device_Name
        from Device
                """;

    public static final String LIST_USER = 
        """
        select username, name, surname, email, password
        from USER
                """;

    public static final String LIST_CHARACTER = 
        """
        select IGN, Username, Game_Name
        from `CHARACTER`
                """;

    public static final String LIST_EXISTS = 
        """
        select Game_Name, Monster_Name, first_appearance
        from Exists_in
                """;

    public static final String LIST_QUEST = 
        """
        select Game_Name, Quest_Name, Map_Name
        from HUNTING_QUEST
                """;

    public static final String LIST_LIVES = 
        """
        select Map_Name, Game_Name, Monster_Name
        from LIVES_IN
                """;

    public static final String LIST_RELEASED = 
        """
        select Game_Name, Device_Name
        from RELEASED_ON
                """;

    public static final String LIST_TARGET = 
        """ 
        select Monster_Name, Quest_Name, Game_Name
        from TARGET
                """;

    public static final String LIST_REWARD = 
        """
        select Material_Name, Quest_Name
        from REWARD
                """;
    public static final String INSERT_USER =
        """
        INSERT INTO User (Username, Name, Surname, Email, Password)
            VALUES ( ?, ?, ?, ? ,?)
                """;

	public static final String INSERT_CHARACTER =
        """
        INSERT INTO `CHARACTER` (Username, Game_Name, IGN)
            VALUES( ?, ?, ? )
                """;

    public static final String insertHuntSQL = "INSERT INTO `HUNT_REQUEST` (Username, Game_Name) VALUES (?, ?)";
    public static final String insertObjectiveSQL = "INSERT INTO `REQUEST_OBJECTIVE` (Request_Id, Monster_Name) VALUES (?, ?)";
    public static final String insertGroupSQL = "INSERT INTO `GROUP` (Request_Id) VALUES (?)";
    public static final String insertPartOfSQL = "INSERT INTO `PART_OF` (Username, Game_Name, IGN, Group_Id) VALUES (?, ?, ?, ?)";
    public static final String huntSQL = """
        SELECT hr.Request_Id, hr.Username
        FROM HUNT_REQUEST hr
        JOIN `GROUP` g ON hr.Request_Id = g.Request_Id
        JOIN `PART_OF` p ON g.Group_Id = p.Group_Id
        WHERE hr.Game_Name = ?
        GROUP BY hr.Request_Id
        HAVING COUNT(p.Username) < 4
        """;
    public static final String monstersSQL = "SELECT Monster_Name FROM REQUEST_OBJECTIVE WHERE Request_Id = ?";
    public static final String membersSQL = "SELECT Username FROM `PART_OF` WHERE Group_Id = (SELECT Group_Id FROM `GROUP` WHERE Request_Id = ?)";


    public static final String findOwnerQuery = "SELECT Username, Game_Name FROM `CHARACTER` WHERE IGN = ?";
    public static final String findGroupQuery = "SELECT Group_Id FROM `GROUP` WHERE Request_Id = ?";
    public static final String insertIntoPartOfQuery = "INSERT INTO `PART_OF` (Username, IGN, Game_Name, Group_Id) VALUES (?, ?, ?, ?)";

    public static final String getHuntedTop =
        """
            SELECT t.Monster_Name, COUNT(r.Quest_Name) AS Hunt_Count 
            FROM TARGET t 
            JOIN HUNTING_QUEST hq ON t.Quest_Name = hq.Quest_Name 
            JOIN RUNS r ON hq.Quest_Name = r.Quest_Name 
            GROUP BY t.Monster_Name 
            ORDER BY Hunt_Count DESC 
            LIMIT 10
                """;
    public static final String getHuntedBot =
        """
            SELECT t.Monster_Name, COUNT(r.Quest_Name) AS Hunt_Count 
            FROM TARGET t 
            JOIN HUNTING_QUEST hq ON t.Quest_Name = hq.Quest_Name 
            JOIN RUNS r ON hq.Quest_Name = r.Quest_Name 
            GROUP BY t.Monster_Name 
            ORDER BY Hunt_Count ASC 
            LIMIT 10
                """;
    public static final String getTopPlayedGames =
    """
            SELECT
            g.Game_Name,
            COUNT(r.Quest_Name) AS Run_Count
            FROM
            GAME g
            JOIN
            HUNTING_QUEST hq ON g.Game_Name = hq.Game_Name
            JOIN
            RUNS r ON r.Quest_Name = hq.Quest_Name
            GROUP BY
            g.Game_Name
            ORDER BY
            Run_Count DESC
            LIMIT 10;
            """;
    public static final String getBotPlayedGames =
    """
            SELECT
            g.Game_Name,
            COUNT(r.Quest_Name) AS Run_Count
            FROM
            GAME g
            JOIN
            HUNTING_QUEST hq ON g.Game_Name = hq.Game_Name
            JOIN
            RUNS r ON r.Quest_Name = hq.Quest_Name
            GROUP BY
            g.Game_Name
            ORDER BY
            Run_Count ASC
            LIMIT 10;
            """;

    public static final String getTopQuestSuccessRates= 
    """
            SELECT
            r.`Quest_Name`,
            COUNT(*) AS Total_Runs,
            SUM(CASE WHEN r.`result` = '1' THEN 1 ELSE 0 END) AS Successful_Runs,
            CASE 
                WHEN COUNT(*) = 0 THEN 0
                ELSE (SUM(CASE WHEN r.`result` = '1' THEN 1 ELSE 0 END) / COUNT(*)) * 100 
            END AS Success_Percentage
            FROM
                `RUNS` r
            GROUP BY
                r.`Quest_Name`
            ORDER BY
                Success_Percentage DESC
            
            """;

    public static final String getBotQuestSuccessRates =
    """
            SELECT
            r.`Quest_Name`,
            COUNT(*) AS Total_Runs,
            SUM(CASE WHEN r.`result` = '1' THEN 1 ELSE 0 END) AS Successful_Runs,
            CASE 
                WHEN COUNT(*) = 0 THEN 0
                ELSE (SUM(CASE WHEN r.`result` = '1' THEN 1 ELSE 0 END) / COUNT(*)) * 100 
            END AS Success_Percentage
            FROM
                `RUNS` r
            GROUP BY
                r.`Quest_Name`
            ORDER BY
                Success_Percentage ASC
            """;

    public static final String getTopMonsterSuccessRates=
    """
            SELECT
                t.Monster_Name,
                COUNT(*) AS Total_Runs,
                SUM(CASE WHEN r.result = '1' THEN 1 ELSE 0 END) AS Successful_Runs,
                CASE 
                    WHEN COUNT(*) = 0 THEN 0
                    ELSE (SUM(CASE WHEN r.result = '1' THEN 1 ELSE 0 END) / COUNT(*)) * 100 
                END AS Success_Percentage
            FROM
                TARGET t
            JOIN
                HUNTING_QUEST hq ON t.Quest_Name = hq.Quest_Name
            JOIN
                RUNS r ON hq.Quest_Name = r.Quest_Name
            GROUP BY
                t.Monster_Name
            ORDER BY
                Success_Percentage DESC;
            """;
    public static final String getBotMonsterSuccessRates=
    """
            SELECT
                t.Monster_Name,
                COUNT(*) AS Total_Runs,
                SUM(CASE WHEN r.result = '1' THEN 1 ELSE 0 END) AS Successful_Runs,
                CASE 
                    WHEN COUNT(*) = 0 THEN 0
                    ELSE (SUM(CASE WHEN r.result = '1' THEN 1 ELSE 0 END) / COUNT(*)) * 100 
                END AS Success_Percentage
            FROM
                TARGET t
            JOIN
                HUNTING_QUEST hq ON t.Quest_Name = hq.Quest_Name
            JOIN
                RUNS r ON hq.Quest_Name = r.Quest_Name
            GROUP BY
                t.Monster_Name
            ORDER BY
                Success_Percentage ASC;
            """;
    public static final String getTopSuccessfulHunters=
    """
            SELECT
            u.Username,
            COUNT(*) AS Total_Runs,
            SUM(CASE WHEN r.result = 1 THEN 1 ELSE 0 END) AS Successful_Runs,
            (SUM(CASE WHEN r.result = 1 THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS
            Success_Percentage
            FROM
            RUNS r
            JOIN
            `GROUP` g ON r.Group_Id = g.Group_Id
            JOIN
            PART_OF p ON g.Group_Id = p.Group_Id
            JOIN
            `CHARACTER` c ON p.IGN = c.IGN AND p.Game_Name = c.Game_Name
            JOIN
            USER u ON c.Username = u.Username
            GROUP BY
            u.Username
            ORDER BY
            Success_Percentage DESC;
            """;
    public static final String getBotSuccessfulHunters=
        """
            SELECT
            u.Username,
            COUNT(*) AS Total_Runs,
            SUM(CASE WHEN r.result = 1 THEN 1 ELSE 0 END) AS Successful_Runs,
            (SUM(CASE WHEN r.result = 1 THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS
            Success_Percentage
            FROM
            RUNS r
            JOIN
            `GROUP` g ON r.Group_Id = g.Group_Id
            JOIN
            PART_OF p ON g.Group_Id = p.Group_Id
            JOIN
            `CHARACTER` c ON p.IGN = c.IGN AND p.Game_Name = c.Game_Name
            JOIN
            USER u ON c.Username = u.Username
            GROUP BY
            u.Username
            ORDER BY
            Success_Percentage ASC;
            """;

    public static final String INSERT_MONSTER = 
            """ 
            INSERT INTO MONSTER (Monster_Name, Category, Variant)
            VALUES (?, ?, ?)
                    
                    """;     
    public static final String INSERT_GAME =   
        """
            INSERT INTO GAME (Game_Name, Release_Date)
            values (?, ?)
                """;
    public static final String  INSERT_MAP=   
        """
            INSERT INTO MAP (Map_Name, Game_Name)
            VALUES (?,?)
                """;
    public static final String  INSERT_DEVICE=   
        """
            INSERT INTO DEVICE (Device_Name)
            VALUES (?)
                """;

    public static final String  INSERT_GAMETODEVICE=   
        """
            INSERT INTO RELEASED_ON (Game_Name, Device_Name)
            VALUE (?, ?)
                """;

    public static final String  INSERT_MONSTERTOMAP=   
        """
            INSERT INTO LIVES_IN (Monster_Name, Map_Name, Game_Name)
            VALUE (?, ?, ?)
                """;
    public static final String  INSERT_MATERIAL=   
        """
            INSERT INTO MATERIAL (Material_name, Monster_Name, Rarity)
            VALUES (?, ?, ?)
                """;
    
    public static final String  INSERT_QUEST=   
        """
            INSERT INTO HUNTING_QUEST (Quest_Name, Game_Name, Map_Name)
            Values (?, ?, ?)
                """;
    public static final String  INSERT_TARGET=   
        """
            INSERT INTO TARGET (Monster_Name, Quest_Name, Game_name)
            values( ?, ?, ?)
                """;
    public static final String  INSERT_MONSTERTOGAME=   
        """
            INSERT INTO Exists_IN (Monster_name, Game_Name)
            values (?, ?)
                """;
                        
    


}

