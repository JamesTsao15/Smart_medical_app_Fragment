package com.example.smart_medical_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var editText_email: EditText
    private lateinit var editText_password: EditText
    private lateinit var btn_Login:Button
    private lateinit var btn_Register:Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: FirebaseUser
    private val emailPatterns:Regex= Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editText_email=findViewById(R.id.editTextEmailAddress)
        editText_password=findViewById(R.id.editTextPassword)
        btn_Login=findViewById(R.id.Button_login)
        btn_Register=findViewById(R.id.Button_Register)
        progressDialog=ProgressDialog(this)
        mAuth= FirebaseAuth.getInstance()
        val user:FirebaseUser ?= FirebaseAuth.getInstance().currentUser
        if(user!=null){
            val intent=Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        else{
            Log.e("JAMES","User is signout")
        }
    }

    override fun onResume() {
        super.onResume()
        btn_Register.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        btn_Login.setOnClickListener {
            PerforLogin()
        }
    }

    private fun PerforLogin() {
        val email:String=editText_email.text.toString()
        val password:String=editText_password.text.toString()

        if (!email.matches(emailPatterns)){
            editText_email.setError("輸入email格是錯誤")
        }
        else if(password.isEmpty() || password.length<6){
            editText_password.setError("密碼輸入為空或低於6個字元")
        }
        else {
            progressDialog.setMessage("正在登入中，請稍後...")
            progressDialog.setTitle("登入")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                task->
                if(task.isSuccessful){
                    mUser=mAuth.currentUser!!
                    progressDialog.dismiss()
                    Toast.makeText(this,"${mUser.email}登入成功", Toast.LENGTH_SHORT).show()
                    sendUserToMainActivity()
                }
                else{
                    Toast.makeText(this,"登入失敗!!${task.exception}",Toast.LENGTH_SHORT).show()
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