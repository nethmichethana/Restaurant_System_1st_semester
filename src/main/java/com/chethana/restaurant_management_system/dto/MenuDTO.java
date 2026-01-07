package com.chethana.restaurant_management_system.dto;

import com.chethana.restaurant_management_system.model.MenuModel;
import com.chethana.restaurant_management_system.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDTO {
    public static boolean saveMenu(MenuModel menu) throws SQLException {
        String sql = "INSERT INTO menu_item (name, category, description, price, availability) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, menu.getName(), menu.getCategory(), menu.getDescription(), menu.getPrice(), menu.isAvailability());
    }

    public static boolean updateMenu(MenuModel menu) throws SQLException {
        String sql = "UPDATE menu_item SET name=?, category=?, description=?, price=?, availability=? WHERE menu_item_id=?";
        return CrudUtil.execute(sql, menu.getName(), menu.getCategory(), menu.getDescription(), menu.getPrice(), menu.isAvailability(), menu.getMenuItemId());
    }

    public static boolean deleteMenu(int menuItemId) throws SQLException {
        String sql = "DELETE FROM menu_item WHERE menu_item_id=?";
        return CrudUtil.execute(sql, menuItemId);
    }

    public static List<MenuModel> getAllMenus() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM menu_item");
        List<MenuModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<MenuModel> searchMenus(String searchText) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM menu_item WHERE name LIKE ? OR description LIKE ?", "%" + searchText + "%", "%" + searchText + "%");
        List<MenuModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<MenuModel> getMenusByCategory(String category) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM menu_item WHERE category=?", category);
        List<MenuModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<String> getDistinctCategories() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT DISTINCT category FROM menu_item WHERE category IS NOT NULL");
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getString("category"));
        }
        return list;
    }

    private static MenuModel mapResultSet(ResultSet rs) throws SQLException {
        MenuModel menu = new MenuModel();
        menu.setMenuItemId(rs.getInt("menu_item_id"));
        menu.setName(rs.getString("name"));
        menu.setCategory(rs.getString("category"));
        menu.setDescription(rs.getString("description"));
        menu.setPrice(rs.getBigDecimal("price"));
        menu.setAvailability(rs.getBoolean("availability"));
        return menu;
    }
}
