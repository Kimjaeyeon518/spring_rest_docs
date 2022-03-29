package spring.rest.api.doc.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.response.ApiResponseDto;
import spring.rest.api.doc.service.CharacterService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CharacterRestController {

    private final CharacterService characterService;

    @GetMapping("/characters")
    public ApiResponseDto<List<CharacterResponseDto>> findAll() {
        return ApiResponseDto.createOK(characterService.findAll());
    }

    @GetMapping("/characters/{id}")
    public ApiResponseDto<CharacterResponseDto> findOne(@PathVariable("id") Long id) {
        return ApiResponseDto.createOK(characterService.findById(id));
    }

    @PostMapping("/characters")
    public ApiResponseDto<CharacterResponseDto> create(@ModelAttribute CharacterRequestDto characterRequestDto) {
        return ApiResponseDto.createOK(characterService.create(characterRequestDto));
    }

    @PutMapping("/characters/{id}")
    public ApiResponseDto<CharacterResponseDto> update(@PathVariable("id") Long id,
                                                       @ModelAttribute CharacterRequestDto characterRequestDto) throws Exception {
        return ApiResponseDto.createOK(characterService.update(id, characterRequestDto));
    }

    @DeleteMapping("/characters/{id}")
    public ApiResponseDto<String> delete(@PathVariable("id") Long id) {
        characterService.delete(id);
                
        return ApiResponseDto.DEFAULT_OK;
    }
}


