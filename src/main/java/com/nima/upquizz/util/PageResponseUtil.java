package com.nima.upquizz.util;

import com.nima.upquizz.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PageResponseUtil {

    <T, U> PageResponse<T> createPageResponse(List<T> responses, Page<U> page);
}
