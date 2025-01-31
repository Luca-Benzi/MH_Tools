package mhtool.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Device {
    public final String name;
    public Device(String name) {
        this.name = name;
    }
    public final class DAO {
        public static List<Device> listDevice(Connection connection) {
            var deviceList = new ArrayList<Device>();
            try ( 
                var statement = DAOUtils.prepare(connection, Queries.LIST_DEVICE);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var name = resultSet.getString("Device_Name");
                    var device = new Device(name);
                    deviceList.add(device);
                } 
            }  catch (Exception e) {
                throw new DAOException(e);
            }
            return deviceList;
        }
    }
}
