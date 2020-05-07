package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UserLab.USER_LOGIN_SUCCESS:
                    loginSucess();
                    break;
                case UserLab.USER_LOGIN_FAIL:
                    loginFail();
                    break;
                case UserLab.USER_LOGIN_PASSWORD_ERROR:
                    loginPasswordError();
                    break;
            }
        }
    };


    private TextInputLayout username, password;
    private Button loginButton;
    private UserLab lab = UserLab.getInstance();

    private void loginSucess() {
        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void loginPasswordError() {
        Toast.makeText(LoginActivity.this, "密码错误，请重试！", Toast.LENGTH_LONG).show();
    }
    private void loginFail() {
        Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String u = username.getEditText().getText().toString();
            String p = password.getEditText().getText().toString();
            lab.login(u, p, handler);
        });
    }
}
