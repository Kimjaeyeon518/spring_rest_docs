package spring.rest.api.doc.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.rest.api.doc.api.document.Docs;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.EnumType;
import spring.rest.api.doc.response.ApiResponseCode;
import spring.rest.api.doc.response.ApiResponseDto;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EnumViewController {       // test 패키지에 만들면 테스트 수행때만 동작

    @GetMapping("/docs")
    public ApiResponseDto<Docs> findAll() {

        // 문서화 하고 싶은 -> Docs 클래스에 담긴 모든 Enum 값 생성
        Map<String, String> apiResponseCodes = getDocs(ApiResponseCode.values());
        Map<String, String> characterSpecies = getDocs(CharacterSpecies.values());

        // 전부 담아서 반환 -> 테스트에서는 이걸 꺼내 해석하여 조각을 만들면 된다.
        return ApiResponseDto.createOK(
                Docs.builder()
                        .apiResponseCodes(apiResponseCodes)
                        .characterSpecies(characterSpecies)
                        .build()
        );
    }

    private Map<String, String> getDocs(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .collect(Collectors.toMap(EnumType::getId, EnumType::getText));
    }
}
