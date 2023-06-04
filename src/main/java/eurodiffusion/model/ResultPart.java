package eurodiffusion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultPart implements Comparable<ResultPart> {

    private Country country;
    private Integer daysToComplete;

    @Override
    public String toString() {
        return country.getName() + " " + daysToComplete;
    }

    @Override
    public int compareTo(ResultPart otherPart) {
        int sortingIndex = this.daysToComplete.compareTo(otherPart.getDaysToComplete());
        boolean hasSameDaysToComplete = sortingIndex == 0;
        if (hasSameDaysToComplete) {
            sortingIndex = this.getCountry().getName().compareTo(otherPart.getCountry().getName());
        }
        return sortingIndex;
    }
}
