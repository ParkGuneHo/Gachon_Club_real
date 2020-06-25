package com.example.gachon_club.Account

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.gachon_club.Account.Model.User
import com.example.gachon_club.Club.Model.Club
import com.example.gachon_club.Network.ServiceControl
import com.example.gachon_club.R
import kotlinx.android.synthetic.main.activity_modified.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifiedActivity : AppCompatActivity() {

    var list_of_club = arrayListOf<String>("선택")
    var list_of_position = arrayOf("동아리 회장", "동아리 부회장", "동아리 부윈")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modified)

        loadData("중앙동아리")
        edit_club.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list_of_club)
        edit_position.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list_of_position)

        val user:User = intent.getParcelableExtra<User>("userInfo")
        edit_username.setText(user.userId)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        btn_modify.setOnClickListener{

            val PW = edit_password.text.toString()
            val NAME = edit_name.text.toString()
            val MAJOR = edit_major.text.toString()
            val CLUB = edit_club.selectedItem.toString()
            val POSITION = edit_position.selectedItem.toString()

            if(user.userPw == PW) {
                if ((!NAME.isNullOrBlank()) && (!MAJOR.isNullOrBlank()) && (CLUB != "선택") && (!POSITION.isNullOrBlank())) {
                    val user = User(
                        edit_username.text.toString(),
                        edit_password.text.toString(),
                        edit_name.text.toString(),
                        edit_major.text.toString(),
                        edit_club.selectedItem.toString(),
                        edit_position.selectedItem.toString()
                    )
                    modifyData(user)
                } else {
                    Toast.makeText(this, "빠짐없이 입력해주세요", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun loadData(head: String) {
        val retrofitService = ServiceControl.getInstance()
        retrofitService?.getAllClubs(head)?.enqueue(object: Callback<List<Club>> {
            override fun onResponse(call: Call<List<Club>>, response: Response<List<Club>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        for(i in body){
                            list_of_club.add(i.name)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Club>>, t: Throwable) {
            }
        })
    }
    private fun modifyData(user: User) {
        val retrofitService = ServiceControl.getInstance()
        retrofitService?.modifyUser(user)?.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        Toast.makeText(applicationContext, "회원정보 수정", Toast.LENGTH_LONG).show()
                        val intent = Intent()
                        intent.putExtra("userInfo", it)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "ID가 틀렸어요", Toast.LENGTH_LONG).show()
            }
        })
    }
}
