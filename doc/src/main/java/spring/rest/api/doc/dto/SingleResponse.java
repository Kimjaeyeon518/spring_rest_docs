package spring.rest.api.doc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponse<T> {
    private T data;
}
