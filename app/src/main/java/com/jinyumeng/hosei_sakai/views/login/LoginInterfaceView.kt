package com.jinyumeng.hosei_sakai.views.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinyumeng.hosei_sakai.BuildConfig
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme

@Composable
fun LoginInterfaceView(
    username: String,
    password: String,
    error: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    loginAction: () -> Unit,
) {
    @Composable
    fun LoginTitle(modifier: Modifier = Modifier) {
        Column(modifier) {
            Text(
                text = "ようこそ",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = "Hoppii アカウントでログインしてください。",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }

    @Composable
    fun LoginInput(modifier: Modifier = Modifier) {
        val focusManager = LocalFocusManager.current

        Column(modifier){
            OutlinedTextField(value = username,
                onValueChange = {
                    if (!it.contains("\n"))
                        onUsernameChange(it)
                },
                label = {
                    Text("学籍番号")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    if (!it.contains("\n"))
                        onPasswordChange(it)
                },
                label = {
                    Text("パスワード")
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        loginAction()
                    }
                )
            )
            if (error) {
                Text(
                    text = "ログイン情報が違います。",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            } else {
                Text(
                    text = "ログイン情報は安全に保存されます。",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.alpha(if (BuildConfig.DEBUG) 0.8f else 1f)
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .imePadding()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            LoginTitle(modifier = Modifier.padding(bottom = 20.dp))
            LoginInput(modifier = Modifier.padding(bottom = 20.dp))
            Button(onClick = loginAction) {
                Text("ログイン")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginInterfaceViewPreview() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    HoseiSakaiTheme {
        LoginInterfaceView(
            username = username,
            password = password,
            error = false,
            onUsernameChange = { username = it },
            onPasswordChange = { password = it },
            loginAction = {}
        )
    }
}