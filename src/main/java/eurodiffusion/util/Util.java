package eurodiffusion.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@UtilityClass
public class Util {

    public static <T> Stream<T> safeStream(List<T> items) {
        return Optional.of(items)
                .stream()
                .flatMap(Collection::stream);
    }

}
