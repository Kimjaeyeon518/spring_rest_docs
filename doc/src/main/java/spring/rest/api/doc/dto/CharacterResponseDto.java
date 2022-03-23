package spring.rest.api.doc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Weapon;

@Builder
@Getter
public class CharacterResponseDto {

    private Long id;
    private Float hp;
    private Float attackPower;          // 공격력
    private Integer attackSpeed;        // 공격속도
    private CharacterSpecies characterSpecies;      // 종족
    private Weapon weapon;
}
