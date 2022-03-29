package spring.rest.api.doc.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.exception.NotFoundException;
import spring.rest.api.doc.repository.CharacterRepository;
import spring.rest.api.doc.service.CharacterService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

    @Override
    public List<CharacterResponseDto> findAll() {
        return characterRepository.findAll().stream()
                .map(c -> new CharacterResponseDto(c))
                .collect(Collectors.toList());
    }

    @Override
    public CharacterResponseDto findById(Long id) {
        return characterRepository.findById(id)
                .map(c -> new CharacterResponseDto(c))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public CharacterResponseDto create(CharacterRequestDto characterRequestDto) {
        Characters character = characterRepository.save(characterRequestDto.toEntity());

        return character.toResponseDto();
    }

    @Override
    @Transactional
    public CharacterResponseDto update(Long id, CharacterRequestDto characterRequestDto) {

        Characters character = characterRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        if(id != character.getId())
            throw new NotFoundException();

        characterRequestDto.apply(character);

        return new CharacterResponseDto(character);
    }

    @Override
    public void delete(Long id) {
        if(!characterRepository.existsById(id))
            throw new NotFoundException();

        characterRepository.deleteById(id);
    }
}
