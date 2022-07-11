package ru.skillbox.diplom.alpha.microservice.post.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.alpha.library.core.dto.FileType;
import ru.skillbox.diplom.alpha.microservice.post.response.ImageRs;

/**
 * StoragePostResource
 *
 * @author Olga Samoylova
 */

@RequestMapping("api/v1/storagePostPhoto")
public interface StoragePostResource {

    @PostMapping(value = "")
    ResponseEntity<ImageRs> uploadFile(@RequestParam FileType type ,
                                       @RequestBody MultipartFile file);
}