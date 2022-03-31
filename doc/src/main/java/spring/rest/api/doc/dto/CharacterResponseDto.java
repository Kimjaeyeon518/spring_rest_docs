package spring.rest.api.doc.dto;

import lombok.*;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.domain.Weapon;

import java.time.LocalDate;

@Getter
@Builder
public class CharacterResponseDto {
    private Long id;
    private Float hp;
    private Float attackPower;          // 공격력
    private Integer attackSpeed;        // 공격속도
    private CharacterSpecies characterSpecies;      // 종족
    private LocalDate birthDate;

    @Data
    public static class delete {
        private Long id;
    }

    public static CharacterResponseDto of(Characters characters) {
        return CharacterResponseDto.builder()
                .id(characters.getId())
                .hp(characters.getHp())
                .attackPower(characters.getAttackPower())
                .attackSpeed(characters.getAttackSpeed())
                .characterSpecies(characters.getCharacterSpecies())
//                .weapon(characters.toResponseDto().getWeapon())
                .birthDate(characters.getBirthDate())
                .build();
    }
}
