package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelVPAdatper(fragment: Fragment) : FragmentStateAdapter(fragment){

    // 이 클래스 안에서만 사용할 것이기 때문에 private로 선언함
    private  val fragmentList : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment) // fragmentList의 동작값으로 받은 fragment를 추가하는 작업
        notifyItemInserted(fragmentList.size-1) // 새로운 값이 리스트에 추가되는 것 (인덱스는 0부터!)
    }
}