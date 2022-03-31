package spring.rest.api.doc.dto;

import lombok.*;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.domain.Weapon;

import java.time.LocalDate;

@Getter
@Builder
public class CharacterRequestDto {

    @Getter
    @Builder
    public static class create {
        private Float hp;
        private Float attackPower;
        private Integer attackSpeed;
        private CharacterSpecies characterSpecies;
        private LocalDate birthDate;

        public Characters toEntity() {
            return Characters.builder()
                    .hp(hp)
                    .attackPower(attackPower)
                    .attackSpeed(attackSpeed)
                    .characterSpecies(characterSpecies)
                    .birthDate(birthDate)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class update {
        private Float hp;
        private Float attackPower;
        private Integer attackSpeed;

        public void apply(Characters character) {
            character.update(this);
        }
    }
}
