package ee.taltech.iti0302project.response;

import ee.taltech.iti0302project.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class FeedResponse {

    private List<PostDto> posts;
    private Integer pageNo;
    private Integer pageSize;
    private Long elementsTotal;
    private Integer pagesTotal;
    private Boolean isLast;

}
