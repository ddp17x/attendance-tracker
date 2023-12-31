package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.StudentAttendance;

public class CSVParser {

    public ParseResult parseCSV(String filePath) {
        List<StudentAttendance> attendanceRecords = new ArrayList<>();
        Set<String> generatedTimeInCodes = new HashSet<>();
        Set<String> generatedTimeOutCodes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String studentNumber = values.length > 0 ? values[0].trim() : "";
                String timeInCode = values.length > 1 && !values[1].trim().isEmpty() ? values[1].trim() : null;
                String timeOutCode = values.length > 2 && !values[2].trim().isEmpty() ? values[2].trim() : null;
                String generatedTimeInCode = values.length > 3 ? values[3].trim() : "";
                String generatedTimeOutCode = values.length > 4 ? values[4].trim() : "";

                if (!generatedTimeInCode.isEmpty()) {
                    generatedTimeInCodes.add(generatedTimeInCode);
                }
                if (!generatedTimeOutCode.isEmpty()) {
                    generatedTimeOutCodes.add(generatedTimeOutCode);
                }

                // Handling rows with empty StudentNumber
                if (studentNumber.isEmpty() && timeInCode == null && timeOutCode == null) {
                    StudentAttendance record = new StudentAttendance(
                            "EmptyStudentNumber", null, null, generatedTimeInCode, generatedTimeOutCode);
                    attendanceRecords.add(record);
                } else if (!studentNumber.isEmpty()) {
                    StudentAttendance record = new StudentAttendance(
                            studentNumber, timeInCode, timeOutCode, generatedTimeInCode, generatedTimeOutCode);
                    attendanceRecords.add(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ParseResult(attendanceRecords, generatedTimeInCodes, generatedTimeOutCodes);
    }

    public static class ParseResult {
        private final List<StudentAttendance> attendanceRecords;
        private final Set<String> generatedTimeInCodes;
        private final Set<String> generatedTimeOutCodes;

        public ParseResult(List<StudentAttendance> attendanceRecords, Set<String> generatedTimeInCodes,
                Set<String> generatedTimeOutCodes) {
            this.attendanceRecords = attendanceRecords;
            this.generatedTimeInCodes = generatedTimeInCodes;
            this.generatedTimeOutCodes = generatedTimeOutCodes;
        }

        public List<StudentAttendance> getAttendanceRecords() {
            return attendanceRecords;
        }

        public Set<String> getGeneratedTimeInCodes() {
            return generatedTimeInCodes;
        }

        public Set<String> getGeneratedTimeOutCodes() {
            return generatedTimeOutCodes;
        }
    }
}
