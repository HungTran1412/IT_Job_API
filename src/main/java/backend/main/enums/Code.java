package backend.main.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Code {
    UNCATEGORIZED_EXCEPTION("error", "Uncategorized error"),
    EMAIL_EXISTED("error","Email Already Exists!"),
    WRONG_PASSWORD("error","Wrong Password!"),
    EMAIL_DOES_NOT_EXIST("error","Email Does Not Exist!"),
    EMPLOYER_NOT_FOUND("error", "Employer not found!"),
    CANDIDATE_NOT_FOUND("error", "Candidate not found!"),
    TOKEN_INVALID("error", "Token invalid!"),
    ACCOUNT_UNENABLED("error", "Account Unenabled!"),
    ID_EXISTED("error", "ID existed!"),
    FILE_UPLOAD_FAILED("error", "File Upload Failed!"),
    LOGIN_SUCCEEDED("success", "Login succeeded!"),
    UPDATE_INFO_SUCCEEDED("success", "Update info succeeded!"),
    UPDATE_INFO_FAILED("error", "Update info failed!"),
    CANNOT_UPDATE_ANOTHER_USER("error", "Cannot update another user!"),
    CANNOT_GET_ANOTHER_INFO("error", "Cannot get another info!"),
    GET_INFO_SUCCEEDED("success", "Get info succeeded!"),
    TOKEN_EXPIRED("error", "Token Expired. Please Register Again!"),
    ;

    String code;
    String message;
}
