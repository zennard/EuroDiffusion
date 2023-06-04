package eurodiffusion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static eurodiffusion.util.Util.safeStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    @Builder.Default
    private List<ResultPart> resultParts = new ArrayList<>();

    @Override
    public String toString() {
        return safeStream(resultParts)
                .sorted()
                .collect(StringBuilder::new,
                        (builder, part) -> builder.append(part).append(System.getProperty("line.separator")),
                        StringBuilder::append)
                .toString();
    }

    public void addResultPart(ResultPart part) {
        resultParts.add(part);
    }
}
