package spring.rest.api.doc.domain;

import lombok.*;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.dto.WeaponResponseDto;

import javax.persistence.*;
import javax.persistence.EnumType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Character")
public class Characters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float hp;
    private Float attackPower; // 공격력
    private Integer attackSpeed; // 공격속도
    private LocalDate birthDate;

    @Enumerated(value = EnumType.STRING)
    private CharacterSpecies characterSpecies;     // 종족

    @ManyToOne
    @JoinColumn(name = "weapon_id", nullable = true)
    private Weapon weapon;

    public CharacterResponseDto toResponseDto() {
        return CharacterResponseDto.builder()
                .id(this.id)
                .hp(this.hp)
                .attackPower(this.attackPower)
                .attackSpeed(this.attackSpeed)
                .characterSpecies(this.characterSpecies)
//                .weapon(new WeaponResponseDto(this.weapon))
                .birthDate(this.birthDate)
                .build();
    }

    public void update(CharacterRequestDto.update characterRequestDto) {
        this.hp = characterRequestDto.getHp();
        this.attackPower = characterRequestDto.getAttackPower();
        this.attackSpeed = characterRequestDto.getAttackSpeed();
    }
}
