package com.example

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignupActivity : AppCompatActivity() {
    // views
    private var tStatus: TextView? = null
    private var tDetail: TextView? = null
    private var eEmail: EditText? = null
    private var ePass: EditText? = null
    private var pLoading: ProgressBar? = null

    // Firebase
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        // views
        tStatus = findViewById<View>(R.id.tStatus) as TextView
        tDetail = findViewById<View>(R.id.tDetail) as TextView
        eEmail = findViewById<View>(R.id.eEmail) as EditText
        ePass = findViewById<View>(R.id.ePass) as EditText
        pLoading = findViewById<View>(R.id.pLoading) as ProgressBar
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = object : FirebaseAuth.AuthStateListener() {
            fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                val user: FirebaseUser = firebaseAuth.getCurrentUser()
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid())
                    setResult(RESULT_OK)
                    finish()
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out")
                }
                updateUI(user)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    public override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }
        showProgressDialog()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<Any?> { task ->
                    if (!task.isSuccessful) Toast.makeText(
                        applicationContext,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgressDialog()
                })
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }
        showProgressDialog()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<Any?> { task ->
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful)
                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithEmail:failed", task.exception)
                        Toast.makeText(
                            applicationContext,
                            "Authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (!task.isSuccessful) {
                        tStatus!!.text = "Authentication failed"
                    }
                    hideProgressDialog()
                })
    }

    private fun signOut() {
        mAuth.signOut()
        updateUI(null)
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = eEmail!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            eEmail!!.error = "Required."
            valid = false
        } else {
            eEmail!!.error = null
        }
        val password = ePass!!.text.toString()
        if (TextUtils.isEmpty(password)) {
            ePass!!.error = "Required."
            valid = false
        } else {
            ePass!!.error = null
        }
        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {
            tStatus!!.text = "Email user: " + user.getEmail()
            tDetail!!.text = "Firebase user: " + user.getUid()
            findViewById<View>(R.id.lSignIn).visibility = View.GONE
            findViewById<View>(R.id.email_password_fields).visibility = View.GONE
            findViewById<View>(R.id.bSignOut).visibility = View.VISIBLE
        } else {
            tStatus!!.text = "Signed out"
            tDetail.setText(null)
            findViewById<View>(R.id.lSignIn).visibility = View.VISIBLE
            findViewById<View>(R.id.email_password_fields).visibility = View.VISIBLE
            findViewById<View>(R.id.bSignOut).visibility = View.GONE
        }
    }

    fun clicked(v: View) {
        when (v.id) {
            R.id.bRegister -> createAccount(eEmail!!.text.toString(), ePass!!.text.toString())
            R.id.bSignIn -> signIn(eEmail!!.text.toString(), ePass!!.text.toString())
            R.id.bSignOut -> signOut()
        }
    }

    private fun showProgressDialog() {
        pLoading!!.visibility = View.VISIBLE
    }

    private fun hideProgressDialog() {
        pLoading!!.visibility = View.GONE
    }

    companion object {
        private const val TAG = "smartwallet-login"
    }
}