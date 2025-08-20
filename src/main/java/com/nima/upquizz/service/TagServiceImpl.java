package com.nima.upquizz.service;

import com.nima.upquizz.entity.Tag;
import com.nima.upquizz.entity.User;
import com.nima.upquizz.repository.QuizRepository;
import com.nima.upquizz.repository.TagRepository;
import com.nima.upquizz.request.CategoryRequest;
import com.nima.upquizz.request.TagRequest;
import com.nima.upquizz.response.CategoryResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.TagResponse;
import com.nima.upquizz.util.FindUserAuthentication;
import com.nima.upquizz.util.PageResponseUtil;
import com.nima.upquizz.util.UserRoleIndicator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements  TagService {

    private final FindUserAuthentication findUserAuthentication;
    private final TagRepository tagRepository;
    private final UserRoleIndicator userRoleIndicator;
    private final QuizRepository quizRepository;
    private final PageResponseUtil pageResponseUtil;

    public TagServiceImpl(FindUserAuthentication findUserAuthentication, TagRepository tagRepository, UserRoleIndicator userRoleIndicator, QuizRepository quizRepository, PageResponseUtil pageResponseUtil) {
        this.findUserAuthentication = findUserAuthentication;
        this.tagRepository = tagRepository;
        this.userRoleIndicator = userRoleIndicator;
        this.quizRepository = quizRepository;
        this.pageResponseUtil = pageResponseUtil;
    }

    @Transactional
    @Override
    public TagResponse addTag(TagRequest tagRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        boolean tagExists = tagRepository.findTagByName(tagRequest.name()).isPresent();
        if (tagExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists");
        }
        Tag tag = createTagFromRequest(tagRequest);
        tag.setId(0);
        return createTagResponse(tagRepository.save(tag));
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<TagResponse> getTags(Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Tag> page = tagRepository.findAll(pageable);
        List<TagResponse> responses = page.getContent().stream().map(this::createTagResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional(readOnly = true)
    @Override
    public TagResponse getTagById(long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return createTagResponse(tag.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<TagResponse> searchTags(String name, Pageable pageable) {
        User user = findUserAuthentication.getAuthenticatedUser();
        Page<Tag> page = tagRepository.findTagsByNameContains(name, pageable);
        List<TagResponse> responses = page.getContent().stream().map(this::createTagResponse).toList();
        return pageResponseUtil.createPageResponse(responses, page);
    }

    @Transactional
    @Override
    public TagResponse updateTag(long id, TagRequest tagRequest) {
        User user = findUserAuthentication.getAuthenticatedUser();
        if (!userRoleIndicator.isUserAdmin(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can edit tags");
        }
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such tag was found");
        }
        boolean nameTaken = tagRepository.findTagByName(tagRequest.name()).isPresent();
        if (nameTaken){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists");
        }
        tag.get().setName(tagRequest.name());
        return createTagResponse(tagRepository.save(tag.get()));
    }

    @Transactional
    @Override
    public void deleteTag(long id) {
        User user = findUserAuthentication.getAuthenticatedUser();
        if (!userRoleIndicator.isUserAdmin(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete tags");
        }
        boolean tagExists = tagRepository.findById(id).isPresent();
        if (!tagExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such tag was found");
        }
        quizRepository.deleteQuizzesByTags_Id(id);
        tagRepository.deleteById(id);
    }

    private Tag createTagFromRequest(TagRequest tagRequest) {
        return new Tag(tagRequest.name());
    }

    private TagResponse createTagResponse(Tag tag){
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                quizRepository.countByTags(List.of(tag))
        );
    }
}
