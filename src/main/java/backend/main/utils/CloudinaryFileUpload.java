package backend.main.utils;

import backend.main.enums.Code;
import backend.main.exception.AppException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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
            throw new AppException(Code.FILE_UPLOAD_FAILED);
        }

        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File content type: " + file.getContentType());
        System.out.println("Folder: " + folder);
        System.out.println("Resource type: " + resourceType);

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", resourceType,
                            "format", "pdf"
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
