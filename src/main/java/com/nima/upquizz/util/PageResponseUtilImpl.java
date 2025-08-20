package com.nima.upquizz.util;

import com.nima.upquizz.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageResponseUtilImpl implements PageResponseUtil {
    @Override
    public <T, U> PageResponse<T> createPageResponse(List<T> responses, Page<U> page) {
        return new PageResponse<>(
                responses,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
