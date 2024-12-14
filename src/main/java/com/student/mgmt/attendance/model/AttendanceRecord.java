package com.student.mgmt.attendance.model;

import java.time.LocalDateTime;

public class AttendanceRecord implements Comparable<AttendanceRecord> {

    private int studentId;

    private int clazzId;

    private LocalDateTime attendTime;

    private String status;

    public AttendanceRecord() {
    }

    public AttendanceRecord(int studentId, int clazzId, LocalDateTime attendTime, String status) {
        this.studentId = studentId;
        this.clazzId = clazzId;
        this.attendTime = attendTime;
        this.status = status;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClazzId() {
        return clazzId;
    }

    public void setClazzId(int clazzId) {
        this.clazzId = clazzId;
    }

    public LocalDateTime getAttendTime() {
        return attendTime;
    }

    public void setAttendTime(LocalDateTime attendTime) {
        this.attendTime = attendTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int compareTo(AttendanceRecord other) {
        if (this.studentId != other.studentId) {
            return Integer.compare(this.studentId, other.studentId);
        }

        // If studentId is the same, compare by clazzId
        return Integer.compare(this.clazzId, other.clazzId);
    }

    public String toString() {
        return "AttendanceRecord{studentId=" + this.studentId + ", class=" + this.clazzId +  ", attendTime=" + this.attendTime + "', status='" + this.status + "'}";
    }


}
