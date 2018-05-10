import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Solution {

    private static final Map<Integer, String> DIGITS = new HashMap<>();

    static {
        DIGITS.put(1, "one");
        DIGITS.put(2, "two");
        DIGITS.put(3, "three");
        DIGITS.put(4, "four");
        DIGITS.put(5, "five");
        DIGITS.put(6, "six");
        DIGITS.put(7, "seven");
        DIGITS.put(8, "eight");
        DIGITS.put(9, "nine");
        DIGITS.put(10, "ten");
    }

    private static final class Converter {

        private static final Map<Integer, String> DOZENS = new HashMap<>();

        static {
            DOZENS.put(1, "eleven");
            DOZENS.put(2, "twelve");
            DOZENS.put(3, "thirteen");
            DOZENS.put(4, "fourteen");
            DOZENS.put(5, "fifteen");
            DOZENS.put(6, "sixteen");
            DOZENS.put(7, "seventeen");
            DOZENS.put(8, "eighteen");
            DOZENS.put(9, "nineteen");
        }

        private static final Map<Integer, Function<String, String>> CONVERTER = new HashMap();

        static {
            CONVERTER.put(0, Function.identity());
            CONVERTER.put(2, s -> "twenty " + s);
            CONVERTER.put(3, s -> "thirty " + s);
            CONVERTER.put(4, s -> "forty" + s);
            CONVERTER.put(5, s -> "fifty " + s);
        }

        public String convert(int n) {
            if (n > 10 && n < 20) {
                return DOZENS.get(n - 10 * (n / 10));
            }

            String res = DIGITS.get(n % 10);
            return CONVERTER.get(n / 10).apply(res);
        }
    }

    private static final Converter CONVERTER = new Converter();

    private static final List<BiFunction<Integer, Integer, String>> POSSIBLE = Arrays.asList(
            (h, m) -> m == 0 ? CONVERTER.convert(h) + " o' clock" : null,
            (h, m) -> m == 1 ? "one minute past " + CONVERTER.convert(h) : null,
            (h, m) -> (m > 1 && m < 10) || (m > 10 && m < 15) || (m > 15 && m < 30)
                    ? CONVERTER.convert(m) + " minutes past " + CONVERTER.convert(h)
                    : null,
            (h, m) -> m == 10 ? "ten minutes past " + CONVERTER.convert(h) : null,
            (h, m) -> m == 15 ? "quarter past " + CONVERTER.convert(h) : null,
            (h, m) -> m == 30 ? "half past " + CONVERTER.convert(h) : null,
            (h, m) -> (m > 30 && m < 45) || (m > 45 && m < 59)
                    ? CONVERTER.convert(60 - m) + " minutes to " + CONVERTER.convert(h + 1)
                    : null,
            (h, m) -> m == 59 ? "one minute to " + CONVERTER.convert(h + 1) : null,
            (h, m) -> m == 45 ? "quarter to " + CONVERTER.convert(h + 1) : null
    );

    private static String timeInWords(int h, int m) {
        return POSSIBLE.stream().map(fun -> fun.apply(h, m))
                .filter(Objects::nonNull)
                .findFirst()
                .get();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int h = in.nextInt();
        int m = in.nextInt();
        String result = timeInWords(h, m);
        System.out.println(result);
        in.close();
    }
}