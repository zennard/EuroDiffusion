package eurodiffusion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Problem {

    private Integer id;
    private List<Country> countries;

}
