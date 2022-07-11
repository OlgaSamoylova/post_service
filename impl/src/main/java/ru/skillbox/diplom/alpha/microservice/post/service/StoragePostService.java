package ru.skillbox.diplom.alpha.microservice.post.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.alpha.library.core.dto.FileType;
import ru.skillbox.diplom.alpha.library.core.security.utils.SecurityUtils;
import ru.skillbox.diplom.alpha.microservice.post.dto.ImageDto;
import ru.skillbox.diplom.alpha.microservice.post.model.Post;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostRepository;
import ru.skillbox.diplom.alpha.microservice.post.response.ImageRs;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;


/**
 * StoragePostService
 *
 * @author Olga Samoylova
 */


@Setter
@Getter
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StoragePostService {
    private final Cloudinary cloudinary;
    private final PostRepository postRepository;



    private static final String folderName = "diploma_alpha/";

    public ResponseEntity<ImageRs> uploadFile(FileType type, MultipartFile multipartFile) {
        try {
            log.info("Отправлен запрос на загрузку файла " + multipartFile.getOriginalFilename() + " в хранилище"
                    + ", тип файла: " + type.toString());
            ImageRs response = new ImageRs();
            String fileName = Instant.now().getEpochSecond() + "-image-id";
            File file = convertToFile(multipartFile);
            String publicId = folderName + fileName;
            String url = uploadToCloudinary(file, publicId);
            deleteFileAfterUpload(file);
            response.setTimestamp(Instant.now().getEpochSecond())
                    .setData(new ImageDto(url, fileName));
            log.info("Запрос успешно завершен. Фото загружено в хранилище. ");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.warn("Невозможно загрузить файл: " + ex);
            throw new RuntimeException(ex);
        }
    }

    private File convertToFile(MultipartFile image) throws IOException {
        File file = new File(Objects.requireNonNull(image.getOriginalFilename()));
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(image.getBytes());
        stream.close();
        return file;
    }

    private String uploadToCloudinary(File image, String publicId) throws IOException {
        Map cloudinaryUrl = cloudinary.uploader()
                .upload(image, ObjectUtils.asMap("public_id", publicId, "transformation",
                        new Transformation<>().width(500).height(500).crop("fit").fetchFormat("jpg")));
        return (String) cloudinaryUrl.get("url");
    }

    private void deleteFileAfterUpload(File image) {
        boolean isDeleted = image.delete();
        if (isDeleted) {
            log.info("Файл успешно удален из файловой системы приложения");
        } else {
            log.warn("Файл не существует");
        }
    }

    public void deleteFile(String url) {
        if (url == null) {
            return;
        }
        String publicId = folderName + url.substring(url.lastIndexOf('/') + 1).split("\\.", 3)[0];
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
        } catch (IOException ex) {
            log.warn("Невозможно удалить файл: " + ex);
            throw new RuntimeException(ex);
        }
    }
}
