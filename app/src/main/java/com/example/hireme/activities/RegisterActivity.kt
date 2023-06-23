package com.example.hireme.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.hireme.R
import com.example.hireme.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView


@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

    //auth
        auth  = FirebaseAuth.getInstance()


        //fullname validation
        val nameStream = RxTextView.textChanges(binding.etFullname)
            .skipInitialValue()
            .map { name->
                name.isEmpty()
            }
        nameStream.subscribe{
            showNameExistAlert(it)
        }

        //Email validation
        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            shoEmailValidAlert(it)
        }

        //username Validation
        val usernameStream = RxTextView.textChanges(binding.etUsername)
            .skipInitialValue()
            .map { username ->
                username.length <6
            }
        usernameStream.subscribe{
            showTextMinimaAlert(it, "Username")
        }

        //password validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.length <6
            }
        passwordStream.subscribe{
            showTextMinimaAlert(it, "Password")
        }

        //click
        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            registerUser(email, password)
        }
        binding.tvHaveAccount.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun showNameExistAlert(isNotValid: Boolean){
        binding.etFullname.error = if(isNotValid) "Name is not valid" else null

    }

    private fun showTextMinimaAlert(isNotValid: Boolean, text: String){
        if(text== "Username")
            binding.etUsername.error = if(isNotValid) "$text lenth is not valid" else null

        else if(text == "Password")
            binding.etPassword.error = if(isNotValid) "$text is over 8" else null

    }

    private  fun shoEmailValidAlert(isNotValid: Boolean){
        binding.etEmail.error = if (isNotValid) "Email tidal valid!" else null

    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.etConfirmPassword.error = if(isNotValid) "password is not same!" else null
    }

    private fun registerUser(email:String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Register Successfully!", Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(this, it.exception?.message , Toast.LENGTH_SHORT).show()
                }
            }
    }
}