package spring.rest.api.doc.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import spring.rest.api.doc.DocApplication;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.domain.Weapon;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;
import spring.rest.api.doc.service.CharacterService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = DocApplication.class)
/**
 * @ExtendWith(RestDocumentationExtension.class) : 설정 시 documentation snippets default 생성 위치 : "build/generated-snippets"
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class CharacterRestControllerTest {

    /**
     * .adoc file 생성 위치를 "src/main/resources/static" 으로 설정
     */
    @RegisterExtension
    final RestDocumentationExtension restDocumentationExt = new RestDocumentationExtension ("src/main/resources/static");

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CharacterService characterService;

    private MockMvc mockMvc;

    private Characters characters;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        characters = Characters.builder()
                .id(1L)
                .hp(100F)
                .attackPower(15F)
                .attackSpeed(50)
                .characterSpecies(CharacterSpecies.HUMAN)
                .build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                // MockMvc instance 에 MockMvcRestDocumentationConfigurer 적용
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withResponseDefaults(prettyPrint()))
                .build();
    }


    @Test
    @DisplayName("캐릭터 리스트 조회")
    public void getCharacterList() throws Exception {
        // given
        List<CharacterResponseDto> characterResponseDtoList = new ArrayList<>();
        characterResponseDtoList.add(characters.toResponseDto());
        characterResponseDtoList.add(characters.toResponseDto());
        characterResponseDtoList.add(characters.toResponseDto());
        given(characterService.findAll()).willReturn(characterResponseDtoList);

        // when
        this.mockMvc.perform(get("/characters").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("characterList", responseFields(
                        // subsectionWithPath : 상위 레벨 수준의 overview 제공
                        subsectionWithPath("data").description("캐릭터 배열"))));

        // then
        then(characterService).should(times(1)).findAll();
        assertThat(characterService.findAll()).isEqualTo(characterResponseDtoList);
    }

    @Test
    @DisplayName("캐릭터 조회")
    public void getCharacter() throws Exception {
        // given
        Long id = 1L;
        given(characterService.findById(any(Long.class))).willReturn(characters.toResponseDto());

        // when
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/characters/{id}", id).accept(MediaType.APPLICATION_JSON))  // pathParameters 를 사용하는 요청은 RestDocumentationRequestBuilders 사용
                .andExpect(status().isOk())
                .andDo(document("character-read",
                        pathParameters(parameterWithName("id").description("The character's id")),
                        responseFields(
                                // fieldWithPath : 구체적인 overview 제공
                                // type(JsonFieldType.**) : 해당 필드의 타입 정보 설정
                                fieldWithPath("data.id").description("캐릭터 ID"),
                                fieldWithPath("data.hp").type(JsonFieldType.NUMBER).description("캐릭터의 체력"),
                                fieldWithPath("data.attackPower").type(JsonFieldType.NUMBER).description("캐릭터의 공격력"),
                                fieldWithPath("data.attackSpeed").type(JsonFieldType.NUMBER).description("캐릭터의 공격속도"),
                                fieldWithPath("data.characterSpecies").type(JsonFieldType.STRING).description("캐릭터의 종족"),
                                subsectionWithPath("data.weapon").description("캐릭터의 무기"))));

        // then
        then(characterService).should(times(1)).findById(id);
        assertThat(characterService.findById(id).getId()).isEqualTo(characters.getId());
    }

    @Test
    @DisplayName("캐릭터 추가")
    public void createCharacter() throws Exception {
        // given
        given(characterService.create(any(CharacterRequestDto.class))).willReturn(characters.toResponseDto());

        // when
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/characters")
                        .content(objectMapper.writeValueAsString(characters))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(document("character-create",
                        preprocessRequest(
                                modifyUris() // (1)
                                        .scheme("https")
                                        .host("docs.api.com")
                                        .removePort(),
                                prettyPrint()),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("캐릭터 ID(=NULL)").optional(),
                                fieldWithPath("hp").type(JsonFieldType.NUMBER).description("캐릭터의 체력"),
                                fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("캐릭터의 공격력"),
                                fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("캐릭터의 공격속도"),
                                fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description("캐릭터의 종족"),
                                fieldWithPath("weapon").type(JsonFieldType.STRING).description("캐릭터의 무기").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("생성된 캐릭터 ID"),
                                fieldWithPath("data.hp").type(JsonFieldType.NUMBER).description("생성된 캐릭터 체력"),
                                fieldWithPath("data.attackPower").type(JsonFieldType.NUMBER).description("생성된 캐릭터의 공격력"),
                                fieldWithPath("data.attackSpeed").type(JsonFieldType.NUMBER).description("생성된 캐릭터의 공격속도"),
                                fieldWithPath("data.characterSpecies").type(JsonFieldType.STRING).description("생성된 캐릭터의 종족"),
                                fieldWithPath("data.weapon").type(JsonFieldType.STRING).description("생성된 캐릭터의 무기").optional()
                        )
                ));

        then(characterService).should(times(1)).create(any(CharacterRequestDto.class));
    }

    @Test
    @DisplayName("캐릭터 수정")
    public void updateCharacter() throws Exception {
        // given
        Long id = 1L;
        given(characterService.update(any(Long.class), any(CharacterRequestDto.class))).willReturn(characters.toResponseDto());

        // when
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.put("/characters/{id}", id)
                        .content(objectMapper.writeValueAsString(characters))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(document("character-update",
                        preprocessRequest(
                                modifyUris() // (1)
                                        .scheme("https")
                                        .host("docs.api.com")
                                        .removePort(),
                                prettyPrint()),
                        // pathParameters : 파라미터 정보
                        pathParameters(
                                parameterWithName("id").description("캐릭터 ID")
                        ),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("캐릭터 ID"),
                                fieldWithPath("hp").type(JsonFieldType.NUMBER).description("캐릭터의 체력"),
                                fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("캐릭터의 공격력"),
                                fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("캐릭터의 공격속도"),
                                fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description("캐릭터의 종족"),
                                fieldWithPath("weapon").type(JsonFieldType.STRING).description("캐릭터의 무기").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("수정된 캐릭터 ID"),
                                fieldWithPath("data.hp").type(JsonFieldType.NUMBER).description("수정된 캐릭터 체력"),
                                fieldWithPath("data.attackPower").type(JsonFieldType.NUMBER).description("수정된 캐릭터의 공격력"),
                                fieldWithPath("data.attackSpeed").type(JsonFieldType.NUMBER).description("수정된 캐릭터의 공격속도"),
                                fieldWithPath("data.characterSpecies").type(JsonFieldType.STRING).description("수정된 캐릭터의 종족"),
                                fieldWithPath("data.weapon").type(JsonFieldType.STRING).description("수정된 캐릭터의 무기").optional()
                        )
                ));

        then(characterService).should(times(1)).update(any(Long.class), any(CharacterRequestDto.class));
    }

    @Test
    @DisplayName("캐릭터 삭제")
    public void deleteCharacter() throws Exception {
        // given
        Long id = 1L;

        // when
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/characters/{id}", id))
                .andExpect(status().isOk())
                .andDo(document("character-delete",
                        pathParameters(parameterWithName("id").description("삭제된 캐릭터 ID"))));

        // then
        then(characterService).should(times(1)).deleteById(any(Long.class));
    }
}