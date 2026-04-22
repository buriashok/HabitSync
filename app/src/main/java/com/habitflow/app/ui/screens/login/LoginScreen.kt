package com.habitflow.newapp.ui.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.habitflow.newapp.R
import com.habitflow.newapp.ui.theme.*
import com.habitflow.newapp.viewmodel.HabitViewModel

@Composable
fun LoginScreen(
    viewModel: HabitViewModel,
    onLoginSuccess: () -> Unit = {}
) {
    val colors = AppTheme.colors
    val context = LocalContext.current
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val error = viewModel.authError
    val isLoading = viewModel.authLoading

    LaunchedEffect(viewModel.isLoggedIn) {
        if (viewModel.isLoggedIn) {
            onLoginSuccess()
        }
    }
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { token ->
                    viewModel.signInWithGoogleToken(token)
                }
            } catch (e: ApiException) {
                viewModel.authError = "Google sign-in failed: ${e.statusCode}"
            }
        }
    }

    fun launchGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("200245445482-hhbhho7p141b3qtpc2pi1j76addnp9bo.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(context, gso)
        client.signOut() // Clear any previous session
        googleSignInLauncher.launch(client.signInIntent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .offset(x = (-50).dp, y = (-100).dp)
                .blur(60.dp)
                .clip(CircleShape)
                .background(Indigo500.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .size(500.dp)
                .offset(x = 100.dp, y = 200.dp)
                .blur(60.dp)
                .clip(CircleShape)
                .background(Purple500.copy(alpha = 0.06f))
        )

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(24.dp))
                .background(colors.bgCard.copy(alpha = 0.9f))
                .border(1.dp, colors.borderColor, RoundedCornerShape(24.dp))
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎯", fontSize = 48.sp, modifier = Modifier.padding(bottom = 8.dp))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp,
                fontWeight = FontWeight.W800,
                color = Indigo500,
            )

            Text(
                text = if (isSignUp) "Create your account to start building habits"
                else "Welcome back! Continue your streak",
                fontSize = 14.sp,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )


            if (error.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Red500.copy(alpha = 0.1f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Error, null, tint = Red500, modifier = Modifier.size(16.dp))
                    Text(error, fontSize = 13.sp, color = Red500, lineHeight = 18.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = { launchGoogleSignIn() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                Text("🔵", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", fontSize = 14.sp, color = colors.textPrimary, fontWeight = FontWeight.W500)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    viewModel.authError = "Apple Sign-In requires setup in Firebase Console (Authentication → Sign-in method → Apple)"
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                Text("🍎", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Apple", fontSize = 14.sp, color = colors.textPrimary, fontWeight = FontWeight.W500)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = colors.borderColor)
                Text("  or continue with email  ", fontSize = 12.sp, color = colors.textMuted)
                HorizontalDivider(modifier = Modifier.weight(1f), color = colors.borderColor)
            }


            if (isSignUp) {
                Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Your name", fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Person, null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    enabled = !isLoading,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }


            Text("Email", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary,
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("you@example.com", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Outlined.Email, null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            Text("Password", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary,
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Min 6 characters", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Outlined.Lock, null, modifier = Modifier.size(16.dp)) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            null, modifier = Modifier.size(16.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = !isLoading,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) return@Button
                    if (isSignUp) {
                        viewModel.signUpWithEmail(email.trim(), password)
                    } else {
                        viewModel.signInWithEmail(email.trim(), password)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Indigo500),
                enabled = !isLoading && email.isNotBlank() && password.length >= 6
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (isSignUp) "Create Account" else "Sign In",
                        fontWeight = FontWeight.W600,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isSignUp) "Already have an account? " else "Don't have an account? ",
                    fontSize = 13.sp,
                    color = colors.textSecondary
                )
                Text(
                    text = if (isSignUp) "Sign In" else "Sign Up",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W600,
                    color = Indigo500,
                    modifier = Modifier.clickable {
                        isSignUp = !isSignUp
                        viewModel.authError = ""
                    }
                )
            }
        }
    }
}
