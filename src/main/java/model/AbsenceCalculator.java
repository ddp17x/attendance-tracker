package model;

import java.util.List;
import java.util.Set;

public class AbsenceCalculator {

    public void calculateAbsences(List<StudentAttendance> attendanceRecords, Set<String> generatedTimeInCodes,
            Set<String> generatedTimeOutCodes) {
        for (StudentAttendance record : attendanceRecords) {
            // Check TimeINCode for absence
            if (record.getTimeInCode() == null || !generatedTimeInCodes.contains(record.getTimeInCode())) {
                record.incrementAbsence();
            }

            // Check TimeOUTCode for absence
            if (record.getTimeOutCode() == null || !generatedTimeOutCodes.contains(record.getTimeOutCode())) {
                record.incrementAbsence();
            }
        }
    }
}
