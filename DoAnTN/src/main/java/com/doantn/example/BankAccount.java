package com.doantn.example;

/**
 * Ví dụ 5: Quản Lý Tài Khoản Ngân Hàng (BankAccount)
 *
 * Mục đích kiểm thử:
 * - Nhiều hàm void có thay đổi trạng thái nội bộ (số dư).
 * - Hàm deposit() và withdraw() ném Exception khi vi phạm điều kiện kinh doanh.
 * - Hàm transfer() kết hợp hai thao tác, tạo kịch bản phức tạp hơn.
 * - Phù hợp để kiểm tra hành vi khi tham số bằng 0, dương, hoặc âm.
 */
public class BankAccount {

    private double balance;
    private final String accountNumber;

    public BankAccount(String accountNumber, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Số dư ban đầu không được âm");
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    /** Lấy số dư hiện tại của tài khoản. */
    public double getBalance() {
        return balance;
    }

    /** Lấy số tài khoản. */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Nộp tiền vào tài khoản.
     *
     * @throws IllegalArgumentException nếu số tiền nộp <= 0.
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền nộp phải lớn hơn 0");
        }
        balance += amount;
    }

    /**
     * Rút tiền từ tài khoản.
     *
     * @throws IllegalArgumentException nếu số tiền rút <= 0.
     * @throws IllegalStateException    nếu số dư không đủ.
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền rút phải lớn hơn 0");
        }
        if (amount > balance) {
            throw new IllegalStateException("Số dư không đủ để thực hiện giao dịch");
        }
        balance -= amount;
    }

    /**
     * Kiểm tra tài khoản có đủ tiền để thực hiện giao dịch không.
     */
    public boolean hasSufficientFunds(double amount) {
        return balance >= amount;
    }

    /**
     * Tính phí giao dịch dựa trên số tiền.
     * Dưới 1 triệu: phí 0.1%, từ 1 triệu trở lên: phí 0.05%.
     */
    public double calculateFee(double amount) {
        if (amount < 1_000_000) {
            return amount * 0.001;
        } else {
            return amount * 0.0005;
        }
    }
}
