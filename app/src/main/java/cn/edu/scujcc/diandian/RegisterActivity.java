package cn.edu.scujcc.diandian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "DianDian";
    Button registerButton;
    TextInputLayout birthdayInput;
    private UserLab lab = UserLab.getInstance();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //新建一个Builder
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        //告诉builder我们想要的效果
        builder.setTitleText(R.string.birthday_title);
        //告诉builder，开工
        MaterialDatePicker picker = builder.build();
        picker.addOnPositiveButtonClickListener(s -> {
            String date = picker.getHeaderText();
            birthdayInput.getEditText().setText(date);
        });

        //点击图标，弹出日历
        birthdayInput = findViewById(R.id.r_birthday);
        birthdayInput.setEndIconOnClickListener(v -> {
            Log.d(TAG, "生日图标被点击了。");
            picker.show(getSupportFragmentManager(), picker.toString());
        });

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            User u = new User();
            //TODO 填充u对象的内容。
            lab.register(u, handler);
        });
    }
}
