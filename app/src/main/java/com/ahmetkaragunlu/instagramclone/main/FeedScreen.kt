package com.ahmetkaragunlu.instagramclone.main


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahmetkaragunlu.instagramclone.DestinationScreen
import com.ahmetkaragunlu.instagramclone.IgViewModel
import com.ahmetkaragunlu.instagramclone.data.PostData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.ahmetkaragunlu.instagramclone.R


@Composable
fun FeedScreen(navController: NavController, vm: IgViewModel) {

    val userDataLoading = vm.inProgress.value
    val userData = vm.userData.value
    val personalizedFeed = vm.postsFeed.value
    val personalizedFeedLoading = vm.postsFeedProgress.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
        ) {
            UserImageCard(userImage = userData?.imageUrl)
        }
        PostsList(
            posts = personalizedFeed,
            modifier = Modifier.weight(1f),
            loading = personalizedFeedLoading or userDataLoading,
            navController = navController,
            vm = vm,
            currentUserId = userData?.userId ?: ""
        )

        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.FEED,
            navController = navController
        )
    }

}

@Composable
fun PostsList(
    posts: List<PostData>,
    modifier: Modifier,
    loading: Boolean,
    navController: NavController,
    vm: IgViewModel,
    currentUserId: String
) {
    Box(modifier = modifier) {
        LazyColumn {
            items(items = posts) {
                Post(post = it, currentUserId = currentUserId, vm) {
                    navigateTo(
                        navController,
                        DestinationScreen.SinglePost,
                        NavParam("post", it)
                    )
                }
            }
        }
        if (loading)
            CommonProgressSpinner()
    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Post(post: PostData, currentUserId: String, vm: IgViewModel, onPostClick: () -> Unit) {

    val likeAnimation = remember { mutableStateOf(false) }
    val dislikeAnimation = remember { mutableStateOf(false) }

    // Beğeni durumunu takip etmek için mutable state
    val isLiked = remember { mutableStateOf(post.likes?.contains(currentUserId) == true) }
    val likesCount = remember { mutableStateOf(post.likes?.size ?: 0) }

    LaunchedEffect(key1 = post.likes) {
        isLiked.value = post.likes?.contains(currentUserId) == true
        likesCount.value = post.likes?.size ?: 0
    }

    Card(
        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape, modifier = Modifier
                        .padding(4.dp)
                        .size(32.dp)
                ) {
                    CommonImage(data = post.userImage, contentScale = ContentScale.Crop)
                }
                Text(text = post.username ?: "", modifier = Modifier.padding(4.dp))
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (isLiked.value) {
                                    dislikeAnimation.value = true
                                    isLiked.value = false
                                    likesCount.value = (likesCount.value - 1).coerceAtLeast(0)
                                } else {
                                    likeAnimation.value = true
                                    isLiked.value = true
                                    likesCount.value = likesCount.value + 1
                                }
                                vm.onLikePost(post)
                                vm.refreshPosts()
                                vm.getPersonalizedFeed()
                            },
                            onTap = {
                                onPostClick.invoke()
                            }
                        )
                    }
                CommonImage(
                    data = post.postImage,
                    modifier = modifier,
                    contentScale = ContentScale.FillWidth
                )

                if (likeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000L)
                        likeAnimation.value = false
                    }
                    LikeAnimation()
                }
                if (dislikeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000L)
                        dislikeAnimation.value = false
                    }
                    LikeAnimation(false)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(
                        id = if (isLiked.value) R.drawable.ic_like else R.drawable.ic_dislike
                    ),
                    contentDescription = "Like",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            isLiked.value = !isLiked.value
                            if (isLiked.value) {
                                likesCount.value = likesCount.value + 1
                            } else {
                                likesCount.value = (likesCount.value - 1).coerceAtLeast(0)
                            }
                            vm.onLikePost(post)
                            vm.refreshPosts()
                            vm.getPersonalizedFeed()
                        },
                    colorFilter = ColorFilter.tint(
                        if (isLiked.value) Color.Red else Color.Black
                    )
                )

                Spacer(modifier = Modifier.width(16.dp))


                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "${likesCount.value} likes",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (!post.postDescription.isNullOrEmpty()) {
                Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                    Text(
                        text = post.username ?: "",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = post.postDescription ?: "")
                }
            }
        }
    }
}