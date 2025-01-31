package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Material {
    public final String name;
    public final String monster;
    public final int rarity;
    public Material(String name, String monster, int rarity) {
        this.name = name;
        this.monster = monster;
        this.rarity = rarity;
    }
    public final class DAO {
        public static List<Material> listMaterial(Connection connection) {
            var materialList = new ArrayList<Material>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_MATERIAL);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var name = resultSet.getString("Material_Name");
                    var monster = resultSet.getString("Monster_Name");
                    var rarity = resultSet.getInt("Rarity");
                    var material = new Material(name, monster, rarity);
                    materialList.add(material);
                } 
            }  catch (Exception e) {
                throw new DAOException(e);
            }
            return materialList;
        }
    }
}
