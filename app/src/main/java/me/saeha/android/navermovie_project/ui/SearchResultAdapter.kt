package me.saeha.android.navermovie_project.ui

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.saeha.android.navermovie_project.R
import me.saeha.android.navermovie_project.databinding.ItemMovieBinding
import me.saeha.android.navermovie_project.model.MovieData
import me.saeha.android.navermovie_project.network.RxBus
import me.saeha.android.navermovie_project.network.RxEvents

class SearchResultAdapter(
    val context: Context,
    var searchResultList: MutableList<MovieData>
) :
    RecyclerView.Adapter<SearchResultAdapter.ItemViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //viewbinding - item 레이아웃과
        val templateDetailBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(templateDetailBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = searchResultList[position]
        holder.onBind(context, item)


        holder.itemView.setOnClickListener{
            val intent = Intent(context, MovieDetailInfoActivity::class.java)
            intent.putExtra("movieObject", item)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = searchResultList.size

    class ItemViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        private val compositeDisposable = CompositeDisposable()
        private val ivbFavorite = binding.ivbFavorite
        private val ivPoster = binding.ivPoster
        private val tvTitle = binding.tvTitle
        private val tvDirector = binding.tvDirector
        private val tvActor = binding.tvActor
        private val tvUserRating = binding.tvUserRating

        fun onBind(context: Context, item: MovieData) {
            //영화 포스터 이미지 set
            Glide.with(context)
                .load(item.image)
                .override(190,250)
                .centerCrop()
                .error(R.drawable.ic_no_pictures_black_512)
                .into(ivPoster)

            tvTitle.text= item.title
            tvDirector.text= context.getString(R.string.director,item.director)
            tvActor.text= context.getString(R.string.actor,item.actor)
            tvUserRating.text= context.getString(R.string.user_rating,item.userRating)
            if(item.favorite){
                ivbFavorite.setBackgroundResource(R.drawable.ic_star_yellow_24)
            }else{
                ivbFavorite.setBackgroundResource(R.drawable.ic_star_gray_24)
            }

            //즐겨찾기 별 버튼
            ivbFavorite.setOnClickListener{
                Log.d("즐겨찾기 값 확인1",item.favorite.toString())
                item.favorite = !item.favorite
                Log.d("즐겨찾기 값 확인2",item.favorite.toString())
                RxBus.publish(RxEvents.EventFavoriteOfMainList(item))
            }
        }
    }
}
