package com.nima.upquizz.controller;

import com.nima.upquizz.request.TagRequest;
import com.nima.upquizz.response.PageResponse;
import com.nima.upquizz.response.TagResponse;
import com.nima.upquizz.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "tags endpoints", description = "endpoints used to access tags")
@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "create tag")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse createTag(@Valid @RequestBody TagRequest tagRequest) {
        return tagService.addTag(tagRequest);
    }

    @Operation(summary = "get tags")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TagResponse> getTags(
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return tagService.getTags(pageable);
    }

    @Operation(summary = "search tags")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TagResponse> searchTags(
            @RequestParam @Size(min = 2, message = "tag name must at least have 2 characters") String name,
            @RequestParam(defaultValue = "0", required = false) @Min(value = 0, message = "page number cant be less than 0") int page,
            @RequestParam(defaultValue = "10", required = false) @Min(value = 5, message = "page size must be at least 5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return tagService.searchTags(name, pageable);
    }

    @Operation(summary = "get tag by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagResponse getTagById(@PathVariable @Min(value = 1, message = "id can be less than 1") long id) {
        return tagService.getTagById(id);
    }

    @Operation(summary = "update tag")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagResponse updateTag(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id, @Valid @RequestBody TagRequest tagRequest) {
        return tagService.updateTag(id, tagRequest);
    }

    @Operation(summary = "delete tag")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable @Min(value = 1, message = "id cant be less than 1") long id) {
        tagService.deleteTag(id);
    }
}
