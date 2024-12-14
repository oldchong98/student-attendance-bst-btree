package com.student.mgmt.attendance.service;

import com.student.mgmt.attendance.model.AttendanceRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;

public class BSTServiceTest {

    private BSTService bstService;

    private List<AttendanceRecord> records = CsvLoader.getAttendanceList();

    @BeforeEach
    public void setUp() {
        bstService = new BSTService();
        Collections.shuffle(records);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void addRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- BST Test Add----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);

        long startTime = System.nanoTime();
        subset.forEach(bstService::add);
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void updateRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- BST Test Update----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);
        subset.forEach(bstService::add);

        long startTime = System.nanoTime();
        subset.forEach(bstService::update);
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void deleteRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- BST Test Delete----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);
        subset.forEach(bstService::add);

        Collections.reverse(subset);
        long startTime = System.nanoTime();
        subset.forEach(record -> {
            bstService.delete(record.getStudentId(), record.getClazzId());
        });
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 2500, 5000, 7500, 10000})
    public void searchRecords(int recordCount) {
        if (recordCount == 10)
            System.out.println("---------- BST Test Search----------");
        List<AttendanceRecord> subset = records.subList(0, recordCount);

        subset.forEach(bstService::add);

        long startTime = System.nanoTime();
        subset.forEach(record -> {
            bstService.search(record.getStudentId(), record.getClazzId());
        });
        long elapsedTime = System.nanoTime() - startTime;

        long meanTimeNs = elapsedTime / subset.size();

        System.out.println("Size: "+subset.size()+"\t Time(ns): "+meanTimeNs);
    }
}
