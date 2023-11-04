package com.example.githubuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.githubuser.ui.theme.User
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.githubuser.R.drawable.ic_github
import com.example.githubuser.ui.theme.fetchGitHubUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				GitHubUserList()
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubUserList(modifier: Modifier = Modifier) {
	var githubUsers by remember { mutableStateOf(emptyList<User>()) }

	val coroutineScope = rememberCoroutineScope()

	// Retrieve GitHub users from API
	runBlocking {
		coroutineScope.launch(Dispatchers.IO) {
			val response = fetchGitHubUsers()
			githubUsers = response
		}
	}

	Scaffold (topBar = { GithubTopAppBar()}){it ->
		LazyColumn(contentPadding = it,
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {

			items(githubUsers) {
				GitHubUserCard(user = it,  )
			}
	}
	}
}

@Composable
fun GitHubUserCard(user: User) {
	Card (modifier = Modifier) {
		Column(
			modifier = Modifier
				.animateContentSize(
					animationSpec = spring(
						dampingRatio = Spring.DampingRatioNoBouncy,
						stiffness = Spring.StiffnessMedium
					)
				)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()

			) {
				DogIcon(user.avatarUrl)
				DogInformation(user.username)
				Spacer(Modifier.weight(1f))

			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubTopAppBar(modifier: Modifier = Modifier) {
	CenterAlignedTopAppBar(
		title = {
			Row(
				verticalAlignment = CenterVertically
			) {

				Image(modifier = Modifier
					.size(150.dp)
					,painter = painterResource(id = ic_github), contentDescription = null)
			}
		},
		modifier = modifier
	)
}

@Composable
fun DogIcon(
	avatar: String,
	modifier: Modifier = Modifier
) {
	Image(
		modifier = modifier
			.size(100.dp)
			.padding(16.dp)
			.clip(MaterialTheme.shapes.small),
		contentScale = ContentScale.Crop,
		painter = rememberAsyncImagePainter(avatar),

		// Content Description is not needed here - image is decorative, and setting a null content
		// description allows accessibility services to skip this element during navigation.

		contentDescription = null
	)
}

@Composable
fun DogInformation(
	username: String,

) {
	Column(modifier = Modifier) {
		Text(
			text = username,
			style = MaterialTheme.typography.titleMedium

		)
	}
}


@Preview
@Composable
fun GitHubUserListPreview() {
	GitHubUserList()
}




