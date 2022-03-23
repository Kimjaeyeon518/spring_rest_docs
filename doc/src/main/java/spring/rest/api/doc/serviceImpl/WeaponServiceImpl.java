package spring.rest.api.doc.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.rest.api.doc.domain.Weapon;
import spring.rest.api.doc.repository.WeaponRepository;
import spring.rest.api.doc.service.WeaponService;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeaponServiceImpl implements WeaponService {

    private final WeaponRepository weaponRepository;

    @Override
    public Weapon save(Weapon weapon) {
        return weaponRepository.save(weapon);
    }

    @Override
    public Weapon findById(Long weaponId) {
        Weapon weapon = weaponRepository.findById(weaponId)
                .orElseThrow(NullPointerException::new);

        return weapon;
    }
}
