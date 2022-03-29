package spring.rest.api.doc.service;

import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;

import java.util.List;

public interface CharacterService {

    List<CharacterResponseDto> findAll();
    CharacterResponseDto findById(Long id);
    CharacterResponseDto create(CharacterRequestDto characterRequestDto);
    CharacterResponseDto update(Long id, CharacterRequestDto characterRequestDto) throws Exception;
    void delete(Long id);
}
