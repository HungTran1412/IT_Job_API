package backend.main.utils;

import backend.main.enums.ErrorCode;
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
public class CloudinaryImageUpload {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        System.out.println("===== [UPLOAD IMAGE TO CLOUDINARY] =====");

        if (file == null) {
            System.out.println(">>>File is null!");
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File content type: " + file.getContentType());
        System.out.println("File size: " + file.getSize() + " bytes");

        try {
            // Log trước khi upload
            System.out.println("Uploading to Cloudinary...");
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "avatars",
                            "resource_type", "auto"
                    )
            );

            // Log kết quả trả về
            System.out.println(">>>Upload successful!");
            System.out.println("Cloudinary response: " + uploadResult);

            // Lấy URL ảnh
            String imageUrl = uploadResult.get("secure_url").toString();
            System.out.println("Image URL: " + imageUrl);

            return imageUrl;

        } catch (IOException e) {
            System.out.println(">>>IOException when uploading image!");
            e.printStackTrace();
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        } catch (Exception e) {
            System.out.println(">>>Unexpected error during upload!");
            e.printStackTrace();
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }
}
