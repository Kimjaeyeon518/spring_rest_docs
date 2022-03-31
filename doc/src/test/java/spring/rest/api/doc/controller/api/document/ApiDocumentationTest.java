package spring.rest.api.doc.controller.api.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import spring.rest.api.doc.api.controller.CharacterApiController;
import spring.rest.api.doc.api.controller.EnumViewController;
import spring.rest.api.doc.service.CharacterService;

@WebMvcTest(controllers = {
        CharacterApiController.class,
        EnumViewController.class
})
@ExtendWith({
        RestDocumentationExtension.class, // 설정 시 documentation snippets default 생성 위치 : "build/generated-snippets"
        SpringExtension.class,
        MockitoExtension.class})
@AutoConfigureRestDocs      // REST Docs 선언
public abstract class ApiDocumentationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected CharacterService characterService;


    /**
     * .adoc file 생성 위치를 "src/main/resources/static" 으로 설정
     * @RegisterExtension
     * final RestDocumentationExtension restDocumentationExt = new RestDocumentationExtension ("src/main/resources/static");
     */
}
