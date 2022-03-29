package spring.rest.api.doc.dto;

import lombok.Builder;
import lombok.Getter;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.domain.Weapon;

@Getter
public class CharacterRequestDto {

    private Long id;
    private Float hp;
    private Float attackPower;          // 공격력
    private Integer attackSpeed;        // 공격속도
    private CharacterSpecies characterSpecies;      // 종족

    public CharacterRequestDto(Long id) {
        this.id = id;
    }

    public Characters toEntity() {
        return Characters.builder()
                .id(this.id)
                .hp(this.hp)
                .attackPower(this.attackPower)
                .attackSpeed(this.attackSpeed)
                .characterSpecies(this.characterSpecies)
                .build();
    }

    public void apply(Characters character) {
        character.update(this);
    }
}
