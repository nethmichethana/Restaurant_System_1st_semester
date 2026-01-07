package com.chethana.restaurant_management_system.dto;

import com.chethana.restaurant_management_system.model.CustomerModel;
import com.chethana.restaurant_management_system.model.ReservationModel;
import com.chethana.restaurant_management_system.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDTO {
    public static boolean saveReservation(ReservationModel reservation) throws SQLException {
        String sql = "INSERT INTO reservation (customer_id, date, time, number_of_guests, status, special_requests) VALUES (?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                reservation.getCustomerId(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getNumberOfGuests(),
                reservation.getStatus(),
                reservation.getSpecialRequests());
    }

    public static boolean updateReservation(ReservationModel reservation) throws SQLException {
        String sql = "UPDATE reservation SET customer_id=?, date=?, time=?, number_of_guests=?, status=?, special_requests=? WHERE reservation_id=?";
        return CrudUtil.execute(sql,
                reservation.getCustomerId(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getNumberOfGuests(),
                reservation.getStatus(),
                reservation.getSpecialRequests(),
                reservation.getReservationId());
    }

    public static boolean deleteReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM reservation WHERE reservation_id=?";
        return CrudUtil.execute(sql, reservationId);
    }

    public static boolean updateReservationStatus(int reservationId, String status) throws SQLException {
        String sql = "UPDATE reservation SET status=? WHERE reservation_id=?";
        return CrudUtil.execute(sql, status, reservationId);
    }

    public static List<ReservationModel> getAllReservations() throws SQLException {
        String sql = "SELECT r.*, c.name as customer_name, c.contact as customer_contact FROM reservation r LEFT JOIN customer c ON r.customer_id = c.customer_id ORDER BY r.date DESC, r.time DESC";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<ReservationModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<ReservationModel> searchReservations(String searchText) throws SQLException {
        String sql = "SELECT r.*, c.name as customer_name, c.contact as customer_contact FROM reservation r LEFT JOIN customer c ON r.customer_id = c.customer_id WHERE c.name LIKE ? OR c.contact LIKE ? OR r.status LIKE ? ORDER BY r.date DESC, r.time DESC";
        ResultSet rs = CrudUtil.executeQuery(sql, "%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%");
        List<ReservationModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<ReservationModel> getReservationsByDate(LocalDate date) throws SQLException {
        String sql = "SELECT r.*, c.name as customer_name, c.contact as customer_contact FROM reservation r LEFT JOIN customer c ON r.customer_id = c.customer_id WHERE r.date=? ORDER BY r.time";
        ResultSet rs = CrudUtil.executeQuery(sql, date);
        List<ReservationModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<ReservationModel> getReservationsByStatus(String status) throws SQLException {
        String sql = "SELECT r.*, c.name as customer_name, c.contact as customer_contact FROM reservation r LEFT JOIN customer c ON r.customer_id = c.customer_id WHERE r.status=? ORDER BY r.date DESC, r.time DESC";
        ResultSet rs = CrudUtil.executeQuery(sql, status);
        List<ReservationModel> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSet(rs));
        }
        return list;
    }

    public static List<String> getDistinctStatus() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT DISTINCT status FROM reservation WHERE status IS NOT NULL");
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getString("status"));
        }
        return list;
    }

    public static List<CustomerModel> getAllCustomersForCombo() throws SQLException {
        String sql = "SELECT customer_id, name, contact FROM customer ORDER BY name";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<CustomerModel> list = new ArrayList<>();
        while (rs.next()) {
            CustomerModel customer = new CustomerModel();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setContact(rs.getString("contact"));
            list.add(customer);
        }
        return list;
    }

    public static ReservationModel getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT r.*, c.name as customer_name, c.contact as customer_contact FROM reservation r LEFT JOIN customer c ON r.customer_id = c.customer_id WHERE r.reservation_id=?";
        ResultSet rs = CrudUtil.executeQuery(sql, reservationId);
        if (rs.next()) {
            return mapResultSet(rs);
        }
        return null;
    }

    public static int getTotalReservationsCount() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM reservation");
        if (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public static int getReservationsCountByStatus(String status) throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM reservation WHERE status=?", status);
        if (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public static int getTodayReservationsCount() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) as count FROM reservation WHERE date = CURDATE()");
        if (rs.next()) {
            return rs.getInt("count");
        }
        return 0;
    }

    public static int getTotalGuestsCount() throws SQLException {
        ResultSet rs = CrudUtil.executeQuery("SELECT SUM(number_of_guests) as total FROM reservation");
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }

    private static ReservationModel mapResultSet(ResultSet rs) throws SQLException {
        ReservationModel reservation = new ReservationModel();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setCustomerId(rs.getInt("customer_id"));
        reservation.setCustomerName(rs.getString("customer_name"));
        reservation.setCustomerContact(rs.getString("customer_contact"));

        reservation.setReservationDate(rs.getDate("date").toLocalDate());
        reservation.setReservationTime(rs.getTime("time").toLocalTime());

        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        reservation.setStatus(rs.getString("status"));
        reservation.setSpecialRequests(rs.getString("special_requests"));
        return reservation;
    }
}