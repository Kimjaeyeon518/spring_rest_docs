package spring.rest.api.doc.api.document;

import lombok.RequiredArgsConstructor;

public interface DocumentLinkGenerator {

    // mustache 문법 - 클릭 시 팝업창 생성
    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:common/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text, "코드");
    }

    static String generateText(DocUrl docUrl) {
        return String.format("%s %s", docUrl.text, "코드명");
    }

    @RequiredArgsConstructor
    enum DocUrl {
        CHARACTERSPECIES("characterSpecies", "종족"),
        ;

        private final String pageId;  // common 폴더에 있는 파일 명
        private final String text;    // 문서에 노출 되는 텍스트
    }
}