package ru.skillbox.diplom.alpha.microservice.post.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * PostTagRq
 *
 * @author Olga Samoylova
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostTagRq {
    //@JsonProperty(value = "tag")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> tag;
}

