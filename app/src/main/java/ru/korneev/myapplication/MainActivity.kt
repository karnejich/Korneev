package ru.korneev.myapplication

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import okhttp3.internal.wait
import ru.korneev.myapplication.databinding.ActivityMainBinding
import ru.korneev.myapplication.repository.Repository
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    var count = 0
    var cache = ArrayList<Resp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var temp = Resp("","")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()


        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (count==0){
            viewModel.getPost()
            viewModel.myRespone.observe(this, Observer { responce ->
                Glide.with(this)
                        .load(responce.gifURL.toString())
                        .fitCenter()
                        .placeholder(circularProgressDrawable)
                        .listener(object : RequestListener<Drawable>{
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                binding.imageView.setImageResource(R.drawable.no_img)
                                binding.description.text = "Error connection"
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                        })
                        .error(R.drawable.no_img)
                        .into(binding.imageView).waitForLayout()

                binding.description.text = responce.description
                temp = Resp(responce.description.toString(), responce.gifURL.toString())
                cache.add(count, temp)
                count++
            })
        }

        binding.nextBut.setOnClickListener{
            if (count > 0) {
                binding.backBut.setImageResource(R.drawable.back_active)
            }

            if (count>=cache.size){

                viewModel.getPost()

            }else {
                var path = cache[count].gifURL
                Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageView)
                binding.description.text = cache[count].description
                count++
            }


        }

        binding.backBut.setOnClickListener{
            if (count > 1){
                var path = cache[count-2].gifURL
                Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageView).waitForLayout()
                binding.description.text = cache[count-2].description
                count--
            }
            if (count==1){
                binding.backBut.setImageResource(R.drawable.back_inactive)
            }
        }
    }
}


//.listener(object : RequestListener<Drawable> {
//    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//        binding.description.text = responce.description
//        temp = Resp(responce.description.toString(), responce.gifURL.toString())
//        cache.add(count, temp)
//        count++
//        return false
//    }
//
//})