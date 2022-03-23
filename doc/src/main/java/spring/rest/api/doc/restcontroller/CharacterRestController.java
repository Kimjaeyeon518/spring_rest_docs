package spring.rest.api.doc.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.service.CharacterService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CharacterRestController {

    private final CharacterService characterService;

    @GetMapping("/characters")
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                Collections.singletonMap("characterList", characterService.findAll()));
    }

    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterResponseDto> findOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(characterService.findById(id));
    }

    @PostMapping("/characters")
    public ResponseEntity<CharacterResponseDto> create(@ModelAttribute CharacterRequestDto characterRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(characterService.create(characterRequestDto));
    }

    @PutMapping("/characters/{id}")
    public ResponseEntity<CharacterResponseDto> update(@PathVariable("id") Long id,
                                                       @ModelAttribute CharacterRequestDto characterRequestDto) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(characterService.update(id, characterRequestDto));
    }

    @DeleteMapping("/characters/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        characterService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("successfully delete !");
    }
}


