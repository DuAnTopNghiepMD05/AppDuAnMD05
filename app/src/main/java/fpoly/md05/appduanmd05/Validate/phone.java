package fpoly.md05.appduanmd05.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class phone {
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(0[0-9]{9,10})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }
}
