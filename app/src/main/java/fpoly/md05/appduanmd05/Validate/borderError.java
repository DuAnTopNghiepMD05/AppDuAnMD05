package fpoly.md05.appduanmd05.Validate;

import android.widget.EditText;

import fpoly.md05.appduanmd05.R;

public class borderError {
    public static void resetErrorAndBorder(EditText editText) {
        editText.setError(null);
        editText.setBackgroundResource(R.drawable.edittext_default_border);
    }

    public static void setRedBorderAndError(EditText editText, String errorMessage) {
        editText.setBackgroundResource(R.drawable.edittext_red_border);
        editText.setError(errorMessage);
    }
}
