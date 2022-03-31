package spring.rest.api.doc.api.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Docs {

    // 문서화하고 싶은 모든 enum 값을 명시
    private Map<String, String> apiResponseCodes;
    private Map<String, String> characterSpecies;

}