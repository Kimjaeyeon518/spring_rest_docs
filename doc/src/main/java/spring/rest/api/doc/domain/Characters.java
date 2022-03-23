package spring.rest.api.doc.domain;

import lombok.*;
import spring.rest.api.doc.dto.CharacterResponseDto;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private CharacterSpecies characterSpecies;     // 종족

    @ManyToOne
    @JoinColumn(name = "weapon_id", nullable = false)
    private Weapon weapon;

    public CharacterResponseDto toResponseDto() {
        return CharacterResponseDto.builder()
                .id(this.id)
                .hp(this.hp)
                .attackPower(this.attackPower)
                .attackSpeed(this.attackSpeed)
                .characterSpecies(this.characterSpecies)
                .weapon(this.weapon)
                .build();
    }
}
