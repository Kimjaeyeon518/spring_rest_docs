package spring.rest.api.doc.controller.api.document.util;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface ApiDocumentUtils {

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris() // 문서상 request uri 변경
                        .scheme("https")
                        .host("docs.api.com")
                        .removePort(),
                prettyPrint()); // 문서의 request 를 예쁘게 출력
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint()); // 문서의 response 를 예쁘게 출력
    }
}

