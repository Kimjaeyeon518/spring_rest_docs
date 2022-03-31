package spring.rest.api.doc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.domain.Weapon;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.repository.CharacterRepository;
import spring.rest.api.doc.serviceImpl.CharacterServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @InjectMocks
    private CharacterServiceImpl characterService;
    @Mock
    private CharacterRepository characterRepository;

    private Characters characters;

    @BeforeEach
    public void initCharacter() {
        characters = Characters.builder()
                .id(1L)
                .weapon(new Weapon(1L))
                .build();
    }

    @Test
    @DisplayName("캐릭터 추가")
    public void addCharacter() {
        // given
        given(characterRepository.save(any(Characters.class))).willReturn(characters);

        CharacterRequestDto.create characterRequestDto = CharacterRequestDto.create.builder()
                .hp(100F)
                .attackPower(50F)
                .attackSpeed(100)
                .characterSpecies(CharacterSpecies.HUMAN)
                .build();

        // when
        CharacterResponseDto savedGameCharacter = characterService.create(characterRequestDto);

        // then
        then(characterRepository).should(times(1)).save(any(Characters.class));
        assertThat(savedGameCharacter.getId()).isEqualTo(characters.getId());
    }

    @Test
    @DisplayName("캐릭터 조회")
    public void findCharacter() {
        // given
        given(characterRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(characters));

        // when
        Characters foundCharacter = characterRepository.findById(1L).get();

        // then
        then(characterRepository).should(times(1)).findById(1L);
        assertThat(foundCharacter).isEqualTo(characters);
    }

    @Test
    @DisplayName("캐릭터 리스트 조회")
    public void findAllCharacter() {
        // given
        List<Characters> characterList = new ArrayList<>();
        characterList.add(characters);
        given(characterRepository.findAll()).willReturn(characterList);

        // when
        List<Characters> findCharacterList = characterRepository.findAll();

        // then
        then(characterRepository).should(times(1)).findAll();
        assertThat(characterList).isEqualTo(findCharacterList);
    }

    @Test
    @DisplayName("캐릭터 삭제")
    public void deleteCharacter() {
        // given
        given(characterRepository.save(any(Characters.class))).willReturn(characters);
        given(characterRepository.existsById(any(Long.class))).willReturn(false);

        // when
        Characters savedCharacter = characterRepository.save(characters);
        characterRepository.deleteById(savedCharacter.getId());

        // then
        then(characterRepository).should(times(1)).save(characters);
        then(characterRepository).should(times(1)).deleteById(savedCharacter.getId());
        assertThat(characterRepository.existsById(characters.getId())).isEqualTo(false);
    }

}