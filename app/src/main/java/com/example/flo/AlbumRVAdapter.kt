package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter (private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    // 외부에서 click event를 사용할 수 있도록 interface!
    interface MyItemClickListner{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    // 외부에서 함수와 리스너 객체를 사용할 수 있도록 받아와야 함
    private lateinit var mItemClickListener: MyItemClickListner
    fun setMyItemClickListener(itemClickListner: MyItemClickListner){
        mItemClickListener = itemClickListner
    }

    // 아이템 추가
    fun addItem(album: Album){
        albumList.add(album)

        // ** 중요 **
        // 리사이클러뷰 어댑터는 데이터가 바뀐지 모름
        // 따라서 바뀌었다고 반드시 알려줘야 함!!
        notifyDataSetChanged()
    }

    // 아이템 삭제
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // 데이터 바인딩 작업
    /* 클릭 이벤트 처리
    왜 여기서?
    onBindViewHolder가 position값을 갖고있기 때문에
    주로 클릭 이벤트는 이곳에서 처리함!
     */
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])

        /*
        그냥 setOnClickListener 안에 원하는 동작 구현 시?
        어댑터 안에서만 유효한 방법임!
        외부에서 아이템 클릭 이벤트 처리하고 싶을 때 사용 불가능

        따라서 외부에서도 사용가능하도록 클릭 리스너 인터페이스를 만들어줘야 함
        */

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }


        // 재생버튼
        //holder.binding.itemAlbumBtnPlayIv.setOnClickListener{mItemClickListener.onRemoveDownloadSong(position)}

        // 삭제기능
        // holder.binding.itemAlbumTitleTv.setOnClickListener{mItemClickListener.onRemoveAlbum(position)}
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}