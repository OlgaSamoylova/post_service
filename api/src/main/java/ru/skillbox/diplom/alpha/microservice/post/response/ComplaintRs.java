package ru.skillbox.diplom.alpha.microservice.post.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ComplaintRs
 *
 * @author Olga Samoylova
 */

@Data
@Schema(description = "Ответ на результат жалобы на публикацию")
public class ComplaintRs {
    String message;
}
