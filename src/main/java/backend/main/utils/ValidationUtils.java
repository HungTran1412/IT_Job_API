package backend.main.utils;

import backend.main.enums.Code;
import backend.main.exception.AppException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValidationUtils {
    static String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    // Regex Password: Ít nhất 8 ký tự, 1 chữ hoa, 1 số, 1 ký tự đặc biệt
    static String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!.]).{8,}$";

    public static void validateEmail(String email) {
        if(email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new AppException(Code.EMAIL_INVALID_FORMAT);
        }
    }

    public static void validatePassword(String password) {
        if(password == null || !Pattern.matches(PASSWORD_REGEX, password)) {
            throw new AppException(Code.PASSWORD_INVALID_FORMAT);
        }
    }
}
