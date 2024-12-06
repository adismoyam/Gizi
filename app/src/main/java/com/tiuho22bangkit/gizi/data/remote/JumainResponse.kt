package com.tiuho22bangkit.gizi.data.remote

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class JumainResponse(

	@field:SerializedName("JumainResponse")
	val jumainResponse: List<JumainResponseItem> = listOf()
) : Parcelable

@Parcelize
data class JumainResponseItem(

	@field:SerializedName("publishedAt")
	val publishedAt: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("urlToImage")
	val urlToImage: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("source")
	val source: Source,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("content")
	val content: String
) : Parcelable

@Parcelize
data class Source(

	@field:SerializedName("name")
	val name: String
) : Parcelable
