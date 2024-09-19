/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucamlite;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class AttendenceS extends JFrame {
    private DefaultTableModel model;

    public AttendenceS(int studentID) {
        setTitle("Attendance History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Create a table model with columns: Date, Status
        model = new DefaultTableModel(new String[]{"Date", "Status"}, 0);

        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Retrieve the student's attendance history from the database
        retrieveAttendanceDataFromDatabase(studentID);
    }

    private void retrieveAttendanceDataFromDatabase(int studentID) {
        // Replace these values with your database connection details
        String jdbcUrl = "jdbc:mysql://localhost:3306/demo";
        String dbUser = "root";
        String dbPassword = "root";

        String query = "SELECT date, status FROM attendence WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing data in the table
            model.setRowCount(0);

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");
                model.addRow(new Object[]{date, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while fetching attendance history: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Provide a UI for selecting the student ID
            JComboBox<Integer> studentIdComboBox = new JComboBox<>();
            // Populate the combo box with available student IDs
            // You can fetch this list from your database
            studentIdComboBox.addItem(1);
            studentIdComboBox.addItem(2);
            studentIdComboBox.addItem(3);
            // Add the combo box to a dialog for user selection
            JPanel panel = new JPanel();
            panel.add(new JLabel("Select Student ID: "));
            panel.add(studentIdComboBox);
            int result = JOptionPane.showConfirmDialog(null, panel, "Student ID Selection", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int selectedStudentID = (Integer) studentIdComboBox.getSelectedItem();
                AttendenceS attendanceWindow = new AttendenceS(selectedStudentID);
                attendanceWindow.setVisible(true);
            }
        });
    }
}
