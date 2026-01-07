package com.chethana.restaurant_management_system.dto;

import com.chethana.restaurant_management_system.model.StaffModel;
import com.chethana.restaurant_management_system.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffDTO {
    public static boolean saveStaff(StaffModel staff) throws SQLException {
        String sql = "INSERT INTO staff (staff_code, name, email, contact, role, username, password, hire_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                staff.getStaffCode(),
                staff.getName(),
                staff.getEmail(),
                staff.getContact(),
                staff.getRole(),
                staff.getUsername(),
                staff.getPassword(),
                staff.getHireDate(),
                staff.getStatus());
    }

    public static boolean updateStaff(StaffModel staff) throws SQLException {
        String sql = "UPDATE staff SET staff_code=?, name=?, email=?, contact=?, role=?, username=?, password=?, hire_date=?, status=? WHERE staff_id=?";
        return CrudUtil.execute(sql,
                staff.getStaffCode(),
                staff.getName(),
                staff.getEmail(),
                staff.getContact(),
                staff.getRole(),
                staff.getUsername(),
                staff.getPassword(),
                staff.getHireDate(),
                staff.getStatus(),
                staff.getStaffId());
    }

    public static boolean deleteStaff(int staffId) throws SQLException {
        String sql = "DELETE FROM staff WHERE staff_id=?";
        return CrudUtil.execute(sql, staffId);
    }

    public static List<StaffModel> getAllStaff() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM staff");
        List<StaffModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<StaffModel> searchStaff(String searchText) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM staff WHERE name LIKE ? OR email LIKE ? OR staff_code LIKE ? OR contact LIKE ?",
                "%" + searchText + "%",
                "%" + searchText + "%",
                "%" + searchText + "%",
                "%" + searchText + "%");
        List<StaffModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<StaffModel> getStaffByRole(String role) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM staff WHERE role=?", role);
        List<StaffModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<String> getDistinctRoles() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT DISTINCT role FROM staff WHERE role IS NOT NULL");
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getString("role"));
        }
        return list;
    }

    public static StaffModel getStaffById(int staffId) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM staff WHERE staff_id=?", staffId);
        if (rs.next()) {
            return mapResultSet(rs);
        }
        return null;
    }

    public static boolean checkUsernameExists(String username) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM staff WHERE username=?", username);
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        return false;
    }

    public static boolean checkStaffCodeExists(String staffCode) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM staff WHERE staff_code=?", staffCode);
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        return false;
    }

    private static StaffModel mapResultSet(ResultSet rs) throws SQLException {
        StaffModel staff = new StaffModel();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setStaffCode(rs.getString("staff_code"));
        staff.setName(rs.getString("name"));
        staff.setEmail(rs.getString("email"));
        staff.setContact(rs.getString("contact"));
        staff.setRole(rs.getString("role"));
        staff.setUsername(rs.getString("username"));
        staff.setPassword(rs.getString("password"));

        java.sql.Date hireDate = rs.getDate("hire_date");
        if (hireDate != null) {
            staff.setHireDate(hireDate.toLocalDate());
        }

        staff.setStatus(rs.getString("status"));
        return staff;
    }
}