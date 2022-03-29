package spring.rest.api.doc.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import spring.rest.api.doc.response.ApiResponseCode;

@ToString
@NoArgsConstructor
@Getter
public class ApiResponseDto<T> {
    public static final spring.rest.api.doc.response.ApiResponseDto<String> DEFAULT_OK = new spring.rest.api.doc.response.ApiResponseDto<>(ApiResponseCode.OK);
    public static final spring.rest.api.doc.response.ApiResponseDto<String> DEFAULT_UNAUTHORIZED = new spring.rest.api.doc.response.ApiResponseDto<>(ApiResponseCode.UNAUTHORIZED);

    private ApiResponseCode code;
    private String message;
    private T data;

    private ApiResponseDto(ApiResponseCode status) {
        this.bindStatus(status);
    }

    private ApiResponseDto(ApiResponseCode status, T data) {
        this.bindStatus(status);
        this.data = data;
    }

    private void bindStatus(ApiResponseCode status) {
        this.code = status;
        this.message = status.getMessage();
    }

    public static <T> spring.rest.api.doc.response.ApiResponseDto<T> createOK(T data) {
        return new spring.rest.api.doc.response.ApiResponseDto<>(ApiResponseCode.OK, data);
    }
}