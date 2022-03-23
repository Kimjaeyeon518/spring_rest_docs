package spring.rest.api.doc.restcontroller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = DocApplication.class)
/*
 * RestDocumentationExtension.class : When using JUnit 5, generate documentation snippets
 *									 -> automatically configured with an output directory(build/generated-snippets)
 * SpringExtension.class : testing a typical Spring application
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
class CharacterRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private CharacterService characterService;

    private Characters characters;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        characters = Characters.builder()
                .id(1L)
                .weapon(new Weapon(1L))
                .build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))   // MockMvc instance is configured by using a MockMvcRestDocumentationConfigurer
                .build();
    }

    @Test
    @DisplayName("캐릭터 리스트 조회")
    public void getCharacterList() throws Exception {
        // given
        List<CharacterResponseDto> characterResponseDtoList = new ArrayList<>();
        characterResponseDtoList.add(characters.toResponseDto());
        given(characterService.findAll()).willReturn(characterResponseDtoList);

        // when
        this.mockMvc.perform(get("/characters").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("characterList", responseFields(
                        subsectionWithPath("characterList").description("An array of Characters"))));

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
        // pathParameters 를 사용하는 요청은 RestDocumentationRequestBuilders 사용
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/characters/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("character-read",
                        pathParameters(parameterWithName("id").description("The character's id")),
                        responseFields(
                                fieldWithPath("id").description("The character's id"),
                                fieldWithPath("hp").description("The character's hp"),
                                fieldWithPath("attackPower").description("The character's attackPower"),
                                fieldWithPath("attackSpeed").description("The character's attackSpeed"),
                                fieldWithPath("characterSpecies").description("The character's characterSpecies"),
                                subsectionWithPath("weapon").description("The character's weapon"))));

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
        this.mockMvc.perform(post("/characters")
                        .param("hp", "100F")
                        .param("attackPower", "10F")
                        .param("attackSpeed", "100F")
                        .param("characterSpecies", "HUMAN")
                        .param("weapon", "Sword"))
                .andExpect(status().isOk())
                .andDo(document("character-create",
                        requestParameters(parameterWithName("hp").description("The character's hp"),
                                parameterWithName("attackPower").description("The character's attackPower"),
                                parameterWithName("attackSpeed").description("The character's attackSpeed"),
                                parameterWithName("characterSpecies").description("The character's characterSpecies"),
                                parameterWithName("weapon").description("The character's weapon"))));

        // then
        then(characterService).should(times(1)).create(any(CharacterRequestDto.class));
    }

    @Test
    @DisplayName("캐릭터 수정")
    public void updateCharacter() throws Exception {
        // given
        Long id = 1L;
        given(characterService.update(any(Long.class), any(CharacterRequestDto.class))).willReturn(characters.toResponseDto());

        // when
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/characters/{id}", id)
                        .param("hp", "10F")
                        .param("attackPower", "10F")
                        .param("attackSpeed", "100F")
                        .param("characterSpecies", "HUMAN")
                        .param("weapon", "Sword"))
                .andExpect(status().isOk())
                .andDo(document("character-update",
                        pathParameters(parameterWithName("id").description("The character's id")),
                        requestParameters(parameterWithName("hp").description("The character's hp"),
                                parameterWithName("attackPower").description("The character's attackPower"),
                                parameterWithName("attackSpeed").description("The character's attackSpeed"),
                                parameterWithName("characterSpecies").description("The character's characterSpecies"),
                                parameterWithName("weapon").description("The character's weapon"))));

        // then
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
                        pathParameters(parameterWithName("id").description("The character's id"))));

        // then
        then(characterService).should(times(1)).deleteById(any(Long.class));
    }
}