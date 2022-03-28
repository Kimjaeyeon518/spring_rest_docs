package spring.rest.api.doc.dto;

import lombok.Data;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Weapon;

import javax.persistence.*;

@Data
public class WeaponResponseDto {

    private Long id;
    private CharacterSpecies characterSpecies;  // 해당 무기 사용이 가능한 캐릭터 종족
    private String name;    // 무기 이름
    private String effect;  // 무기 효과

    public WeaponResponseDto(Weapon weapon) {
        id = weapon.getId();
        characterSpecies = weapon.getCharacterSpecies();
        name = weapon.getName();
        effect = weapon.getEffect();
    }
}
