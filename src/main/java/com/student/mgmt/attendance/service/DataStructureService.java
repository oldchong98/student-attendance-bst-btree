package com.student.mgmt.attendance.service;


import com.student.mgmt.attendance.model.AttendanceRecord;

import java.util.List;

public abstract class DataStructureService {

    public abstract void add(AttendanceRecord attendanceRecord);

    public abstract List<AttendanceRecord> search(Integer studentId, Integer clazzId);

    public abstract void update(AttendanceRecord attendanceRecord);

    public abstract void delete(Integer studentId, Integer clazzId);

    public abstract List<AttendanceRecord> getAll();
}
