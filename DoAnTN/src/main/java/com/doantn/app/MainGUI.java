package com.doantn.app;

import com.doantn.generator.TestGenerator;

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
    private JProgressBar progressBar;
    private JComboBox<String> cbxTests;
    private JLabel lblStatus;

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

        // 2. Khối Cấu hình Phương pháp
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

        // Log Panel
        JPanel pnlLog = createWindowsCard("NHẬT KÝ THỰC THI & KẾT QUẢ");
        pnlLog.setLayout(new BorderLayout());

        txtLog = new JTextArea();
        txtLog.setFont(FONT_CODE);
        txtLog.setBackground(COLOR_LOG_BG);
        txtLog.setForeground(new Color(204, 204, 204));
        txtLog.setCaretColor(Color.WHITE);
        txtLog.setEditable(false);
        txtLog.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(txtLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        pnlLog.add(scrollPane, BorderLayout.CENTER);

        // Extra Buttons Panel
        JPanel pnlExtra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlExtra.setBackground(COLOR_CARD_BG);

        JButton btnJacoco = new JButton("Xem báo cáo JaCoCo");
        btnJacoco.setFont(FONT_NORMAL);
        btnJacoco.addActionListener(e -> viewJacocoReport());

        pnlExtra.add(btnJacoco);
        pnlLog.add(pnlExtra, BorderLayout.SOUTH);

        rightPanel.add(topActionPanel, BorderLayout.NORTH);
        rightPanel.add(pnlLog, BorderLayout.CENTER);

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
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void generateTests() {
        final String sourcePath = txtFile.getText().trim();
        final String outputDir = "src/test/java";
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
        String selectedTestPath = (String) cbxTests.getSelectedItem();
        if (selectedTestPath == null || selectedTestPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có tập tin kiểm thử nào được chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File testFile = new File(selectedTestPath);
        File projectDir = findMavenProjectRoot(testFile);
        if (projectDir == null) {
            projectDir = findMavenProjectRoot(getApplicationDirectory());
        }
        if (projectDir == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy pom.xml trong thư mục dự án. Vui lòng chạy ứng dụng từ thư mục chứa pom.xml.", "Lỗi cấu hình", JOptionPane.ERROR_MESSAGE);
            setUIState(true, "Lỗi chạy kiểm thử");
            return;
        }

        String className = buildTestClassName(testFile, projectDir);
        final File projectDirFinal = projectDir;

        setUIState(false, "Đang chạy kiểm thử...");
        System.out.println("\n[THÔNG TIN] Bắt đầu chạy kiểm thử cho lớp: " + className);

        new Thread(() -> {
            try {
                String mvnCommand = getMavenCommand();
                ProcessBuilder pb = new ProcessBuilder(mvnCommand, "test", "-Dtest=" + className);
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
        File jacocoReport = new File("target/site/jacoco/index.html");
        if (jacocoReport.exists() && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(jacocoReport.toURI());
                System.out.println("[THÔNG TIN] Đã mở báo cáo JaCoCo trong trình duyệt.");
            } catch (IOException e) {
                System.err.println("[LỖI] Không thể mở báo cáo JaCoCo: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Không thể mở báo cáo JaCoCo.\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.err.println("[CẢNH BÁO] Không tìm thấy báo cáo JaCoCo tại: " + jacocoReport.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Không tìm thấy báo cáo JaCoCo.\nHãy chắc chắn rằng bạn đã chạy kiểm thử trước đó.", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
            testDir = new File(projectRoot, "src/test/java/com/doantn");
        }
        if (testDir == null || !testDir.exists() || !testDir.isDirectory()) {
            testDir = new File("src/test/java/com/doantn");
        }

        List<File> testFiles = findTestFiles(testDir);
        for (File file : testFiles) {
            cbxTests.addItem(file.getPath());
        }

        if (cbxTests.getItemCount() > 0) {
            cbxTests.setSelectedIndex(0);
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