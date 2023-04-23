package com.example.musicpie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musicpie.databinding.DetailSectionBinding

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: DetailSectionBinding

    private lateinit var backButton: Button
    private lateinit var coverDetail: ImageView

    private var covers: MutableList<Int> = mutableListOf(
        R.drawable.cover1,
        R.drawable.cover2,
        R.drawable.cover3
    )
    private var songs: MutableList<Int> = mutableListOf(
        R.raw.song1,
        R.raw.song2,
        R.raw.song3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.detail_section)

        backButton = binding.backButton
        coverDetail = binding.coverDetail

        detailSection()
    }

    private fun detailSection() {

        val position = intent.getIntExtra("position",0)

        coverDetail.setImageResource(covers[position])

        backButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("position", position)
            startActivity(intent)
        }
    }
}