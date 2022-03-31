package spring.rest.api.doc.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum CharacterSpecies implements EnumType {

    COMMON("공통"),
    HUMAN("인간"),
    OAK("오크"),
    ELF("엘프");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
