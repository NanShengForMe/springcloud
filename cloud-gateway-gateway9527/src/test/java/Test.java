import java.time.ZonedDateTime;

/**
 * test
 *
 * @author zoujidi
 * @version 1.0.0
 * @date 2020年08月04日 16:26:00
 */
public class Test {

    public static void main(String[] args) {
        // 获取上海时区
        ZonedDateTime zbj = ZonedDateTime.now();
        System.out.println(zbj);
    }
}
