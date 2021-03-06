package spring.rest.api.doc.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.rest.api.doc.domain.EnumType;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode implements EnumType {
    OK("요청이 성공하였습니다."),
    BAD_PARAMETER("요청 파라미터가 잘못되었습니다."),
    NOT_FOUND("리소스를 찾지 못했습니다."),
    UNAUTHORIZED("인증에 실패하였습니다."),
    SERVER_ERROR("서버 에러입니다.");

    private final String text;

    @Override
    public String getId() {
        return name();
    }
}