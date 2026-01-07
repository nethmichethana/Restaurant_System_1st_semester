package com.chethana.restaurant_management_system.dto;

import com.chethana.restaurant_management_system.db.DBConnection;
import com.chethana.restaurant_management_system.model.CustomerModel;
import com.chethana.restaurant_management_system.util.CrudUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class CustomerDTO {
    public static boolean saveCustomer(CustomerModel customer) throws SQLException {
        String sql = "INSERT INTO customer (customer_code, name, email, contact) VALUES (?, ?, ?, ?)";
        return CrudUtil.execute(sql, customer.getCustomerCode(), customer.getName(), customer.getEmail(), customer.getContact());
    }

    public static boolean updateCustomer(CustomerModel customer) throws SQLException {
        String sql = "UPDATE customer SET customer_code=?, name=?, email=?, contact=? WHERE customer_id=?";
        return CrudUtil.execute(sql, customer.getCustomerCode(), customer.getName(), customer.getEmail(), customer.getContact(), customer.getCustomerId());
    }

    public static boolean deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customer WHERE customer_id=?";
        return CrudUtil.execute(sql, customerId);
    }

    public static List<CustomerModel> getAllCustomers() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM customer");
        List<CustomerModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<CustomerModel> searchCustomers(String searchText) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM customer WHERE name LIKE ? OR email LIKE ? OR customer_code LIKE ? OR contact LIKE ?", "%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%");
        List<CustomerModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static CustomerModel getCustomerById(int customerId) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM customer WHERE customer_id=?", customerId);
        if (rs.next()) {
            return mapResultSet(rs);
        }
        return null;
    }

    public static boolean checkCustomerCodeExists(String customerCode) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM customer WHERE customer_code=?", customerCode);
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        return false;
    }

    public static boolean checkEmailExists(String email) throws SQLException {
        if (email == null || email.isEmpty()) return false;
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM customer WHERE email=?", email);
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        return false;
    }

    private static CustomerModel mapResultSet(ResultSet rs) throws SQLException {
        CustomerModel customer = new CustomerModel();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setCustomerCode(rs.getString("customer_code"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setContact(rs.getString("contact"));
        return customer;
    }
    public void printCustomerReport() throws Exception{


        Connection conn = DBConnection.getInstance().getConnection();

        InputStream inputStream = getClass().getResourceAsStream("/report/customer.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(inputStream);

        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn); // (jr, params, connection_obj)

        JasperViewer.viewReport(jp, false);

    }
}