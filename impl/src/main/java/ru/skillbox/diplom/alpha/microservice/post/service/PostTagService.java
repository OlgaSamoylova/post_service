package ru.skillbox.diplom.alpha.microservice.post.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostSearchDto;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostTagDto;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostTagSearchDto;
import ru.skillbox.diplom.alpha.microservice.post.mapper.PostMapper;
import ru.skillbox.diplom.alpha.microservice.post.mapper.PostTagMapper;
import ru.skillbox.diplom.alpha.microservice.post.model.Post;
import ru.skillbox.diplom.alpha.microservice.post.model.PostTag;
import ru.skillbox.diplom.alpha.microservice.post.model.PostTag_;
import ru.skillbox.diplom.alpha.microservice.post.model.Post_;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostRepository;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostTagRepository;
import ru.skillbox.diplom.alpha.microservice.post.request.PostTagRq;
import ru.skillbox.diplom.alpha.microservice.post.response.PostTagRs;
import ru.skillbox.diplom.alpha.microservice.post.response.TagRs;

import java.time.Instant;
import java.util.*;

import static ru.skillbox.diplom.alpha.library.core.repository.NewSpecification.*;

/**
 * PostTagService
 *
 * @author Olga Samoylova
 */

@Setter
@Getter
@Service
@Slf4j
@RequiredArgsConstructor
public class PostTagService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;

    PostTagMapper mapper = Mappers.getMapper(PostTagMapper.class);
    PostMapper postMapper = Mappers.getMapper(PostMapper.class);

    public PostTagRs getTags(String tag){ //получаем список постов, содержащих tag
        log.info("start getTag");
        PostTagSearchDto dto = new PostTagSearchDto().setTag(tag);
        List<PostTag> postTagList = postTagRepository.findAll(getSpecificationTag(dto));
        List<Post> posts = new ArrayList<>();
        if (postTagList.size() != 0){
            posts = postTagList.get(0).getPosts();
        }
        else {
            posts = Collections.EMPTY_LIST;
        }
        PostTagRs response = new PostTagRs();
        response.setTimestamp(Instant.now().getEpochSecond());
        response.setTotal(1);
        response.setDataPosts(postMapper.convertPostListToPostDtoList(posts));
        log.info("finish get tags");
        return response;
    }

    public PostTagRs putTag(int postId, PostTagRq tagRq){
        log.info("start to add tag");
        PostTagRs response = new PostTagRs();
        List<PostTagDto> data = new ArrayList<>();
        for(String tag : tagRq.getTag()) {
            PostTag postTag = findOrCreateTag(tag);
            findOrCreateRelationPost2Tag(postId, postTag);
            data.add(mapper.postTagToPostTagDto(postTag));
        }
        response.setData(data);
        response.setTimestamp(Instant.now().getEpochSecond());
        log.info("finish to add tag");
        return response;
    }

    public TagRs deleteTag(int postId, int tagId){
        log.info("start to delete tag");
        Post post = postRepository.findAll(getSpecificationPost(new PostSearchDto().setId(postId))).get(0);
        List<PostTag> data = new ArrayList<>(post.getTags());
        PostTag postTag = postTagRepository.findAll(getSpecificationTag( new PostTagSearchDto().setId(tagId))).get(0);
        Set<PostTag> tagSet = new HashSet<>();
        for (PostTag tag: data){
            if (tag.getId() != postTag.getId()){
                tagSet.add(tag);
            }
        }
        post.setTags(tagSet);
        postRepository.save(post);
        TagRs tagRs = new TagRs();
        tagRs.setMessage("ok");
        tagRs.setTimestamp(Instant.now().getEpochSecond());
        log.info("finish to delete tag");
        return tagRs;
    }

    private PostTag findOrCreateTag(String tag){
        log.info("start find or create PostTag");
        PostTag postTag;
        if (postTagRepository.findAll(getSpecificationTag(new PostTagSearchDto().setTag(tag))).isEmpty()){
            postTag = new PostTag();
            postTag.setTag(tag);
            postTagRepository.save(postTag);
        }
        else {
           postTag = postTagRepository.findAll(getSpecificationTag(new PostTagSearchDto().setTag(tag))).get(0);
        }
        log.info("finish find or create PostTag");
        return postTag;
    }

    public Set<Integer> getTagIds(List<PostTag> tags){
        Set<Integer> tagIds = new HashSet<>();
        for (PostTag tag: tags){
            tagIds.add(tag.getId());
        }
        return tagIds;
    }

    private void findOrCreateRelationPost2Tag(int postId, PostTag postTag){
        log.info("start find or create relation Post2Tag");
        Post post = postRepository.findById(postId).orElseThrow();
        Set<PostTag> tags = post.getTags();
        boolean flag = false;
        for (PostTag tag: tags){
           if (tag.getId().intValue() == postTag.getId()){
               flag = true;
               break;
           }
        }
        if (!flag){
            post.getTags().add(postTag);
            postRepository.save(post);
        }
        log.info("finish find or create relation Post2Tag");
    }

    private Specification<PostTag> getSpecificationTag(PostTagSearchDto dto) {
        log.info("Start - getSpecificationTag");
        return is(PostTag_.id, dto.getId(), true)
                .and(like(PostTag_.tag, dto.getTag(), true));
    }

    private Specification<Post> getSpecificationPost(PostSearchDto dto) {
        log.info("Start - getSpecificationPost");
        return is(Post_.id, dto.getId(), true);
    }
}
