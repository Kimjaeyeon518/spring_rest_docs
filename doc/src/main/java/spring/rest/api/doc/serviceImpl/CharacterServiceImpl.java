package spring.rest.api.doc.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.repository.CharacterRepository;
import spring.rest.api.doc.service.CharacterService;

import javax.persistence.EntityNotFoundException;
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
        List<CharacterResponseDto> characterResponseDtoList = characterRepository.findAll().stream()
                .map(c -> new CharacterResponseDto(c))
                .collect(Collectors.toList());

        return characterResponseDtoList;
    }

    @Override
    public CharacterResponseDto findById(Long id) {
        Characters characters = characterRepository.findById(id)
                .orElseThrow(NullPointerException::new);

        return characters.toResponseDto();
    }

    @Override
    public CharacterResponseDto create(CharacterRequestDto characterRequestDto) {
        Characters characters = characterRepository.save(characterRequestDto.toEntity());

        return characters.toResponseDto();
    }

    @Override
    public CharacterResponseDto update(Long id, CharacterRequestDto characterRequestDto) throws Exception {

        if(id != characterRequestDto.getId())
            throw new InvalidKeyException();

        if(!characterRepository.existsById(characterRequestDto.getId()))
            throw new EntityNotFoundException();

        Characters characters = characterRepository.save(characterRequestDto.toEntity());

        return characters.toResponseDto();
    }

    @Override
    public void deleteById(Long id) {
        if(!characterRepository.existsById(id))
            throw new EntityNotFoundException();

        characterRepository.deleteById(id);
    }
}
