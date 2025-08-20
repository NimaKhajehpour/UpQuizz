package com.nima.upquizz.service;

import com.nima.upquizz.entity.Tag;
import com.nima.upquizz.request.CategoryRequest;
import com.nima.upquizz.request.TagRequest;
import com.nima.upquizz.response.CategoryResponse;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.TagResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    TagResponse addTag(TagRequest tagRequest);
    PageResponse<TagResponse> getTags(Pageable pageable);
    TagResponse getTagById(long id);
    PageResponse<TagResponse> searchTags(String name, Pageable pageable);
    TagResponse updateTag(long id, TagRequest tagRequest);
    void deleteTag(long id);
}
