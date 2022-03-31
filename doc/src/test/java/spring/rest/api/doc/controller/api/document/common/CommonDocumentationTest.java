package spring.rest.api.doc.controller.api.document.common;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import spring.rest.api.doc.controller.api.document.ApiDocumentationTest;
import spring.rest.api.doc.api.document.Docs;
import spring.rest.api.doc.response.ApiResponseDto;
import spring.rest.api.doc.controller.api.document.util.CustomResponseFieldsSnippet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonDocumentationTest extends ApiDocumentationTest {

    @Test
    @DisplayName("공통 코드")
    public void commons() throws Exception {

        //when
        ResultActions result = this.mockMvc.perform(
                get("/docs")
                        .accept(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();

        // 데이터 파싱
        Docs docs = getData(mvcResult);

        //then
        result.andExpect(status().isOk())
                .andDo(document("common",
                        customResponseFields("custom-response", null,
                                attributes(key("title").value("공통응답")),
                                subsectionWithPath("data").description("데이터"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지")
                        ),
                        customResponseFields("custom-response", beneathPath("data.apiResponseCodes").withSubsectionId("apiResponseCodes"),
                                attributes(key("title").value("응답코드")),
                                enumConvertFieldDescriptor(docs.getApiResponseCodes())
                        ),
                        customResponseFields("custom-response", beneathPath("data.characterSpecies").withSubsectionId("characterSpecies"),
                                attributes(key("title").value("종족코드")),
                                enumConvertFieldDescriptor(docs.getCharacterSpecies())
                        )
                ));
    }

    // mvc result 데이터 파싱
    Docs getData(MvcResult result) throws IOException {
        ApiResponseDto<Docs> apiResponseDto = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                new TypeReference<ApiResponseDto<Docs>>() {
                });

        return apiResponseDto.getData();
    }

    // Map 으로 넘어온 enumValue 를 fieldWithPath 로 변경하여 리턴
    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {

        return enumValues.entrySet().stream()
                .map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
                .toArray(FieldDescriptor[]::new);
    }

    // 커스텀 템플릿 사용을 위한 함수
    public static CustomResponseFieldsSnippet customResponseFields(String type,
                                                                   PayloadSubsectionExtractor<?> subsectionExtractor,
                                                                   Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
                , true);
    }
}