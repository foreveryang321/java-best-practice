package top.ylonline.encrypt.util;

/**
 * 统一异常封装
 *
 * @author YL
 */
public class EncryptException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EncryptException(String msg) {
        super(msg);
    }

    public EncryptException(Exception cause) {
        super(cause);
    }

    public EncryptException(String msg, Exception cause) {
        super(msg, cause);
    }
}
