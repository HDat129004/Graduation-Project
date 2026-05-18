package com.doantn.example;

/**
 * Ví dụ 3: Xử Lý Điểm Học Sinh (GradeCalculator)
 *
 * Mục đích kiểm thử:
 * - Chuỗi if/else if dài (5 nhánh) để phân loại điểm A/B/C/D/F.
 * - Ném ArithmeticException khi danh sách rỗng (tính trung bình).
 * - Phù hợp để kiểm tra kỹ thuật phân vùng tương đương
 *   (Equivalence Partitioning) với các dải điểm [0,60), [60,70), [70,80), [80,90), [90,100].
 */
public class GradeCalculator {

    /**
     * Chuyển đổi điểm số (0–100) sang xếp loại chữ.
     * Ném IllegalArgumentException nếu điểm nằm ngoài phạm vi [0, 100].
     */
    public String letterGrade(int score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Điểm phải nằm trong khoảng 0 đến 100");
        }
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    /**
     * Kiểm tra học sinh có qua môn không (điểm >= 50).
     *
     * @throws IllegalArgumentException nếu điểm âm.
     */
    public boolean isPassed(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Điểm không được âm");
        }
        return score >= 50;
    }

    /**
     * Tính điểm trung bình từ hai điểm thành phần.
     * Điểm cuối kỳ chiếm 60%, điểm giữa kỳ chiếm 40%.
     */
    public double weightedAverage(double midterm, double finalExam) {
        return midterm * 0.4 + finalExam * 0.6;
    }

    /**
     * Tính GPA tương ứng (thang 4.0) từ điểm hệ 10.
     */
    public double toGPA(double score) {
        if (score >= 9.0) {
            return 4.0;
        } else if (score >= 8.0) {
            return 3.5;
        } else if (score >= 7.0) {
            return 3.0;
        } else if (score >= 6.0) {
            return 2.0;
        } else if (score >= 5.0) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
