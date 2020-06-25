package com.example.gachon_club.Club

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gachon_club.Club.Adapter.RecyclerAdapter
import com.example.gachon_club.Club.Model.Club
import com.example.gachon_club.Network.ServiceControl
import com.example.gachon_club.R
import kotlinx.android.synthetic.main.activity_club.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClubActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club)

        loadData(intent.getStringExtra("head"))
    }

    private fun setAdapter(clubList: ArrayList<Club>){
        val mAdapter = RecyclerAdapter(clubList,this) { club ->
            startActivity<ClubInfoActivity>(
                "id" to club._id
            )
        }
        recyler_view.adapter = mAdapter
        recyler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadData(head: String) {
        val retrofitService = ServiceControl.getInstance()
        retrofitService?.getAllClubs(head)?.enqueue(object: Callback<List<Club>> {
            override fun onResponse(call: Call<List<Club>>, response: Response<List<Club>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        setAdapter(it as ArrayList<Club>)
                    }
                }
            }
            override fun onFailure(call: Call<List<Club>>, t: Throwable) {
                Log.d("this is error",t.message)
            }
        })
    }
}