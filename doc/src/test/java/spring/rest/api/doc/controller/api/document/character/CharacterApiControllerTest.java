package spring.rest.api.doc.controller.api.document.character;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import spring.rest.api.doc.controller.api.document.ApiDocumentationTest;
import spring.rest.api.doc.domain.CharacterSpecies;
import spring.rest.api.doc.domain.Characters;
import spring.rest.api.doc.dto.CharacterRequestDto;
import spring.rest.api.doc.dto.CharacterResponseDto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring.rest.api.doc.controller.api.document.util.ApiDocumentUtils.getDocumentRequest;
import static spring.rest.api.doc.controller.api.document.util.ApiDocumentUtils.getDocumentResponse;
import static spring.rest.api.doc.controller.api.document.util.DocumentFormatGenerator.getDateFormat;
import static spring.rest.api.doc.api.document.DocumentLinkGenerator.DocUrl.CHARACTERSPECIES;
import static spring.rest.api.doc.api.document.DocumentLinkGenerator.generateLinkCode;

public class CharacterApiControllerTest extends ApiDocumentationTest {

    private Characters characters = Characters.builder()
            .id(1L)
            .hp(100F)
            .attackPower(15F)
            .attackSpeed(50)
            .characterSpecies(CharacterSpecies.HUMAN)
            .birthDate(LocalDate.now())
            .build();

    @Test
    @DisplayName("캐릭터 리스트 조회")
    public void getCharacterList() throws Exception {
        //given
        List<CharacterResponseDto> responseList = Arrays.asList(
                CharacterResponseDto.builder()
                        .id(1L)
                        .hp(100F)
                        .attackPower(15F)
                        .attackSpeed(50)
                        .characterSpecies(CharacterSpecies.HUMAN)
                        .birthDate(LocalDate.now())
                        .build(),
                CharacterResponseDto.builder()
                        .id(2L)
                        .hp(50F)
                        .attackPower(25F)
                        .attackSpeed(100)
                        .characterSpecies(CharacterSpecies.OAK)
                        .birthDate(LocalDate.now())
                        .build()
        );

        given(characterService.findAll()).willReturn(responseList);

        //when
        ResultActions result = this.mockMvc.perform(
                get("/v1/characters")
                        .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(document("character-find-all",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(beneathPath("data[]").withSubsectionId("data"),
                            fieldWithPath("id").description("캐릭터 ID"),
                            fieldWithPath("hp").type(JsonFieldType.NUMBER).description("캐릭터의 체력"),
                            fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("캐릭터의 공격력"),
                            fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("캐릭터의 공격속도"),
                            fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description(generateLinkCode(CHARACTERSPECIES)),
                            fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("캐릭터 생성일")
//                            fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description("<<CharacterSpecies,캐릭터 종족>>") // '<<ID,설명>>' : 클릭 시 adoc 문서의 '[[ID]]' 위치로 이동
                        )
                ));

        then(characterService).should(times(1)).findAll();
        assertThat(characterService.findAll()).isEqualTo(responseList);
    }

    @Test
    @DisplayName("캐릭터 조회")
    public void getCharacter() throws Exception {
        //given
        given(characterService.findById(1L)).willReturn(characters.toResponseDto());

        //when
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/characters/{id}", 1L) // pathParameters 를 사용하는 요청은 RestDocumentationRequestBuilders 사용
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(document("character-find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        // pathParameters : 파라미터 정보
                        pathParameters(
                                parameterWithName("id").description("조회할 캐릭터 ID")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),    // beneathPath : 'response-fields-data.snippets' 문서에 "data" 라는 필드의 내부값만 출력
                                // fieldWithPath : 구체적인 overview 제공
                                // type(JsonFieldType.**) : 해당 필드의 타입 정보 설정
                                fieldWithPath("id").description("캐릭터 ID"),
                                fieldWithPath("hp").type(JsonFieldType.NUMBER).description("캐릭터의 체력"),
                                fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("캐릭터의 공격력"),
                                fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("캐릭터의 공격속도"),
                                fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description(generateLinkCode(CHARACTERSPECIES)),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("캐릭터 생성일")
                        )
                ));

        then(characterService).should(times(1)).findById(1L);
        assertThat(characterService.findById(1L).getId()).isEqualTo(characters.getId());
    }

    @Test
    @DisplayName("캐릭터 추가")
    public void createCharacter() throws Exception {
        //given
        given(characterService.create(any(CharacterRequestDto.create.class))).willReturn(characters.toResponseDto());

        CharacterRequestDto.create requestDto = CharacterRequestDto.create.builder()
                .hp(100F)
                .attackPower(50F)
                .attackSpeed(100)
                .characterSpecies(CharacterSpecies.HUMAN)
                .birthDate(LocalDate.now())
                .build();

        //when
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/characters")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(document("character-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(      // .optional() 을 추가한 필드는 문서상 필수값 'false'
                                fieldWithPath("hp").type(JsonFieldType.NUMBER).description("캐릭터의 체력"),
                                fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("캐릭터의 공격력"),
                                fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("캐릭터의 공격속도"),
                                fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description(generateLinkCode(CHARACTERSPECIES)),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("캐릭터 생성일")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 캐릭터 ID"),
                                fieldWithPath("hp").type(JsonFieldType.NUMBER).description("생성된 캐릭터 체력"),
                                fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("생성된 캐릭터의 공격력"),
                                fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("생성된 캐릭터의 공격속도"),
                                fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description(generateLinkCode(CHARACTERSPECIES)),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("캐릭터 생성일")
                        )
                ));

        then(characterService).should(times(1)).create(any(CharacterRequestDto.create.class));
    }

    @Test
    @DisplayName("캐릭터 수정")
    public void updateCharacter() throws Exception {
        //given
        given(characterService.update(eq(1L), any(CharacterRequestDto.update.class))).willReturn(characters.toResponseDto());

        CharacterRequestDto.update requestDto = CharacterRequestDto.update.builder()
                .hp(100F)
                .attackPower(50F)
                .attackSpeed(100)
                .build();

        //when
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.put("/v1/characters/{id}", 1L)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(document("character-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("캐릭터 ID")
                        ),
                        requestFields(
                                fieldWithPath("hp").type(JsonFieldType.NUMBER).description("캐릭터의 체력"),
                                fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("캐릭터의 공격력"),
                                fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("캐릭터의 공격속도")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("수정된 캐릭터 ID"),
                                fieldWithPath("hp").type(JsonFieldType.NUMBER).description("수정된 캐릭터 체력"),
                                fieldWithPath("attackPower").type(JsonFieldType.NUMBER).description("수정된 캐릭터의 공격력"),
                                fieldWithPath("attackSpeed").type(JsonFieldType.NUMBER).description("수정된 캐릭터의 공격속도"),
                                fieldWithPath("characterSpecies").type(JsonFieldType.STRING).description(generateLinkCode(CHARACTERSPECIES)),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("캐릭터 생성일")
                        )
                ));

        then(characterService).should(times(1)).update(eq(1L), any(CharacterRequestDto.update.class));
    }

    @Test
    @DisplayName("캐릭터 삭제")
    public void deleteCharacter() throws Exception {
        //given
        doNothing().when(characterService).delete(1L);

        //when
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/v1/characters/{id}", 1L));

        //then
        result.andExpect(status().isOk())
                .andDo(document("character-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("삭제할 캐릭터 ID")
                        )
                ));

        then(characterService).should(times(1)).delete(1L);
    }
}