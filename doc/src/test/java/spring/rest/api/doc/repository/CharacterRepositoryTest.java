package spring.rest.api.doc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.domain.Weapon;
import spring.rest.api.doc.dto.CharacterResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CharacterRepositoryTest {

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

        // when
        Characters savedGameCharacter = characterRepository.save(characters);

        // then
        then(characterRepository).should(times(1)).save(characters);
        assertThat(savedGameCharacter).isEqualTo(characters);
    }

    @Test
    @DisplayName("캐릭터 조회")
    public void findCharacter() {
        // given
        given(characterRepository.save(any(Characters.class))).willReturn(characters);
        given(characterRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(characters));

        // when
        Characters savedGameCharacter = characterRepository.save(characters);
        Characters foundGameCharacter = characterRepository.findById(characters.getId()).get();

        // then
        then(characterRepository).should(times(1)).save(characters);
        then(characterRepository).should(times(1)).findById(characters.getId());
        assertThat(savedGameCharacter).isEqualTo(foundGameCharacter);
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
        Characters savedGameCharacter = characterRepository.save(characters);
        characterRepository.deleteById(savedGameCharacter.getId());

        // then
        then(characterRepository).should(times(1)).save(characters);
        then(characterRepository).should(times(1)).deleteById(savedGameCharacter.getId());
        assertThat(characterRepository.existsById(characters.getId())).isEqualTo(false);
    }
}