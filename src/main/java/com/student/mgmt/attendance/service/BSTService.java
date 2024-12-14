package com.student.mgmt.attendance.service;


import com.student.mgmt.attendance.model.AttendanceRecord;

import java.util.ArrayList;
import java.util.List;

public class BSTService extends DataStructureService {
    private Node root;

    public static class Node {
        AttendanceRecord record;
        Node left;
        Node right;

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public AttendanceRecord getRecord() {
            return record;
        }

        public Node(AttendanceRecord record) {
            this.record = record;
        }
    }

    public Node getRoot() {
        return root;
    }

    public int getDepth() {
        return calculateDepth(root);
    }

    // Recursive helper method to calculate depth
    private int calculateDepth(Node node) {
        if (node == null) {
            return 0;
        }

        int leftDepth = calculateDepth(node.left);
        int rightDepth = calculateDepth(node.right);

        return Math.max(leftDepth, rightDepth) + 1;
    }

    @Override
    public List<AttendanceRecord> getAll() {
        List<AttendanceRecord> records = new ArrayList<>();
        inOrderRec(root, records);
        return records;
    }

    @Override
    public void add(AttendanceRecord record) {
        this.root = this.addRecursive(this.root, record);
    }

    @Override
    public List<AttendanceRecord> search(Integer studentId, Integer clazzId) {
        List<AttendanceRecord> results = new ArrayList<>();
        searchRecursive(this.root, studentId, clazzId, results);
        return results;
    }

    @Override
    public void update(AttendanceRecord record) {
        List<AttendanceRecord> results = search(record.getStudentId(), record.getClazzId());
        if (!results.isEmpty()) {
            AttendanceRecord existingRecord = results.get(0);
            existingRecord.setStatus(record.getStatus());
            existingRecord.setAttendTime(record.getAttendTime());
        }
    }

    @Override
    public void delete(Integer studentId, Integer clazzId) {
        this.root = this.deleteRecursive(this.root, studentId, clazzId);
    }

    private Node addRecursive(Node current, AttendanceRecord record) {
        if (current == null) {
            return new Node(record);
        } else {
            if (record.compareTo(current.record) < 0) {
                current.left = this.addRecursive(current.left, record);
            } else if (record.compareTo(current.record) > 0) {
                current.right = this.addRecursive(current.right, record);
            }

            return current;
        }
    }

    private void searchRecursive(Node current, Integer studentId, Integer clazzId, List<AttendanceRecord> results) {
        if (current == null) {
            return; // Base case: leaf node
        }

        // Check if current node matches the search criteria
        if ((studentId == null || current.record.getStudentId() == studentId) &&
                (clazzId == null || current.record.getClazzId() == clazzId)) {
            results.add(current.record);
        }

        // Continue search in both left and right subtrees
        searchRecursive(current.left, studentId, clazzId, results);
        searchRecursive(current.right, studentId, clazzId, results);
    }

    private Node deleteRecursive(Node current, int studentId, int clazzId) {
        if (current == null) {
            return null; // node not found
        }

        // find the node to delete
        if (studentId < current.record.getStudentId() ||
                (studentId == current.record.getStudentId() && clazzId < current.record.getClazzId())) {
            current.left = this.deleteRecursive(current.left, studentId, clazzId);
        } else if (studentId > current.record.getStudentId() ||
                (studentId == current.record.getStudentId() && clazzId > current.record.getClazzId())) {
            current.right = this.deleteRecursive(current.right, studentId, clazzId);
        } else {
            // node to delete found
            if (current.left == null && current.right == null) {
                // leaf node
                return null;
            } else if (current.left == null) {
                // one child on right
                return current.right;
            } else if (current.right == null) {
                // one child on left
                return current.left;
            } else {
                // Two children
                // Find the smallest in the right subtree
                Node smallestNode = findSmallest(current.right);
                // Replace current node's record
                current.record = smallestNode.record;
                // Remove the in-order successor
                current.right = this.deleteRecursive(current.right,
                        smallestNode.record.getStudentId(), smallestNode.record.getClazzId());
            }
        }

        return current;
    }

    private void inOrderRec(Node root, List<AttendanceRecord> records) {
        if (root != null) {
            inOrderRec(root.left, records);
            records.add(root.record);
            inOrderRec(root.right, records);
        }
    }


    private Node findSmallest(Node root) {
        return root.left == null ? root : findSmallest(root.left);
    }
}
