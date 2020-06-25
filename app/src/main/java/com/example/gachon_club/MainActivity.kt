package com.example.gachon_club

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.gachon_club.Account.Model.User
import com.example.gachon_club.Account.ModifiedActivity
import com.example.gachon_club.Account.SignInActivity
import com.example.gachon_club.Account.SignUpActivity
import com.example.gachon_club.Club.ClubActivity
import com.example.gachon_club.Network.ServiceControl
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    var user:User ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        Main_club.setOnClickListener{
            startActivity<ClubActivity>("head" to "중앙동아리")
        }

        Sub_club.setOnClickListener{
            startActivity<ClubActivity>("head" to "과동아리")
        }

        Information.setOnClickListener{
            drawer_layout.openDrawer(GravityCompat.START)
        }

        Sign_in.setOnClickListener{
            if(user != null){
                user = null
                userName.text = "로그인"
                userMajor.text = "해야"
                userClub.text = "볼수"
                userPosition.text = "있어요"
                Toast.makeText(applicationContext, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
                Sign_in.setText("로그인")
            }
            else {
                val intent = Intent(this, SignInActivity::class.java)
                startActivityForResult(intent, 100)
            }
        }
        Sign_up.setOnClickListener{
            startActivity<SignUpActivity>()
        }
    }

    override fun onBackPressed(){
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawers()
        }
        else{
            super.onBackPressed()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    user = data!!.getParcelableExtra<User>("userInfo")

                    userName.text = user?.name
                    userMajor.text = user?.major
                    userClub.text = user?.club
                    userPosition.text = user?.position
                    Sign_in.setText("로그아웃")
                }
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.nav_modified){
            if(user != null) {
                val intent = Intent(this, ModifiedActivity::class.java)
                intent.putExtra("userInfo", user)
                startActivityForResult(intent, 100)
            }
            else{
                Toast.makeText(this, "로그인해주세요", Toast.LENGTH_LONG).show()
            }
        }else if(id == R.id.nav_club){
            if(user != null){
                val retrofitService = ServiceControl.getInstance()
                retrofitService?.deleteUser(user!!.userId)?.enqueue(object: Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "회원탈퇴되었습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    }
                })
                user = null
                userName.text = "로그인"
                userMajor.text = "해야"
                userClub.text = "볼수"
                userPosition.text = "있어요"
                Sign_in.setText("로그인")
            }
            else{
                Toast.makeText(this, "로그인해주세요", Toast.LENGTH_LONG).show()
            }
        }
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.closeDrawers()
        return true
    }
}
