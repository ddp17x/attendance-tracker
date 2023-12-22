package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import db.StudentDAO;
import model.AbsenceCalculator;
import model.StudentAttendance;
import utility.CSVParser;

public class MainApplication extends JFrame {

    public MainApplication() {
        initUI();
    }

    private void initUI() {
        setTitle("Attendance Tracker");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JButton viewStudentsButton = new JButton("View Student Data");
        JButton uploadCSVButton = new JButton("Upload CSV File");

        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openViewStudentsScreen();
            }
        });

        uploadCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCSVFileChooser();
            }
        });

        mainPanel.add(viewStudentsButton);
        mainPanel.add(uploadCSVButton);

        add(mainPanel);
    }

    private void openViewStudentsScreen() {
        ViewStudentsFrame viewStudentsFrame = new ViewStudentsFrame();
        viewStudentsFrame.setVisible(true);
    }

    private void openCSVFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(MainApplication.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            processCSVFile(filePath);
        }
    }

    private void processCSVFile(String filePath) {
        CSVParser parser = new CSVParser();
        CSVParser.ParseResult parseResult = parser.parseCSV(filePath);
        List<StudentAttendance> attendanceRecords = parseResult.getAttendanceRecords();
        Set<String> generatedTimeInCodes = parseResult.getGeneratedTimeInCodes();
        Set<String> generatedTimeOutCodes = parseResult.getGeneratedTimeOutCodes();

        AbsenceCalculator calculator = new AbsenceCalculator();
        calculator.calculateAbsences(attendanceRecords, generatedTimeInCodes, generatedTimeOutCodes);

        // Update Database
        StudentDAO studentDAO = new StudentDAO();
        studentDAO.updateStudentAbsences(attendanceRecords);

        // Display a confirmation message
        JOptionPane.showMessageDialog(null, "Attendance data processed and updated successfully.");
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        MainApplication mainApp = new MainApplication();
        mainApp.setVisible(true);
    }
}
