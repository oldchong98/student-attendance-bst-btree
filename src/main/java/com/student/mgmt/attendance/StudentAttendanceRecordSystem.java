package com.student.mgmt.attendance;

import com.student.mgmt.attendance.model.AttendanceRecord;
import com.student.mgmt.attendance.service.BSTService;
import com.student.mgmt.attendance.service.BTreeService;
import com.student.mgmt.attendance.service.CsvLoader;
import com.student.mgmt.attendance.service.DataStructureService;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class StudentAttendanceRecordSystem {
    Frame mainFrame;
    Panel rolePanel, treePanel, studentPanel, studentLoginPanel, teacherPanel;
    CardLayout cardLayout;

    //teacher
    TextField studentIdField, classIdField, attendTimeField, statusField, searchStudentIdField, searchClassIdField;
    Button addButton, searchButton, nextButton, prevButton;
    Panel tablePanel;
    ScrollPane scrollPane;
    private int currentPage = 0;
    private String studentId;

    static String role;
    static String dataStructure;

    // Dummy Data Storage
    ArrayList<String> records = new ArrayList<>();

    private static DataStructureService dataStructureService;

    public StudentAttendanceRecordSystem() {
        mainFrame = new Frame("Student Attendance Record System");
        cardLayout = new CardLayout();
        mainFrame.setLayout(cardLayout);

        initializeTreePanel();
        initializeRolePanel();

        mainFrame.setSize(500, 400);
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    // Tree Selection Panel
    private void initializeTreePanel() {
        treePanel = new Panel();
        treePanel.setLayout(new GridLayout(3, 1));

        Label label = new Label("Select Tree Type", Label.CENTER);
        Button bstButton = new Button("BST");
        Button btreeButton = new Button("B-Tree");

        bstButton.addActionListener(e -> {
            dataStructure = "BST";
            cardLayout.show(mainFrame, "RoleSelection");

        });
        btreeButton.addActionListener(e -> {
            dataStructure = "B-Tree";
            cardLayout.show(mainFrame, "RoleSelection");
        });

        treePanel.add(label);
        treePanel.add(bstButton);
        treePanel.add(btreeButton);

        mainFrame.add("TreeSelection", treePanel);
    }

    // Role Selection Panel
    private void initializeRolePanel() {
        rolePanel = new Panel();
        rolePanel.setLayout(new GridLayout(3, 1));

        Label label = new Label("Select Your Role", Label.CENTER);
        Button studentButton = new Button("Student");
        Button teacherButton = new Button("Teacher");

        studentButton.addActionListener(e -> {
            role = "STUDENT";
            initializeStudentLoginPanel();
            cardLayout.show(mainFrame, "StudentLoginPanel");
        });
        teacherButton.addActionListener(e -> {
            role = "TEACHER";
            initializeTeachPanel();
            mainFrame.setSize(1150, 700);
            cardLayout.show(mainFrame, "TeacherPanel");

        });

        rolePanel.add(label);
        rolePanel.add(studentButton);
        rolePanel.add(teacherButton);

        mainFrame.add("RoleSelection", rolePanel);
    }

    private void initializeStudentLoginPanel() {
        studentLoginPanel = new Panel();
        studentLoginPanel.setLayout(new GridLayout(3, 1));

        Label label = new Label("Enter your student ID", Label.CENTER);

        Button loginButton = new Button("Login");
        TextField idField = new TextField(8);
        studentLoginPanel.add(idField);


        loginButton.addActionListener(e -> {
            studentId = idField.getText();
            initializeStudentPanel();
            mainFrame.setSize(1150, 700);
            cardLayout.show(mainFrame, "StudentPanel");

        });

        studentLoginPanel.add(label);
        studentLoginPanel.add(idField);
        studentLoginPanel.add(loginButton);

        mainFrame.add("StudentLoginPanel", studentLoginPanel);
    }

    // Student Panel
    private void initializeStudentPanel() {
        loadRecords();
        studentPanel = new Panel();
        studentPanel.setLayout(new BorderLayout());

        // Top Panel for input
        Panel topPanel = new Panel(new FlowLayout());

        // Add Record Panel
        Panel addPanel = new Panel(new FlowLayout());
        addPanel.add(new Label("Student ID:"));
        studentIdField = new TextField(8);
        studentIdField.disable();
        studentIdField.setText(studentId);
        addPanel.add(studentIdField);

        addPanel.add(new Label("Class ID:"));
        classIdField = new TextField(8);
        addPanel.add(classIdField);

        addPanel.add(new Label("Attend Time:"));
        attendTimeField = new TextField(8);
        addPanel.add(attendTimeField);

        addPanel.add(new Label("Status:"));
        statusField = new TextField(8);
        addPanel.add(statusField);

        addButton = new Button("Add Record");
        addPanel.add(addButton);

        // Search Panel
        Panel searchPanel = new Panel(new FlowLayout());
        searchPanel.add(new Label("Search by Student ID:"));
        searchStudentIdField = new TextField(8);
        searchStudentIdField.disable();
        searchStudentIdField.setText(studentId);
        searchPanel.add(searchStudentIdField);

        searchPanel.add(new Label("or Class ID:"));
        searchClassIdField = new TextField(8);
        searchPanel.add(searchClassIdField);

        searchButton = new Button("Search");
        searchPanel.add(searchButton);

        topPanel.setLayout(new GridLayout(3, 1));
        topPanel.add(searchPanel);
        topPanel.add(addPanel);
        studentPanel.add(topPanel, BorderLayout.NORTH);

        // Table Panel for displaying records
        tablePanel = new Panel();
        tablePanel.setLayout(new GridLayout(1, 6)); // 6 columns: Row#, studentId, classId, attendTime, status, Actions
        scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(tablePanel);
        studentPanel.add(scrollPane, BorderLayout.CENTER);

        // Pagination Panel
        Panel paginationPanel = new Panel(new FlowLayout());
        prevButton = new Button("Previous");
        nextButton = new Button("Next");
        prevButton.setEnabled(false); // Disabled initially
        nextButton.setEnabled(false); // Disabled initially
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        studentPanel.add(paginationPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addRecord());
        searchButton.addActionListener(e -> searchRecords());
        prevButton.addActionListener(e -> changePage(-1, dataStructureService.search(Integer.valueOf(studentId), null)));
        nextButton.addActionListener(e -> changePage(1, dataStructureService.search(Integer.valueOf(studentId), null)));
        mainFrame.add("StudentPanel", studentPanel);

        updateTable(dataStructureService.search(Integer.valueOf(studentId), null));
    }


    private void loadRecords() {
        if ("BST".equals(dataStructure)) {
            dataStructureService = new BSTService();
        } else if ("B-Tree".equals(dataStructure)) {
            dataStructureService = new BTreeService();
        }

        List<AttendanceRecord> attendanceRecordList = CsvLoader.getAttendanceList();
        for (AttendanceRecord attendanceRecord : attendanceRecordList) {
            dataStructureService.add(attendanceRecord);
        }
    }

    public void initializeTeachPanel() {
        loadRecords();
        teacherPanel = new Panel();
        teacherPanel.setLayout(new BorderLayout());

        // Top Panel for input
        Panel topPanel = new Panel(new FlowLayout());

        // Add Record Panel
        Panel addPanel = new Panel(new FlowLayout());
        addPanel.add(new Label("Student ID:"));
        studentIdField = new TextField(8);
        addPanel.add(studentIdField);

        addPanel.add(new Label("Class ID:"));
        classIdField = new TextField(8);
        addPanel.add(classIdField);

        addPanel.add(new Label("Attend Time:"));
        attendTimeField = new TextField(8);
        addPanel.add(attendTimeField);

        addPanel.add(new Label("Status:"));
        statusField = new TextField(8);
        addPanel.add(statusField);

        addButton = new Button("Add Record");
        addPanel.add(addButton);

        // Search Panel
        Panel searchPanel = new Panel(new FlowLayout());
        searchPanel.add(new Label("Search by Student ID:"));
        searchStudentIdField = new TextField(8);
        searchPanel.add(searchStudentIdField);

        searchPanel.add(new Label("or Class ID:"));
        searchClassIdField = new TextField(8);
        searchPanel.add(searchClassIdField);

        searchButton = new Button("Search");
        searchPanel.add(searchButton);

        topPanel.setLayout(new GridLayout(3, 1));
        topPanel.add(searchPanel);
        topPanel.add(addPanel);
        teacherPanel.add(topPanel, BorderLayout.NORTH);

        // Table Panel for displaying records
        tablePanel = new Panel();
        tablePanel.setLayout(new GridLayout(1, 6)); // 6 columns: Row#, studentId, classId, attendTime, status, Actions
        scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(tablePanel);
        teacherPanel.add(scrollPane, BorderLayout.CENTER);

        // Pagination Panel
        Panel paginationPanel = new Panel(new FlowLayout());
        prevButton = new Button("Previous");
        nextButton = new Button("Next");
        prevButton.setEnabled(false); // Disabled initially
        nextButton.setEnabled(false); // Disabled initially
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        teacherPanel.add(paginationPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addRecord());
        searchButton.addActionListener(e -> searchRecords());
        prevButton.addActionListener(e -> changePage(-1, dataStructureService.getAll()));
        nextButton.addActionListener(e -> changePage(1, dataStructureService.getAll()));
        mainFrame.add("TeacherPanel", teacherPanel);

        updateTable(dataStructureService.getAll());
    }

    public static void main(String[] args) {
        new StudentAttendanceRecordSystem();
    }

    private void addRecord() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText().trim());
            int classId = Integer.parseInt(classIdField.getText().trim());
            String attendTime = attendTimeField.getText().trim();
            String status = statusField.getText().trim();

            dataStructureService.add(new AttendanceRecord(studentId, classId, LocalDateTime.parse(attendTime), status));
            if (this.studentId == null) {
                updateTable(dataStructureService.getAll());
            } else {
                updateTable(dataStructureService.search(Integer.valueOf(this.studentId), null));
            }

        } catch (NumberFormatException ex) {
            System.out.println("Invalid input format.");
        }
    }

    private void searchRecords() {
        Integer studentId = null;
        if (!searchStudentIdField.getText().trim().isEmpty()) {
            studentId = Integer.parseInt(searchStudentIdField.getText().trim());
        }
        Integer classId = searchClassIdField.getText().trim().isEmpty() ? null : Integer.parseInt(searchClassIdField.getText().trim());

        List<AttendanceRecord> result = dataStructureService.search(studentId, classId);
        updateTable(result);
    }

    private void addTableHeaders() {
        tablePanel.removeAll();
        tablePanel.add(new Label("Row#", Label.CENTER));
        tablePanel.add(new Label("Student ID", Label.CENTER));
        tablePanel.add(new Label("Class ID", Label.CENTER));
        tablePanel.add(new Label("Attend Time", Label.CENTER));
        tablePanel.add(new Label("Status", Label.CENTER));
        if (role.equals("TEACHER")) {
            tablePanel.add(new Label("Actions", Label.CENTER));
        }
    }

    private void updateTable(List<AttendanceRecord> records) {
        tablePanel.removeAll(); // Clear the table
        addTableHeaders();

        int pageSize = 50;
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, records.size());

        for (int i = start; i < end; i++) {
            AttendanceRecord record = records.get(i);
            tablePanel.add(new Label(String.valueOf(i + 1), Label.CENTER)); // Row number
            tablePanel.add(new Label(String.valueOf(record.getStudentId()), Label.CENTER)); // Student ID
            tablePanel.add(new Label(String.valueOf(record.getClazzId()), Label.CENTER)); // Class ID
            tablePanel.add(new Label(record.getAttendTime().toString(), Label.CENTER)); // Attendance Time


            if (role.equals("TEACHER")) {
                // Editable Status Field
                TextField statusUpdate = new TextField(record.getStatus(), 10);
                statusUpdate.setEditable(true);
                tablePanel.add(statusUpdate);

                Panel actionPanel = new Panel(new FlowLayout());

                // Update Button
                Button updateButton = new Button("Update");
                updateButton.addActionListener(e -> {
                    String updatedStatus = statusUpdate.getText();
                    record.setStatus(updatedStatus);
                    dataStructureService.update(record);
                    updateTable(dataStructureService.getAll());
                });
                actionPanel.add(updateButton);

                // Delete
                Button deleteButton = new Button("Delete");
                deleteButton.addActionListener(e -> {
                    dataStructureService.delete(record.getStudentId(), record.getClazzId());
                    updateTable(dataStructureService.getAll());
                });
                actionPanel.add(deleteButton);
                tablePanel.add(actionPanel);

            } else {
                tablePanel.add(new Label(record.getStatus(), Label.CENTER));
            }
        }

        // Update navigation buttons
        prevButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(end < records.size());

        tablePanel.setLayout(new GridLayout((end - start) + 1, 5)); // Adjust rows dynamically
        tablePanel.validate();
        scrollPane.validate();
    }

    private void changePage(int delta, List<AttendanceRecord> records) {
        currentPage += delta;
        updateTable(records);
    }

}
