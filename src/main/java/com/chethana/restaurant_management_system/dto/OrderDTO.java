package com.chethana.restaurant_management_system.dto;

import com.chethana.restaurant_management_system.model.*;
import com.chethana.restaurant_management_system.util.CrudUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    public static boolean saveOrder(OrderModel order, List<OrderItemModel> orderItems) throws SQLException {
        Connection connection = null;
        try {
            connection = com.chethana.restaurant_management_system.db.DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String orderSql = "INSERT INTO orders (customer_id, staff_id, total_amount, status, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement orderStmt = connection.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getCustomerId());
            orderStmt.setInt(2, order.getStaffId());
            orderStmt.setBigDecimal(3, order.getTotalAmount());
            orderStmt.setString(4, order.getStatus());
            orderStmt.setString(5, order.getPaymentMethod());
            orderStmt.setString(6, order.getNotes());

            int affectedRows = orderStmt.executeUpdate();
            if (affectedRows == 0) {
                connection.rollback();
                return false;
            }

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                connection.rollback();
                return false;
            }
            int orderId = generatedKeys.getInt(1);

            String itemSql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement itemStmt = connection.prepareStatement(itemSql);

            for (OrderItemModel item : orderItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getMenuItemId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setBigDecimal(4, item.getUnitPrice());
                itemStmt.setBigDecimal(5, item.getTotalPrice());
                itemStmt.addBatch();
            }

            itemStmt.executeBatch();
            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status=? WHERE order_id=?";
        return CrudUtil.execute(sql, status, orderId);
    }

    public static boolean deleteOrder(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id=?";
        return CrudUtil.execute(sql, orderId);
    }

    public static List<OrderModel> getAllOrders() throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, s.name as staff_name FROM orders o " +
                "LEFT JOIN customer c ON o.customer_id = c.customer_id " +
                "LEFT JOIN staff s ON o.staff_id = s.staff_id " +
                "ORDER BY o.order_time DESC";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<OrderModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<OrderModel> searchOrders(String searchText) throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, s.name as staff_name FROM orders o " +
                "LEFT JOIN customer c ON o.customer_id = c.customer_id " +
                "LEFT JOIN staff s ON o.staff_id = s.staff_id " +
                "WHERE c.name LIKE ? OR s.name LIKE ? OR o.order_code LIKE ? OR o.status LIKE ? " +
                "ORDER BY o.order_time DESC";
        ResultSet rs = CrudUtil.executeQuery(sql, "%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%");
        List<OrderModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<OrderModel> getOrdersByDate(LocalDate date) throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, s.name as staff_name FROM orders o " +
                "LEFT JOIN customer c ON o.customer_id = c.customer_id " +
                "LEFT JOIN staff s ON o.staff_id = s.staff_id " +
                "WHERE DATE(o.order_time) = ? " +
                "ORDER BY o.order_time DESC";
        ResultSet rs = CrudUtil.executeQuery(sql, date);
        List<OrderModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<OrderModel> getOrdersByStatus(String status) throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, s.name as staff_name FROM orders o " +
                "LEFT JOIN customer c ON o.customer_id = c.customer_id " +
                "LEFT JOIN staff s ON o.staff_id = s.staff_id " +
                "WHERE o.status=? " +
                "ORDER BY o.order_time DESC";
        ResultSet rs = CrudUtil.executeQuery(sql, status);
        List<OrderModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<String> getDistinctStatus() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT DISTINCT status FROM orders WHERE status IS NOT NULL");
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getString("status"));
        }
        return list;
    }

    public static List<CustomerModel> getAllCustomersForCombo() throws SQLException {
        String sql = "SELECT customer_id, name FROM customer ORDER BY name";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<CustomerModel> list = new ArrayList<>();
        while (rs.next()) {
            CustomerModel customer = new CustomerModel();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            list.add(customer);
        }
        return list;
    }

    public static List<StaffModel> getAllWaitersForCombo() throws SQLException {
        String sql = "SELECT staff_id, name FROM staff WHERE role='Waiter' ORDER BY name";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<StaffModel> list = new ArrayList<>();
        while (rs.next()) {
            StaffModel staff = new StaffModel();
            staff.setStaffId(rs.getInt("staff_id"));
            staff.setName(rs.getString("name"));
            list.add(staff);
        }
        return list;
    }

    public static List<MenuModel> getAllMenuItemsForCombo() throws SQLException {
        String sql = "SELECT menu_item_id, name, price FROM menu_item WHERE availability=1 ORDER BY name";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<MenuModel> list = new ArrayList<>();
        while (rs.next()) {
            MenuModel menu = new MenuModel();
            menu.setMenuItemId(rs.getInt("menu_item_id"));
            menu.setName(rs.getString("name"));
            menu.setPrice(rs.getBigDecimal("price"));
            list.add(menu);
        }
        return list;
    }

    public static OrderModel getOrderById(int orderId) throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, s.name as staff_name FROM orders o " +
                "LEFT JOIN customer c ON o.customer_id = c.customer_id " +
                "LEFT JOIN staff s ON o.staff_id = s.staff_id " +
                "WHERE o.order_id=?";
        ResultSet rs = CrudUtil.executeQuery(sql, orderId);
        if (rs.next()) {
            return mapResultSet(rs);
        }
        return null;
    }

    public static List<OrderItemModel> getOrderItems(int orderId) throws SQLException {
        String sql = "SELECT oi.*, mi.name as item_name FROM order_items oi " +
                "LEFT JOIN menu_item mi ON oi.menu_item_id = mi.menu_item_id " +
                "WHERE oi.order_id=?";
        ResultSet rs = CrudUtil.executeQuery(sql, orderId);
        List<OrderItemModel> list = new ArrayList<>();
        while (rs.next()) {
            OrderItemModel item = new OrderItemModel();
            item.setOrderItemId(rs.getInt("order_item_id"));
            item.setOrderId(rs.getInt("order_id"));
            item.setMenuItemId(rs.getInt("menu_item_id"));
            item.setItemName(rs.getString("item_name"));
            item.setQuantity(rs.getInt("quantity"));
            item.setUnitPrice(rs.getBigDecimal("unit_price"));
            item.setTotalPrice(rs.getBigDecimal("total_price"));
            list.add(item);
        }
        return list;
    }

    public static int getTotalOrdersCount() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM orders");
        if (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public static int getOrdersCountByStatus(String status) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM orders WHERE status=?", status);
        if (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public static BigDecimal getTodayRevenue() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT SUM(total_amount) as revenue FROM orders WHERE DATE(order_time) = CURDATE() AND status='Completed'");
        if (rs.next()) {
            BigDecimal revenue = rs.getBigDecimal("revenue");
            return revenue != null ? revenue : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    private static OrderModel mapResultSet(ResultSet rs) throws SQLException {
        OrderModel order = new OrderModel();
        order.setOrderId(rs.getInt("order_id"));
        order.setOrderCode(rs.getString("order_code"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setStaffId(rs.getInt("staff_id"));
        order.setStaffName(rs.getString("staff_name"));
        order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setNotes(rs.getString("notes"));
        return order;
    }
}