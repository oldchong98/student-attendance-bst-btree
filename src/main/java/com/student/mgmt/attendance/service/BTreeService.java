package com.student.mgmt.attendance.service;

import com.student.mgmt.attendance.model.AttendanceRecord;

import java.util.ArrayList;
import java.util.List;

public class BTreeService extends DataStructureService {
    private static final int T = 3; // Minimum degree
    private Node root;

    public static class Node {
        int numKeys;
        boolean isLeaf;
        List<AttendanceRecord> keys; // Keys in the node
        List<Node> children;

        public Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
        }
    }

    public BTreeService() {
        this.root = new Node(true);
    }

    public Node getRoot() {
        return root;
    }

    @Override
    public List<AttendanceRecord> getAll() {
        List<AttendanceRecord> records = new ArrayList<>();
        traverseAndCollect(root, records);
        return records;
    }

    private void traverseAndCollect(Node node, List<AttendanceRecord> records) {
        if (node == null) return;

        for (int i = 0; i < node.numKeys; i++) {
            if (!node.isLeaf) {
                traverseAndCollect(node.children.get(i), records);
            }
            records.add(node.keys.get(i));
        }

        if (!node.isLeaf) {
            traverseAndCollect(node.children.get(node.numKeys), records);
        }
    }

    @Override
    public void add(AttendanceRecord record) {
        Node r = root;
        if (r.numKeys == 2 * T - 1) {
            Node s = new Node(false);
            s.children.add(r);
            splitChild(s, 0, r);
            root = s;
        }
        insertNonFull(root, record);
    }

    @Override
    public List<AttendanceRecord> search(Integer studentId, Integer classId) {
        List<AttendanceRecord> result = new ArrayList<>();
        searchHelper(root, studentId, classId, result);
        return result;
    }

    private void searchHelper(Node node, Integer studentId, Integer classId, List<AttendanceRecord> result) {
        if (node == null) return;

        for (int i = 0; i < node.numKeys; i++) {
            AttendanceRecord record = node.keys.get(i);

            boolean matchesStudentId = (studentId == null || record.getStudentId() == studentId);
            boolean matchesClassId = (classId == null || record.getClazzId() == classId);

            if (matchesStudentId && matchesClassId) {
                result.add(record);
            }

            if (!node.isLeaf) {
                searchHelper(node.children.get(i), studentId, classId, result);
            }
        }

        if (!node.isLeaf) {
            searchHelper(node.children.get(node.numKeys), studentId, classId, result);
        }
    }

    @Override
    public void update(AttendanceRecord attendanceRecord) {
        if (!updateHelper(root, attendanceRecord)) {
            System.out.println("Record not found for update.");
        }
    }

    private boolean updateHelper(Node node, AttendanceRecord updatedRecord) {
        for (int i = 0; i < node.numKeys; i++) {
            AttendanceRecord current = node.keys.get(i);

            if (current.getStudentId() == updatedRecord.getStudentId() &&
                    current.getClazzId() == updatedRecord.getClazzId()) {
                node.keys.set(i, updatedRecord); // Update the record
                return true;
            }

            if (!node.isLeaf) {
                boolean found = updateHelper(node.children.get(i), updatedRecord);
                if (found) return true;
            }
        }

        if (!node.isLeaf) {
            return updateHelper(node.children.get(node.numKeys), updatedRecord);
        }
        return false;
    }

    @Override
    public void delete(Integer studentId, Integer classId) {
        if (studentId == null && classId == null) return;

        deleteHelper(root, studentId, classId);

        if (root.numKeys == 0 && !root.isLeaf) {
            root = root.children.get(0);
        }
    }

    private void deleteHelper(Node node, Integer studentId, Integer classId) {
        if (node == null) return;

        for (int i = 0; i < node.numKeys; i++) {
            AttendanceRecord record = node.keys.get(i);
            boolean matches = (studentId == null || record.getStudentId() == studentId)
                    && (classId == null || record.getClazzId() == classId);

            if (matches) {
                node.keys.remove(i);
                node.numKeys--;
                return;
            }

            if (!node.isLeaf) {
                deleteHelper(node.children.get(i), studentId, classId);
            }
        }

        if (!node.isLeaf) {
            deleteHelper(node.children.get(node.numKeys), studentId, classId);
        }
    }

    private void insertNonFull(Node node, AttendanceRecord record) {
        int i = node.numKeys - 1;

        if (node.isLeaf) {
            node.keys.add(null); // Placeholder
            while (i >= 0 && record.getStudentId() < node.keys.get(i).getStudentId()) {
                node.keys.set(i + 1, node.keys.get(i));
                i--;
            }
            node.keys.set(i + 1, record);
            node.numKeys++;
        } else {
            while (i >= 0 && record.getStudentId() < node.keys.get(i).getStudentId()) {
                i--;
            }
            i++;
            if (node.children.get(i).numKeys == 2 * T - 1) {
                splitChild(node, i, node.children.get(i));
                if (record.getStudentId() > node.keys.get(i).getStudentId()) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), record);
        }
    }

    private void splitChild(Node parent, int i, Node child) {
        Node newChild = new Node(child.isLeaf);
        newChild.numKeys = T - 1;

        for (int j = 0; j < T - 1; j++) {
            newChild.keys.add(child.keys.get(j + T));
        }

        if (!child.isLeaf) {
            for (int j = 0; j < T; j++) {
                newChild.children.add(child.children.get(j + T));
            }
        }

        parent.children.add(i + 1, newChild);
        parent.keys.add(i, child.keys.get(T - 1));
        parent.numKeys++;

        child.keys.subList(T - 1, child.keys.size()).clear();
        if (!child.isLeaf) {
            child.children.subList(T, child.children.size()).clear();
        }
        child.numKeys = T - 1;
    }
}
