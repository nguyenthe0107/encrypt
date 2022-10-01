package com.nguyenthe.encryption

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.nguyenthe.encryption.data.UserInfo
import com.nguyenthe.encryption.ui.theme.EncryptionTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cryptoManager = CryptoManager()
        val dataStore = DataStoreUtils(this)
        setContent {
            EncryptionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scope = rememberCoroutineScope()
                    Greeting("Android")
                    var messageToEncrypt by remember {
                        mutableStateOf("")
                    }
                    var messageToDecrypt by remember {
                        mutableStateOf("")
                    }
                    Column(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ){
                        TextField(
                            value = messageToEncrypt,
                            onValueChange = { messageToEncrypt = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Encrypt string") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = {
                                val bytes = messageToEncrypt.encodeToByteArray()
                                val userLocal = UserInfo(name = "vanthe1", age = 30, address = "Ho Chi Minh1")
                                val userInfo = Gson().toJson(userLocal).toString().encodeToByteArray()
                                val file = File(filesDir, "secret.txt")
                                if(!file.exists()) {
                                    file.createNewFile()
                                }
                                val fos = FileOutputStream(file)
                                messageToDecrypt = cryptoManager.encrypt(
                                    bytes = userInfo,
                                    outputStream = fos
                                ).decodeToString()
                            }) {
                                Text(text = "Encrypt")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = {
                                val file = File(filesDir, "secret.txt")
                                messageToEncrypt = cryptoManager.decrypt(
                                    inputStream = FileInputStream(file)
                                ).decodeToString()
                            }) {
                                Text(text = "Decrypt")
                            }
                        }
                        Text(text = messageToDecrypt)
                        val userLocal = UserInfo(name = "vanthe1", age = 30, address = "Ho Chi Minh1")
                        Button(onClick = {
                            scope.launch {
                                dataStore.saveUserInfo(userLocal)
                            }
                        }) {
                            Text(text = "Save Local")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {
                            scope.launch {
                                dataStore.getUserInfo().collectLatest {
                                    Log.e("WTF", " getUserInfo() $it")
                                }
                            }
                        }) {
                            Text(text = "Get Local")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EncryptionTheme {
        Greeting("Android")
    }
}