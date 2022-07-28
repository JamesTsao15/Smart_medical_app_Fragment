package com.example.smart_medical_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    private lateinit var editText_registerEmailAddress: EditText
    private lateinit var editText_registerPassword: EditText
    private lateinit var editText_doubleCheckPassword: EditText
    private lateinit var btn_registerAccount:Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mUser:FirebaseUser
    private val emailPatterns:Regex= Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        editText_registerEmailAddress=findViewById(R.id.editTextRegisterEmailAddress)
        editText_registerPassword=findViewById(R.id.editTextRegisterPassword)
        editText_doubleCheckPassword=findViewById(R.id.editTextCheckPassword)
        btn_registerAccount=findViewById(R.id.Button_RegisterAccount)
        progressDialog=ProgressDialog(this)
        mAuth= FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        btn_registerAccount.setOnClickListener {
            PerforAuth()
        }
    }

    private fun PerforAuth() {
        val email:String=editText_registerEmailAddress.text.toString()
        val password:String=editText_registerPassword.text.toString()
        val confirmPassword=editText_doubleCheckPassword.text.toString()
        if (!email.matches(emailPatterns)){
            editText_registerEmailAddress.setError("輸入email格是錯誤")
        }
        else if(password.isEmpty() || password.length<6){
            editText_registerPassword.setError("密碼輸入為空或低於6個字元")
        }
        else if(!password.equals(confirmPassword)){
            editText_doubleCheckPassword.setError("密碼不相同")
        }
        else{
            progressDialog.setMessage("正在註冊中，請稍後...")
            progressDialog.setTitle("註冊")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                task->
                if(task.isSuccessful){
                    mUser=mAuth.currentUser!!
                    progressDialog.dismiss()
                    Toast.makeText(this,"${mUser.email}註冊成功",Toast.LENGTH_SHORT).show()
                    sendUserToMainActivity()
                }
                else{
                    Toast.makeText(this,"註冊失敗!!${task.exception}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendUserToMainActivity() {
        val intent:Intent= Intent(this,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
        startActivity(intent)
    }
}