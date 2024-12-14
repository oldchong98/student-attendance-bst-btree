package com.student.mgmt.attendance.service;

import com.student.mgmt.attendance.model.AttendanceRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;

public class BTreeServiceTest {

    private BTreeService bTreeService;

    private List<AttendanceRecord> records;

    @BeforeEach
    public void setUp() {
        bTreeService = new BTreeService();
        records = CsvLoader.getAttendanceList();
        Collections.shuffle(records);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void addRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- B-Tree Test Add----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);

        long startTime = System.nanoTime();
        subset.forEach(record -> {
            bTreeService.add(record);
        });
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void searchRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- B-Tree Test Search----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);

        subset.forEach(bTreeService::add);

        long startTime = System.nanoTime();
        subset.forEach(record -> {
            bTreeService.search(record.getStudentId(), record.getClazzId());
        });
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void updateRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- B-Tree Test Update----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);
        subset.forEach(bTreeService::add);

        long startTime = System.nanoTime();
        subset.forEach(bTreeService::update);
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void deleteRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- B-Tree Test Delete----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);
        subset.forEach(bTreeService::add);

        long startTime = System.nanoTime();
        Collections.shuffle(subset);
        subset.forEach(record -> {
            bTreeService.delete(record.getStudentId(), record.getClazzId());
        });
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }
}
