package com.example.githubuser.ui.theme

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Responses(
	val login: String,
	val avatar_url: String,
	val name : String
)

interface GitHubApiService {
	@GET("users")
	suspend fun getGitHubUsers(): Response<List<Responses>>
}

object GitHubApi {
	private const val BASE_URL = "https://api.github.com/"

	private val retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.client(
			OkHttpClient.Builder().addInterceptor { chain ->
				val request = chain.request().newBuilder()
					.header("Authorization", "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")
					.build()
				chain.proceed(request)
			}.build()
		)
		.build()

	val service: GitHubApiService by lazy {
		retrofit.create(GitHubApiService::class.java)
	}
}

suspend fun fetchGitHubUsers(): List<User> {
	val response = GitHubApi.service.getGitHubUsers()
	if (response.isSuccessful) {
		val userList = response.body()
		return userList?.map { userResponse ->
			User(userResponse.login, userResponse.avatar_url)
		} ?: emptyList()
	} else {
		throw Exception("Failed to fetch GitHub users")
	}
}
