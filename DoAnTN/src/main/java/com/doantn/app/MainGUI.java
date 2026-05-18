package com.doantn.app;

import com.doantn.analyzer.AnalyzerService;
import com.doantn.generator.TestGenerator;
import com.doantn.model.ClassModel;
import com.doantn.model.MethodModel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {

    // --- Bảng màu sáng sủa, chuẩn phong cách Windows ---
    private final Color COLOR_BG_MAIN = new Color(243, 243, 243); // Nền xám nhạt Windows
    private final Color COLOR_CARD_BG = Color.WHITE;
    private final Color COLOR_HEADER = new Color(0, 102, 204); // Xanh Windows Blue
    private final Color COLOR_GREEN_BTN = new Color(16, 124, 65); // Xanh lá phong cách MS Office
    private final Color COLOR_BLUE_BTN = new Color(0, 120, 215);
    private final Color COLOR_LOG_BG = new Color(30, 30, 30); // Nền tối cho Terminal

    // Sử dụng Font Segoe UI hỗ trợ tiếng Việt tốt nhất trên Windows
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 14);

    // Khai báo các Component tương tác
    private JTextField txtFile;
    private JRadioButton radAST;
    private JRadioButton radAI;
    private JRadioButton radBoth;
    private JButton btnGenerate;
    private JTextArea txtLog;
    private JTextArea txtPreviewAST; // Ô xem trước mã kiểm thử sinh bằng AST
    private JTextArea txtPreviewAI;  // Ô xem trước mã kiểm thử sinh bằng AI Gemini
    private JProgressBar progressBar;
    private JComboBox<TestFileItem> cbxTests;
    private JLabel lblStatus;

    // Combo chọn Class và Method (minh chứng parser AST)
    private JComboBox<String> cbClass;
    private JComboBox<String> cbMethod;
    private ClassModel analyzedModel;  // Kết quả phân tích từ AnalyzerService
    private final AnalyzerService analyzerService = new AnalyzerService();

    public MainGUI() {
        setTitle("DoAnTN - Hệ Thống Tự Động Sinh Unit Test (Phiên bản Windows)");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_MAIN);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBackground(COLOR_BG_MAIN);
        mainContent.setBorder(new EmptyBorder(15, 15, 15, 15));

        mainContent.add(buildLeftPanel(), BorderLayout.WEST);
        mainContent.add(buildRightPanel(), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        // Chuyển hướng System.out và System.err vào JTextArea hỗ trợ UTF-8
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(txtLog), true, StandardCharsets.UTF_8);
        System.setOut(printStream);
        System.setErr(printStream);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(COLOR_HEADER);
        JLabel title = new JLabel("DOANTN - HỆ THỐNG TỰ ĐỘNG SINH UNIT TEST");
        title.setForeground(Color.WHITE);
        title.setFont(FONT_TITLE);
        header.add(title);
        return header;
    }

    private JPanel buildLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(COLOR_BG_MAIN);
        leftPanel.setPreferredSize(new Dimension(360, 0));

        // 1. Khối Chọn File
        JPanel pnlFile = createWindowsCard("TẬP TIN NGUỒN");
        txtFile = new JTextField();
        txtFile.setEditable(false);
        txtFile.setFont(FONT_NORMAL);

        JButton btnBrowse = new JButton("Duyệt file...");
        btnBrowse.setFont(FONT_NORMAL);
        btnBrowse.addActionListener(e -> browseSourceFile());

        JPanel fileInputWrapper = new JPanel(new BorderLayout(5, 0));
        fileInputWrapper.setBackground(COLOR_CARD_BG);
        fileInputWrapper.add(txtFile, BorderLayout.CENTER);
        fileInputWrapper.add(btnBrowse, BorderLayout.EAST);

        fileInputWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pnlFile.add(fileInputWrapper);
        pnlFile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // 2. Khối Phân tích AST - Chọn Class & Method
        JPanel pnlAnalysis = createWindowsCard("KẺT QUẢ PHÂN TÍCH AST");

        JLabel lblClass = new JLabel("Class (JavaParser trích xuất):");
        lblClass.setFont(FONT_NORMAL);
        cbClass = new JComboBox<>();
        cbClass.setFont(FONT_NORMAL);
        cbClass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        cbClass.addActionListener(e -> onClassSelected());

        JLabel lblMethod = new JLabel("Method:");
        lblMethod.setFont(FONT_NORMAL);
        cbMethod = new JComboBox<>();
        cbMethod.setFont(FONT_NORMAL);
        cbMethod.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        pnlAnalysis.add(lblClass);
        pnlAnalysis.add(Box.createVerticalStrut(4));
        pnlAnalysis.add(cbClass);
        pnlAnalysis.add(Box.createVerticalStrut(10));
        pnlAnalysis.add(lblMethod);
        pnlAnalysis.add(Box.createVerticalStrut(4));
        pnlAnalysis.add(cbMethod);
        pnlAnalysis.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // 3. Khối Cấu hình Phương pháp
        JPanel pnlMethod = createWindowsCard("PHƯƠNG PHÁP SINH TEST");
        radAST = new JRadioButton("Thuật toán AST (Nhanh, Ngoại tuyến)");
        radAI = new JRadioButton("Trí tuệ nhân tạo Gemini (Trực tuyến)");
        radBoth = new JRadioButton("Kết hợp AST & AI (So sánh)");

        radAST.setFont(FONT_NORMAL);
        radAI.setFont(FONT_NORMAL);
        radBoth.setFont(FONT_NORMAL);
        radAST.setBackground(COLOR_CARD_BG);
        radAI.setBackground(COLOR_CARD_BG);
        radBoth.setBackground(COLOR_CARD_BG);
        radBoth.setSelected(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(radAST);
        bg.add(radAI);
        bg.add(radBoth);

        pnlMethod.add(radAST);
        pnlMethod.add(Box.createVerticalStrut(5));
        pnlMethod.add(radAI);
        pnlMethod.add(Box.createVerticalStrut(5));
        pnlMethod.add(radBoth);

        pnlMethod.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        leftPanel.add(pnlFile);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(pnlAnalysis);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(pnlMethod);
        leftPanel.add(Box.createVerticalGlue());

        return leftPanel;
    }

    private JPanel buildRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setBackground(COLOR_BG_MAIN);

        // Action Panel
        JPanel pnlAction = new JPanel(new BorderLayout(15, 0));
        pnlAction.setBackground(COLOR_BG_MAIN);

        btnGenerate = new JButton("TỰ ĐỘNG SINH UNIT TEST");
        btnGenerate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGenerate.setBackground(COLOR_GREEN_BTN);
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFocusPainted(false);
        btnGenerate.setPreferredSize(new Dimension(250, 45));

        btnGenerate.setContentAreaFilled(false);
        btnGenerate.setOpaque(true);
        btnGenerate.setBorder(new LineBorder(COLOR_GREEN_BTN.darker(), 2));

        btnGenerate.addActionListener(e -> generateTests());

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setFont(FONT_NORMAL);
        pnlAction.add(btnGenerate, BorderLayout.WEST);
        pnlAction.add(progressBar, BorderLayout.CENTER);

        // Test Execution Panel
        JPanel pnlTestExec = createWindowsCard("THỰC THI KIỂM THỬ");
        pnlTestExec.setLayout(new BorderLayout(10, 0));

        cbxTests = new JComboBox<>();
        cbxTests.setFont(FONT_NORMAL);
        cbxTests.addActionListener(e -> loadPreviewCode());

        JButton btnRunTest = new JButton("Chạy Test đã chọn");
        btnRunTest.setFont(FONT_NORMAL);
        btnRunTest.setBackground(COLOR_BLUE_BTN);
        btnRunTest.setForeground(Color.WHITE);

        btnRunTest.setContentAreaFilled(false);
        btnRunTest.setOpaque(true);
        btnRunTest.setBorder(new LineBorder(COLOR_BLUE_BTN.darker(), 1));

        btnRunTest.addActionListener(this::runSelectedTest);

        pnlTestExec.add(cbxTests, BorderLayout.CENTER);
        pnlTestExec.add(btnRunTest, BorderLayout.EAST);

        JPanel topActionPanel = new JPanel(new BorderLayout(0, 15));
        topActionPanel.setBackground(COLOR_BG_MAIN);
        topActionPanel.add(pnlAction, BorderLayout.NORTH);
        topActionPanel.add(pnlTestExec, BorderLayout.CENTER);

        // Khung 1: Nhật ký thực thi (Log)
        JPanel pnlLog = createWindowsCard("NHẬT KÝ THỰC THI & KẾT QUẢ MAVEN");
        pnlLog.setLayout(new BorderLayout());
        txtLog = new JTextArea();
        txtLog.setFont(FONT_CODE);
        txtLog.setBackground(COLOR_LOG_BG);
        txtLog.setForeground(new Color(204, 204, 204));
        txtLog.setCaretColor(Color.WHITE);
        txtLog.setEditable(false);
        txtLog.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        pnlLog.add(scrollLog, BorderLayout.CENTER);

        // Khung 2: Xem trước mã kiểm thử AST
        JPanel pnlPreviewAST = createWindowsCard("XEM TRƯỚC MÃ KIỂM THỬ (AST)");
        pnlPreviewAST.setLayout(new BorderLayout());
        txtPreviewAST = new JTextArea();
        txtPreviewAST.setFont(FONT_CODE);
        txtPreviewAST.setBackground(new Color(30, 30, 30)); // Nền đen IDE (VS Code #1e1e1e)
        txtPreviewAST.setForeground(new Color(212, 212, 212)); // Chữ xám sáng chuẩn mã nguồn
        txtPreviewAST.setCaretColor(Color.WHITE);
        txtPreviewAST.setEditable(false);
        txtPreviewAST.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPreviewAST = new JScrollPane(txtPreviewAST);
        scrollPreviewAST.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlPreviewAST.add(scrollPreviewAST, BorderLayout.CENTER);

        // Khung 3: Xem trước mã kiểm thử AI Gemini
        JPanel pnlPreviewAI = createWindowsCard("XEM TRƯỚC MÃ KIỂM THỬ (AI GEMINI)");
        pnlPreviewAI.setLayout(new BorderLayout());
        txtPreviewAI = new JTextArea();
        txtPreviewAI.setFont(FONT_CODE);
        txtPreviewAI.setBackground(new Color(30, 30, 30)); // Nền đen IDE (VS Code #1e1e1e)
        txtPreviewAI.setForeground(new Color(212, 212, 212)); // Chữ xám sáng chuẩn mã nguồn
        txtPreviewAI.setCaretColor(Color.WHITE);
        txtPreviewAI.setEditable(false);
        txtPreviewAI.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPreviewAI = new JScrollPane(txtPreviewAI);
        scrollPreviewAI.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlPreviewAI.add(scrollPreviewAI, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_HEADER);

        tabbedPane.addTab("Nhật ký thực thi & Kết quả", pnlLog);
        tabbedPane.addTab("Xem trước Mã kiểm thử (AST)", pnlPreviewAST);
        tabbedPane.addTab("Xem trước Mã kiểm thử (AI Gemini)", pnlPreviewAI);

        rightPanel.add(topActionPanel, BorderLayout.NORTH);
        rightPanel.add(tabbedPane, BorderLayout.CENTER);

        return rightPanel;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(COLOR_BG_MAIN);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));
        lblStatus = new JLabel("Trạng thái: Sẵn sàng");
        lblStatus.setFont(FONT_NORMAL);
        footer.add(lblStatus);
        return footer;
    }

    private JPanel createWindowsCard(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(10));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

    // --- LOGIC METHODS ---

    private void browseSourceFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn tập tin nguồn Java");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Admin\\Desktop\\Test Design\\Graduation-Project test\\DoAnTN\\src\\main\\java\\com\\doantn\\example"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            txtFile.setText(path);
            analyzeAndPopulate(path);
        }
    }

    /** Gọi AnalyzerService để phân tích file và điền vào cbClass + cbMethod */
    private void analyzeAndPopulate(String filePath) {
        cbClass.removeAllItems();
        cbMethod.removeAllItems();
        analyzedModel = null;

        new Thread(() -> {
            ClassModel model = analyzerService.analyze(filePath);
            SwingUtilities.invokeLater(() -> {
                if (model == null) {
                    cbClass.addItem("(Không phân tích được)");
                    return;
                }
                analyzedModel = model;
                cbClass.addItem(model.getClassName());
                cbClass.setSelectedIndex(0);
                populateMethods(model);
            });
        }).start();
    }

    /** Điền danh sách method vào cbMethod khi chọn class */
    private void onClassSelected() {
        cbMethod.removeAllItems();
        if (analyzedModel == null) return;
        populateMethods(analyzedModel);
    }

    private void populateMethods(ClassModel model) {
        cbMethod.removeAllItems();
        cbMethod.addItem("(Tất cả phương thức)");
        for (MethodModel m : model.getMethods()) {
            String params = m.getParameters().stream()
                    .map(p -> p.getParamType() + " " + p.getParamName())
                    .collect(java.util.stream.Collectors.joining(", "));
            cbMethod.addItem(m.getMethodName() + "(" + params + ")");
        }
    }

    private void generateTests() {
        final String sourcePath = txtFile.getText().trim();
        final String outputDir = "C:\\Users\\Admin\\Desktop\\Test Design\\Graduation-Project test\\DoAnTN\\src\\test\\java";
        String envKey = System.getenv("GEMINI_API_KEY");
        final String apiKey = (envKey == null) ? "" : envKey;

        if (sourcePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Tập tin nguồn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ((radAI.isSelected() || radBoth.isSelected()) && apiKey.isBlank()) {
            JOptionPane.showMessageDialog(this, "Bạn đã chọn phương pháp cần AI nhưng chưa cấu hình GEMINI_API_KEY trong biến môi trường.", "Lỗi cấu hình", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setUIState(false, "Đang sinh kiểm thử...");

        final TestGenerator.GenerationMode mode = radAST.isSelected() ? TestGenerator.GenerationMode.AST_ONLY :
                radAI.isSelected() ? TestGenerator.GenerationMode.AI_ONLY :
                        TestGenerator.GenerationMode.BOTH;

        new Thread(() -> {
            try {
                String modeName = radAST.isSelected() ? "AST" : radAI.isSelected() ? "AI Gemini" : "Kết hợp AST & AI";
                System.out.println("[THÔNG TIN] Bắt đầu quy trình với phương pháp: " + modeName);
                App.generateTests(sourcePath, outputDir, apiKey, mode);

                SwingUtilities.invokeLater(() -> {
                    System.out.println("\n[THÀNH CÔNG] Hoàn tất quá trình sinh Unit Test!");
                    setUIState(true, "Hoàn tất");
                    populateTestComboBox();
                    loadPreviewCode();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    System.err.println("\n[LỖI] " + e.getMessage());
                    setUIState(true, "Lỗi thực thi");
                });
            }
        }).start();
    }

    private void runSelectedTest(ActionEvent e) {
        TestFileItem selectedItem = (TestFileItem) cbxTests.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Không có tập tin kiểm thử nào được chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File testFile = selectedItem.getFile();
        File projectDir = findMavenProjectRoot(testFile);
        if (projectDir == null) {
            projectDir = findMavenProjectRoot(getApplicationDirectory());
        }
        if (projectDir == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy pom.xml trong thư mục dự án. Vui lòng chạy ứng dụng từ thư mục chứa pom.xml.", "Lỗi cấu hình", JOptionPane.ERROR_MESSAGE);
            setUIState(true, "Lỗi chạy kiểm thử");
            return;
        }

        String baseClassName = buildTestClassName(testFile, projectDir);
        String testParam = baseClassName;

        // Nếu đang chọn chế độ Kết hợp (Both), ta gom cả 2 class AST và AI để chạy chung trong 1 lệnh Maven
        if (radBoth.isSelected()) {
            if (baseClassName.endsWith("AITest")) {
                String astClassName = baseClassName.substring(0, baseClassName.length() - 6) + "ASTTest";
                testParam = astClassName + "," + baseClassName;
            } else if (baseClassName.endsWith("ASTTest")) {
                String aiClassName = baseClassName.substring(0, baseClassName.length() - 7) + "AITest";
                testParam = baseClassName + "," + aiClassName;
            }
        }

        final File projectDirFinal = projectDir;
        final String finalTestParam = testParam;

        setUIState(false, "Đang chạy kiểm thử...");
        System.out.println("\n[THÔNG TIN] Bắt đầu chạy kiểm thử cho các lớp: " + finalTestParam);

        new Thread(() -> {
            try {
                String mvnCommand = getMavenCommand();
                ProcessBuilder pb = new ProcessBuilder(mvnCommand, "test", "-Dtest=" + finalTestParam);
                pb.directory(projectDirFinal);
                pb.redirectErrorStream(true);
                Process process = pb.start();
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                int exitCode = process.waitFor();
                SwingUtilities.invokeLater(() -> {
                    if (exitCode == 0) {
                        System.out.println("[THÀNH CÔNG] Chạy kiểm thử hoàn tất.");
                        setUIState(true, "Chạy kiểm thử xong");
                        // Tạo luồng trễ 1 giây để đảm bảo hệ điều hành và Maven xả toàn bộ file index.html xuống ổ cứng
                        new Thread(() -> {
                            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                            SwingUtilities.invokeLater(this::viewJacocoReport);
                        }).start();
                    } else {
                        System.err.println("[LỖI] Kiểm thử thất bại. Exit code: " + exitCode);
                        setUIState(true, "Kiểm thử thất bại");
                    }
                });
            } catch (IOException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    System.err.println("[LỖI] " + ex.getMessage());
                    setUIState(true, "Lỗi chạy kiểm thử");
                });
            }
        }).start();
    }

    private void viewJacocoReport() {
        File projectRoot = findMavenProjectRoot(getApplicationDirectory());
        File jacocoReport = null;
        if (projectRoot != null) {
            jacocoReport = new File(projectRoot, "target/site/jacoco/index.html");
        }
        if (jacocoReport == null || !jacocoReport.exists()) {
            jacocoReport = new File("C:\\Users\\Admin\\Desktop\\Test Design\\Graduation-Project test\\DoAnTN\\target\\site\\jacoco\\index.html");
        }

        if (jacocoReport.exists() && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(jacocoReport.toURI());
                System.out.println("[THÔNG TIN] Đã mở báo cáo JaCoCo trong trình duyệt: " + jacocoReport.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("[LỖI] Không thể mở báo cáo JaCoCo: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Không thể mở báo cáo JaCoCo.\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.err.println("[CẢNH BÁO] Không tìm thấy báo cáo JaCoCo tại: " + jacocoReport.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Không tìm thấy báo cáo JaCoCo tại:\n" + jacocoReport.getAbsolutePath() + "\nHãy chắc chắn rằng bạn đã chạy kiểm thử trước đó.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setUIState(boolean enabled, String status) {
        btnGenerate.setEnabled(enabled);
        progressBar.setIndeterminate(!enabled);
        if (enabled) {
            progressBar.setValue(status.contains("Lỗi") || status.contains("thất bại") ? 0 : 100);
            progressBar.setString(status);
        } else {
            progressBar.setString(status);
        }
        lblStatus.setText("Trạng thái: " + status);
    }

    private List<File> findTestFiles(File dir) {
        List<File> files = new ArrayList<>();
        if (dir != null && dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    files.addAll(findTestFiles(f));
                } else if (f.getName().endsWith(".java")) {
                    files.add(f);
                }
            }
        }
        return files;
    }

    private void populateTestComboBox() {
        cbxTests.removeAllItems();
        File projectRoot = findMavenProjectRoot(getApplicationDirectory());
        File testDir = null;
        if (projectRoot != null) {
            testDir = new File(projectRoot, "src/test/java/com/doantn/example");
        }
        if (testDir == null || !testDir.exists() || !testDir.isDirectory()) {
            testDir = new File("C:\\Users\\Admin\\Desktop\\Test Design\\Graduation-Project test\\DoAnTN\\src\\test\\java\\com\\doantn\\example");
        }

        List<File> testFiles = findTestFiles(testDir);
        for (File file : testFiles) {
            cbxTests.addItem(new TestFileItem(file));
        }

        if (cbxTests.getItemCount() > 0) {
            cbxTests.setSelectedIndex(0);
        }
    }

    private void loadPreviewCode() {
        TestFileItem selectedItem = (TestFileItem) cbxTests.getSelectedItem();
        if (selectedItem == null) {
            txtPreviewAST.setText("// Không có mã kiểm thử nào được chọn để xem trước.");
            txtPreviewAI.setText("// Không có mã kiểm thử nào được chọn để xem trước.");
            return;
        }
        try {
            File currentFile = selectedItem.getFile();
            String pathStr = currentFile.getAbsolutePath();

            // Phân biệt và nạp file tương ứng
            if (pathStr.endsWith("AITest.java")) {
                // Tệp hiện tại là AI Test
                nạpNộiDungTệp(currentFile, txtPreviewAI);
                File astFile = new File(pathStr.replace("AITest.java", "ASTTest.java"));
                nạpNộiDungTệp(astFile, txtPreviewAST);
            } else if (pathStr.endsWith("ASTTest.java")) {
                // Tệp hiện tại là AST Test
                nạpNộiDungTệp(currentFile, txtPreviewAST);
                File aiFile = new File(pathStr.replace("ASTTest.java", "AITest.java"));
                nạpNộiDungTệp(aiFile, txtPreviewAI);
            } else {
                // Trường hợp khác
                nạpNộiDungTệp(currentFile, txtPreviewAST);
                txtPreviewAI.setText("// Không xác định được tệp AI tương ứng.");
            }
        } catch (Exception e) {
            txtPreviewAST.setText("// Lỗi xử lý: " + e.getMessage());
            txtPreviewAI.setText("// Lỗi xử lý: " + e.getMessage());
        }
    }

    private void nạpNộiDungTệp(File file, JTextArea textArea) {
        if (file.exists()) {
            try {
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                textArea.setText(content);
                textArea.setCaretPosition(0);
            } catch (IOException e) {
                textArea.setText("// Lỗi khi đọc tệp: " + e.getMessage());
            }
        } else {
            textArea.setText("// Tệp kiểm thử chưa được sinh hoặc không tồn tại: \n// " + file.getAbsolutePath());
        }
    }

    private File findMavenProjectRoot(File startDir) {
        File current = (startDir == null) ? null : startDir.getAbsoluteFile();
        while (current != null) {
            if (new File(current, "pom.xml").isFile()) {
                return current;
            }
            current = current.getParentFile();
        }
        return null;
    }

    private String getMavenCommand() {
        return System.getProperty("os.name").toLowerCase().contains("win") ? "mvn.cmd" : "mvn";
    }

    private File getApplicationDirectory() {
        try {
            File location = new File(MainGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return location.isDirectory() ? location : location.getParentFile();
        } catch (URISyntaxException e) {
            return new File(System.getProperty("user.dir"));
        }
    }

    private String buildTestClassName(File testFile, File projectDir) {
        String testRootMarker = projectDir.getAbsolutePath() + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator;
        String path = testFile.getAbsolutePath();
        if (path.startsWith(testRootMarker)) {
            path = path.substring(testRootMarker.length());
        }
        path = path.replace(File.separatorChar, '.');
        if (path.endsWith(".java")) {
            path = path.substring(0, path.length() - 5);
        }
        return path;
    }

    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;
        public TextAreaOutputStream(JTextArea textArea) { this.textArea = textArea; }
        @Override
        public void write(int b) {
            SwingUtilities.invokeLater(() -> {
                textArea.append(String.valueOf((char) b));
                textArea.setCaretPosition(textArea.getDocument().getLength());
            });
        }
        // Ghi đè phương thức write mảng byte để xử lý Unicode (tiếng Việt) từ System.out
        @Override
        public void write(byte[] b, int off, int len) {
            String s = new String(b, off, len, StandardCharsets.UTF_8);
            SwingUtilities.invokeLater(() -> {
                textArea.append(s);
                textArea.setCaretPosition(textArea.getDocument().getLength());
            });
        }
    }

    /** Lớp bọc File để hiển thị tên ngắn gọn trên JComboBox nhưng vẫn giữ đường dẫn tuyệt đối bên dưới */
    private static class TestFileItem {
        private final File file;

        public TestFileItem(File file) {
            this.file = file;
        }

        public File getFile() {
            return file;
        }

        @Override public String toString() {
            return file.getName(); // Chỉ hiển thị tên file ngắn gọn (ví dụ: CalculatorTest.java)
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}