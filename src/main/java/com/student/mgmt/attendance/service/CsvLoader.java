package com.student.mgmt.attendance.service;

import com.student.mgmt.attendance.backup.bst.Main;
import com.student.mgmt.attendance.model.AttendanceRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

    public static List<AttendanceRecord> getAttendanceList() {
        List<AttendanceRecord> records = new ArrayList<>();

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("attendance_records_10000.csv")) {
            assert inputStream != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; //skip header
                    continue;
                }
                AttendanceRecord record = getAttendanceRecord(line);

                records.add(record);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    private static AttendanceRecord getAttendanceRecord(String line) {
        String[] fields = line.split(",");

        // parse fields
        int studentId = Integer.parseInt(fields[0].trim());
        int clazzId = Integer.parseInt(fields[1].trim());
        LocalDateTime attendTime = LocalDateTime.parse(fields[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String status = fields[3].trim();

        return new AttendanceRecord(studentId, clazzId, attendTime, status);
    }
}
