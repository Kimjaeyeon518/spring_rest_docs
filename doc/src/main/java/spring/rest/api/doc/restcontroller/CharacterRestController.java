package spring.rest.api.doc.restcontroller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.dto.ListResponse;
import spring.rest.api.doc.dto.SingleResponse;
import spring.rest.api.doc.service.CharacterService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CharacterRestController {

    private final CharacterService characterService;

    @GetMapping("/characters")
    public ListResponse<CharacterResponseDto> findAll() {
        ListResponse<CharacterResponseDto> result = new ListResponse<>();
        result.setData(characterService.findAll());
        return result;
    }

    @GetMapping("/characters/{id}")
    public SingleResponse<CharacterResponseDto> findOne(@PathVariable("id") Long id) {
        SingleResponse<CharacterResponseDto> result = new SingleResponse<>();
        result.setData(characterService.findById(id));
        return result;
    }

    @PostMapping("/characters")
    public SingleResponse<CharacterResponseDto> create(@ModelAttribute CharacterRequestDto characterRequestDto) {
        SingleResponse<CharacterResponseDto> result = new SingleResponse<>();
        result.setData(characterService.create(characterRequestDto));
        return result;
    }

    @PutMapping("/characters/{id}")
    public SingleResponse<CharacterResponseDto> update(@PathVariable("id") Long id,
                                                       @ModelAttribute CharacterRequestDto characterRequestDto) throws Exception {
        SingleResponse<CharacterResponseDto> result = new SingleResponse<>();
        result.setData(characterService.update(id, characterRequestDto));
        return result;
    }

    @DeleteMapping("/characters/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        characterService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("successfully delete !");
    }
}


