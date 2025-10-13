package backend.main.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Code {
    UNCATEGORIZED_EXCEPTION(1836, "Uncategorized error"),
    EMAIL_EXISTED(1001,"Email Already Exists!"),
    WRONG_PASSWORD(1002,"Wrong Password!"),
    EMAIL_DOES_NOT_EXIST(1003,"Email Does Not Exist!"),
    EMPLOYER_NOT_FOUND(1004, "Employer not found!"),
    CANDIDATE_NOT_FOUND(1005, "Candidate not found!"),
    TOKEN_INVALID(1006, "Token invalid!"),
    ACCOUNT_UNENABLED(1007, "Account Unenabled!"),
    ID_EXISTED(1008, "ID existed!"),
    IMAGE_UPLOAD_FAILED(1009, "Image Upload Failed!"),
    LOGIN_SUCCEEDED(2001, "Login succeeded!"),
    UPDATE_INFO_SUCCEEDED(2002, "Update info succeeded!"),
    UPDATE_INFO_FAILED(2003, "Update info failed!"),
    CANNOT_UPDATE_ANOTHER_USER(2004, "Cannot update another user!"),
    CANNOT_GET_ANOTHER_INFO(2005, "Cannot get another info!"),
    GET_INFO_SUCCEEDED(2006, "Get info succeeded!"),
    TOKEN_EXPIRED(2007, "Token Expired. Please Register Again!"),
    ;

    int code;
    String message;
}
