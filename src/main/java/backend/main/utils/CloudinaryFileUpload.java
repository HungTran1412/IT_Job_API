package backend.main.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import backend.main.enums.Code;
import backend.main.exception.AppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CloudinaryFileUpload {
    @Autowired
    private final Cloudinary cloudinary;

    public CloudinaryFileUpload(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    //Hàm chung xu lý upload
    private String upload(MultipartFile file, String folder, String resourceType) {
        System.out.println("===== [UPLOAD FILE TO CLOUDINARY] =====");

        if (file == null || file.isEmpty()) {
            System.out.println(">>> File is null or empty!");
            // Tùy chọn: Bạn có thể thêm kiểm tra loại tệp tin tại đây nếu muốn chặn sớm.
            throw new AppException(Code.FILE_UPLOAD_FAILED);
        }

        // *** Đảm bảo resourceType là "image" nếu bạn chỉ muốn upload ảnh ***
        // Nếu bạn muốn chắc chắn 100%, bạn có thể hardcode lại như sau:
        // String imageResourceType = "image";

        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File content type: " + file.getContentType());
        System.out.println("Folder: " + folder);
        System.out.println("Resource type: " + resourceType); // Sẽ là "image"

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            // Cloudinary sẽ tự động nhận dạng định dạng ảnh
                            "resource_type", resourceType // Giả định tham số này là "image"
                            // **ĐÃ BỎ:** "format", "pdf"
                    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();
            System.out.println(">>> Upload successful!");
            System.out.println("File URL: " + fileUrl);

            return fileUrl;

        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(Code.FILE_UPLOAD_FAILED);
        }
    }

    public String uploadImage(MultipartFile file) {
        return upload(file, "avatars", "image");
    }

    public String uploadCv(MultipartFile file) {
        System.out.println("===== [UPLOAD CV TO CLOUDINARY] =====");
        try {
            Map uploadResult = cloudinary.uploader().uploadLarge(
                    file.getInputStream(),
                    ObjectUtils.asMap(
                            "folder", "cv",
                            "resource_type", "auto",
                            "use_filename", true,
                            "unique_filename", true,
                            "overwrite", true
                    )
            );

            System.out.println(">>> Upload successful!");
            System.out.println("Upload result: " + uploadResult);

            String secureUrl = uploadResult.get("secure_url").toString();
            System.out.println("PDF URL: " + secureUrl);
            return secureUrl;

        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(Code.FILE_UPLOAD_FAILED);
        }
    }
    

}
