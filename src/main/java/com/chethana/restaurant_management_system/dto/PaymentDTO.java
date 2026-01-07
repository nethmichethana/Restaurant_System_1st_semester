package com.chethana.restaurant_management_system.dto;

import com.chethana.restaurant_management_system.model.paymentModel;
import com.chethana.restaurant_management_system.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDTO {

    // ---------------- SAVE ----------------
    public static boolean savePayment(paymentModel payment) throws SQLException {
        String sql = "INSERT INTO payments " +
                "(order_id, customer_name, payment_method, amount, payment_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        return CrudUtil.execute(
                sql,
                payment.getOrderId(),
                payment.getCustomerName(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentDate()
        );
    }

    // ---------------- UPDATE ----------------
    public static boolean updatePayment(paymentModel payment) throws SQLException {
        String sql = "UPDATE payments SET " +
                "order_id=?, customer_name=?, payment_method=?, amount=?, payment_date=? " +
                "WHERE payment_id=?";

        return CrudUtil.execute(
                sql,
                payment.getOrderId(),
                payment.getCustomerName(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentId()
        );
    }

    // ---------------- DELETE ----------------
    public static boolean deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM payments WHERE payment_id=?";
        return CrudUtil.execute(sql, paymentId);
    }

    // ---------------- GET ALL ----------------
    public static List<paymentModel> getAllPayments() throws SQLException {
        List<paymentModel> list = new ArrayList<>();

        ResultSet rs = CrudUtil.executeQuery(
                "SELECT * FROM payments ORDER BY payment_date DESC"
        );

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    // ---------------- SEARCH BY DATE ----------------
    public static List<paymentModel> searchByDate(LocalDate from, LocalDate to) throws SQLException {
        List<paymentModel> list = new ArrayList<>();

        ResultSet rs = CrudUtil.executeQuery(
                "SELECT * FROM payments WHERE payment_date BETWEEN ? AND ?",
                from,
                to
        );

        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    // ---------------- MAPPER ----------------
    private static paymentModel mapResultSet(ResultSet rs) throws SQLException {
        paymentModel payment = new paymentModel();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setCustomerName(rs.getString("customer_name"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        return payment;
    }
}
