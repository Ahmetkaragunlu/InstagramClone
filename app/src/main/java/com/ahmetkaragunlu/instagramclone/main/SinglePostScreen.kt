package com.ahmetkaragunlu.instagramclone.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ahmetkaragunlu.instagramclone.DestinationScreen
import com.ahmetkaragunlu.instagramclone.IgViewModel
import com.ahmetkaragunlu.instagramclone.data.PostData
import com.ahmetkaragunlu.instagramclone.R



@Composable
fun SinglePostScreen(navController: NavController, vm: IgViewModel, post: PostData) {

    val comments = vm.comments.value

    LaunchedEffect(key1 = Unit) {
        vm.getComments(post.postId)
    }

    post.userId?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            Text(text = "Back", modifier = Modifier.clickable { navController.popBackStack() })

            CommonDivider()

            SinglePostDisplay(
                navController = navController,
                vm = vm,
                post = post,
                nbComments = comments.size
            )
        }
    }
}
@Composable
fun SinglePostDisplay(
    navController: NavController,
    vm: IgViewModel,
    post: PostData,
    nbComments: Int
) {
    val userData = vm.userData.value
    val isLiked = remember { mutableStateOf(post.likes?.contains(userData?.userId) == true) }
    val likesCount = remember { mutableStateOf(post.likes?.size ?: 0) }

    LaunchedEffect(key1 = post.likes) {
        isLiked.value = post.likes?.contains(userData?.userId) == true
        likesCount.value = post.likes?.size ?: 0
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = post.userImage),
                    contentDescription = null
                )
            }

            Text(text = post.username ?: "")
            Text(text = ".", modifier = Modifier.padding(8.dp))

            if (userData?.userId == post.userId) {
                // Current user's post. Don't show anything
            } else if (userData?.following?.contains(post.userId) == true) {
                Text(
                    text = "Folowing",
                    color = Color.Gray,
                    modifier = Modifier.clickable { vm.onFollowClick(post.userId!!) })
            } else {
                Text(
                    text = "Follow",
                    color = Color.Blue,
                    modifier = Modifier.clickable { vm.onFollowClick(post.userId!!) })
            }
        }
    }

    Box {
        val modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
        CommonImage(
            data = post.postImage,
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )
    }

    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(
                id = if (isLiked.value) R.drawable.ic_like else R.drawable.ic_dislike
            ),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    // Beğeni durumunu güncelle
                    isLiked.value = !isLiked.value
                    if (isLiked.value) {
                        likesCount.value = likesCount.value + 1
                    } else {
                        likesCount.value = (likesCount.value - 1).coerceAtLeast(0)
                    }
                    vm.onLikePost(post)
                },
            colorFilter = ColorFilter.tint(
                if (isLiked.value) Color.Red else Color.Black
            )
        )
        Text(text = " ${likesCount.value} likes", modifier = Modifier.padding(start = 0.dp))
    }

    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = post.username ?: "", fontWeight = FontWeight.Bold)
        Text(text = post.postDescription ?: "", modifier = Modifier.padding(start = 8.dp))
    }

    Row(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "$nbComments comments",
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable {
                    post.postId?.let {
                        navController.navigate(DestinationScreen.CommentsScreen.createRoute(it))
                    }
                })
    }
}