package com.example.gachon_club.Club

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import org.jetbrains.anko.startActivity
import com.example.gachon_club.R
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gachon_club.Club.Adapter.BoardRecyclerAdapter
import com.example.gachon_club.Club.Model.Board
import com.example.gachon_club.Club.Model.Club
import com.example.gachon_club.Network.ServiceControl
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_club_info.*
import kotlinx.android.synthetic.main.fragment_notice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClubInfoActivity : AppCompatActivity() {

    private var fragmentPagerAdapter: FragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_info)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(NoticeFragment(), "공지사항")
        adapter.addFragment(CalendarFragment(), "일정")
        viewPager.adapter = adapter
        tab_layout.setupWithViewPager(viewPager)
        tab_layout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null)
                    btn_notice_edit.hide()
                else {
                    if (tab.position == 0)
                        btn_notice_edit.show()
                    else
                        btn_notice_edit.hide()
                }
            }
        })
        loadClub(intent.getLongExtra("id", 0))

    }

    class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){

        private  val fragmentList : MutableList<Fragment> = ArrayList()
        private  val titleList : MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment, title:String){
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

    }

    private fun setAdapter(boardList: ArrayList<Board>){
        val mAdapter = BoardRecyclerAdapter(boardList,this) { it ->
            startActivity<ClubNotice>(
                "id" to it._id
            )
        }
        board_recyler_view.adapter = mAdapter
        board_recyler_view.layoutManager = LinearLayoutManager(this)
    }
    private fun loadClub(id: Long) {
        val retrofitService = ServiceControl.getInstance()
        retrofitService?.getClub(id)?.enqueue(object: Callback<Club> {
            override fun onResponse(call: Call<Club>, response: Response<Club>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    loadBoards(body!!.name)
                    btn_notice_edit.setOnClickListener {
                        startActivity<EditNotice>("club" to body?.name)
                    }
                }
            }
            override fun onFailure(call: Call<Club>, t: Throwable) {
                Log.d("this is error",t.message)
            }
        })
    }
    private fun loadBoards(club: String) {
        val retrofitService = ServiceControl.getInstance()
        retrofitService?.getAllBoards(club)?.enqueue(object: Callback<List<Board>> {
            override fun onResponse(call: Call<List<Board>>, response: Response<List<Board>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        setAdapter(it as ArrayList<Board>)
                    }
                }
            }
            override fun onFailure(call: Call<List<Board>>, t: Throwable) {
                Log.d("this is error",t.message)
            }
        })
    }
}