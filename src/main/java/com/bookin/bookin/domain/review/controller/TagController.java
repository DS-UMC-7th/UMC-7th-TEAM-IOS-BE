package com.bookin.bookin.domain.review.controller;

import com.bookin.bookin.domain.review.dto.TagDTO;
import com.bookin.bookin.domain.review.service.TagService;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "전체 태그 목록 조회")
    @GetMapping
    public ApiResponse<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return ApiResponse.onSuccess(tags);
    }
}
