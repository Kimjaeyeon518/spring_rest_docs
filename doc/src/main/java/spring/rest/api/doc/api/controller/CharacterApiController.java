package spring.rest.api.doc.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.response.ApiResponseDto;
import spring.rest.api.doc.service.CharacterService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CharacterApiController {

    private final CharacterService characterService;

    @GetMapping("/v1/characters")
    public ApiResponseDto<List<CharacterResponseDto>> findAll() {
        return ApiResponseDto.createOK(characterService.findAll());
    }

    @GetMapping("/v1/characters/{id}")
    public ApiResponseDto<CharacterResponseDto> findOne(@PathVariable("id") Long id) {
        return ApiResponseDto.createOK(characterService.findById(id));
    }

    @PostMapping("/v1/characters")
    public ApiResponseDto<CharacterResponseDto> create(@ModelAttribute CharacterRequestDto.create characterRequestDto) {
        return ApiResponseDto.createOK(characterService.create(characterRequestDto));
    }

    @PutMapping("/v1/characters/{id}")
    public ApiResponseDto<CharacterResponseDto> update(@PathVariable("id") Long id,
                                                       @ModelAttribute CharacterRequestDto.update characterRequestDto) throws Exception {
        return ApiResponseDto.createOK(characterService.update(id, characterRequestDto));
    }

    @DeleteMapping("/v1/characters/{id}")
    public ApiResponseDto<String> delete(@PathVariable("id") Long id) {
        characterService.delete(id);
                
        return ApiResponseDto.DEFAULT_OK;
    }
}


