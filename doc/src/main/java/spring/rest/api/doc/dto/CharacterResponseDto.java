package spring.rest.api.doc.dto;

import lombok.*;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.domain.Weapon;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CharacterResponseDto {
    private Long id;
    private Float hp;
    private Float attackPower;          // 공격력
    private Integer attackSpeed;        // 공격속도
    private CharacterSpecies characterSpecies;      // 종족
    private WeaponResponseDto weapon;

    public CharacterResponseDto(Characters characters) {
        id = characters.getId();
        hp = characters.getHp();
        attackPower = characters.getAttackPower();
        characterSpecies = characters.getCharacterSpecies();
        weapon = new WeaponResponseDto(characters.getWeapon());
    }

    @Data
    public static class delete {
        private Long id;
    }
}
