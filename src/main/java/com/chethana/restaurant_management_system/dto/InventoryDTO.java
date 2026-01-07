package com.chethana.restaurant_management_system.dto;


import com.chethana.restaurant_management_system.util.CrudUtil;
import com.chethana.restaurant_management_system.model.InventoryModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDTO {

    public static boolean saveInventory(InventoryModel inventory) throws SQLException {
        String sql = "INSERT INTO inventory (item_name, quantity, unit, last_updated) VALUES (?, ?, ?, ?)";
        return CrudUtil.execute(sql, inventory.getItemName(), inventory.getQuantity(), inventory.getUnit(), inventory.getLastUpdated());
    }

    public static boolean updateInventory(InventoryModel inventory) throws SQLException {
        String sql = "UPDATE inventory SET item_name=?, quantity=?, unit=?, last_updated=? WHERE inventory_Id=?";
        return CrudUtil.execute(sql, inventory.getItemName(), inventory.getQuantity(), inventory.getUnit(), inventory.getLastUpdated(), inventory.getInventoryId());
    }

    public static boolean deleteInventory(int inventory_Id) throws SQLException {
        String sql = "DELETE FROM inventory WHERE inventory_Id=?";
        return CrudUtil.execute(sql, inventory_Id);
    }

    public static List<InventoryModel> getAllInventory() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM inventory");
        List<InventoryModel> list = new ArrayList<>();

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static InventoryModel getInventoryById(int inventory_Id) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM inventory WHERE inventory_Id=?", inventory_Id);

        if (rs.next()) {
            return mapResultSet(rs);
        }
        return null;
    }

    public static List<InventoryModel> searchInventory(String searchText) throws SQLException {
        String sql = "SELECT * FROM inventory WHERE item_name LIKE ? OR unit LIKE ?";
        ResultSet rs = CrudUtil.executeQuery(sql, "%" + searchText + "%", "%" + searchText + "%");

        List<InventoryModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static boolean checkItemNameExists(String item_name) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) AS count FROM inventory WHERE item_name=?", item_name);

        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        return false;
    }

    private static InventoryModel mapResultSet(ResultSet rs) throws SQLException {
        return new InventoryModel(rs.getInt("inventory_Id"), rs.getString("item_name"), rs.getInt("quantity"), rs.getString("unit"), rs.getTimestamp("last_updated"));
    }
}
