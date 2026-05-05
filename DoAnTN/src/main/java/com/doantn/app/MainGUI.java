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
import java.nio.charset.StandardCharsets;

public class MainGUI extends JFrame {

    // --- Bảng màu sáng sủa, chuẩn phong cách Windows ---
    private final Color COLOR_BG_MAIN = new Color(243, 243, 243); // Nền xám nhạt Windows
    private final Color COLOR_CARD_BG = Color.WHITE;
    private final Color COLOR_HEADER = new Color(0, 102, 204); // Xanh Windows Blue
    private final Color COLOR_GREEN_BTN = new Color(16, 124, 65); // Xanh lá phong cách MS Office
    private final Color COLOR_BLUE_BTN = new Color(0, 120, 215);
    private final Color COLOR_LOG_BG = new Color(30, 30, 30); // Nền tối cho Terminal

    // Sử dụng Font Segoe UI mặc định của Windows
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
        setTitle("DoAnTN - Hệ Thống Tự Động Sinh Unit Test (Windows Edition)");
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

        // Chuyển hướng System.out và System.err vào JTextArea
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
        JPanel pnlFile = createWindowsCard("SOURCE FILE");
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

        // Cố định chiều cao ô nhập liệu tránh kéo giãn
        fileInputWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pnlFile.add(fileInputWrapper);
        // Cố định chiều cao khối Card File
        pnlFile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // 2. Khối Cấu hình Phương pháp
        JPanel pnlMethod = createWindowsCard("PHƯƠNG PHÁP SINH TEST");
        radAST = new JRadioButton("Thuật toán AST (Nhanh, Offline)");
        radAI = new JRadioButton("Trí tuệ nhân tạo Gemini (Internet)");
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

        // Cố định chiều cao khối phương pháp
        pnlMethod.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        leftPanel.add(pnlFile);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(pnlMethod);

        // Đẩy toàn bộ các khối lên trên cùng, lấp đầy khoảng trống thừa ở dưới
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

        // Ép Windows cho phép tô màu nền Button
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
        JPanel pnlTestExec = createWindowsCard("THỰC THI TEST");
        pnlTestExec.setLayout(new BorderLayout(10, 0));

        cbxTests = new JComboBox<>();
        cbxTests.setFont(FONT_NORMAL);

        JButton btnRunTest = new JButton("Chạy Test Đã Chọn");
        btnRunTest.setFont(FONT_NORMAL);
        btnRunTest.setBackground(COLOR_BLUE_BTN);
        btnRunTest.setForeground(Color.WHITE);

        // Ép Windows cho phép tô màu nền Button
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
        JPanel pnlLog = createWindowsCard("EXECUTION LOG & OUTPUT");
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

        JButton btnJacoco = new JButton("Xem Báo cáo JaCoCo");
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

        // Ép căn lề trái cho Tiêu đề
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(10));

        // Ép căn lề trái cho cả Card
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

    // --- LOGIC METHODS ---

    private void browseSourceFile() {
        JFileChooser fileChooser = new JFileChooser();
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Source File.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ((radAI.isSelected() || radBoth.isSelected()) && apiKey.isBlank()) {
            JOptionPane.showMessageDialog(this, "Bạn đã chọn phương pháp cần AI nhưng chưa cấu hình GEMINI_API_KEY.", "Lỗi Cấu Hình", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setUIState(false, "Đang sinh test...");

        final TestGenerator.GenerationMode mode = radAST.isSelected() ? TestGenerator.GenerationMode.AST_ONLY :
                radAI.isSelected() ? TestGenerator.GenerationMode.AI_ONLY :
                        TestGenerator.GenerationMode.BOTH;

        new Thread(() -> {
            try {
                String modeName = radAST.isSelected() ? "AST" : radAI.isSelected() ? "AI Gemini" : "Kết hợp AST & AI";
                System.out.println("[INFO] Bắt đầu quy trình với phương pháp: " + modeName);
                App.generateTests(sourcePath, outputDir, apiKey, mode);

                SwingUtilities.invokeLater(() -> {
                    System.out.println("\n[SUCCESS] Hoàn tất quá trình sinh Unit Test!");
                    setUIState(true, "Hoàn tất");
                    String testFileName = new File(sourcePath).getName().replace(".java", "Test.java");
                    // Giả định package, cần điều chỉnh cho đúng thực tế
                    String generatedTestPath = new File(outputDir, "com/doantn/example/tests/" + testFileName).getPath();
                    cbxTests.addItem(generatedTestPath);
                    cbxTests.setSelectedIndex(cbxTests.getItemCount() - 1);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    System.err.println("\n[ERROR] " + e.getMessage());
                    setUIState(true, "Lỗi");
                });
            }
        }).start();
    }

    private void runSelectedTest(ActionEvent e) {
        String selectedTestPath = (String) cbxTests.getSelectedItem();
        if (selectedTestPath == null || selectedTestPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có file test nào được chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Chuyển đổi đường dẫn file thành tên class đầy đủ (com.example.MyTest)
        String className = selectedTestPath.replace(File.separator, ".")
                .replace("src.test.java.", "")
                .replace(".java", "");

        setUIState(false, "Đang chạy test...");
        System.out.println("\n[INFO] Bắt đầu chạy test cho class: " + className);

        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder("mvn.cmd", "test", "-Dtest=" + className);
                pb.redirectErrorStream(true);
                Process process = pb.start();
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                int exitCode = process.waitFor();
                SwingUtilities.invokeLater(() -> {
                    if (exitCode == 0) {
                        System.out.println("[SUCCESS] Chạy test hoàn tất.");
                        setUIState(true, "Chạy test xong");
                    } else {
                        System.err.println("[ERROR] Chạy test thất bại. Exit code: " + exitCode);
                        setUIState(true, "Test thất bại");
                    }
                });
            } catch (IOException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    System.err.println("[ERROR] " + ex.getMessage());
                    setUIState(true, "Lỗi chạy test");
                });
            }
        }).start();
    }

    private void viewJacocoReport() {
        File jacocoReport = new File("target/site/jacoco/index.html");
        if (jacocoReport.exists() && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(jacocoReport.toURI());
                System.out.println("[INFO] Đã mở báo cáo JaCoCo trong trình duyệt.");
            } catch (IOException e) {
                System.err.println("[ERROR] Không thể mở báo cáo JaCoCo: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Không thể mở báo cáo JaCoCo.\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.err.println("[WARNING] Không tìm thấy báo cáo JaCoCo tại: " + jacocoReport.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Không tìm thấy báo cáo JaCoCo.\nHãy chắc chắn rằng bạn đã chạy test trước.", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            // Fallback to default if Windows L&F is not available
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}