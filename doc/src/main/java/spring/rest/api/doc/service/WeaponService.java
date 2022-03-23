package spring.rest.api.doc.service;

import spring.rest.api.doc.domain.Weapon;

public interface WeaponService {
    Weapon save(Weapon weapon);
    Weapon findById(Long weaponId);
}
