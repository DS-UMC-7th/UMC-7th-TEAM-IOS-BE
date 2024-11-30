package com.bookin.bookin.domain.review.service;

import com.bookin.bookin.domain.review.dto.TagDTO;
import com.bookin.bookin.domain.review.entity.Tag;
import com.bookin.bookin.domain.review.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());  // Tag를 TagDTO로 변환하여 반환
    }
}
