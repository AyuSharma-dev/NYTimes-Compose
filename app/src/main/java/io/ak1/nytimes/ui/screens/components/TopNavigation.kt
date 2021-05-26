package io.ak1.nytimes.ui.screens.components

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ak1.nytimes.R
import io.ak1.nytimes.model.Results
import io.ak1.nytimes.ui.screens.home.StoriesViewModel
import io.ak1.nytimes.ui.screens.navigation.MainDestinations

@Composable
fun HomeAppBar(navController: NavController) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .wrapContentSize()
                .wrapContentHeight()
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .layoutId("ToolBarTitleId")
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_bookmark),
                contentDescription = "hie",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                modifier = Modifier
                    .layoutId("ToolBarBookmarkId")
                    .clickable {
                        navController.navigate(MainDestinations.BOOKMARK_ROUTE)
                    }
                    .padding(12.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "hie",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                modifier = Modifier
                    .layoutId("ToolBarSettingsId")
                    .clickable {
                        navController.navigate(MainDestinations.SETTINGS_ROUTE)
                    }
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun CustomAppBar(
    navController: NavController, viewModel: StoriesViewModel,
    story: Results
) {
    val context = LocalContext.current
    val bookmarked =
        viewModel.isBookmarked(story.title ?: "").observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = "hie",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .clickable {
                    navController.navigateUp()
                }
                .padding(12.dp)
        )
        Spacer(modifier = Modifier.weight(1f, true))
        Image(
            painter = painterResource(if (bookmarked.value) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark),
            contentDescription = "hie",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .clickable {
                    if (bookmarked.value) {
                        viewModel.deleteBookmark(story, coroutineScope)
                    } else {
                        viewModel.addBookmark(story, coroutineScope)
                    }
                }
                .padding(12.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_share),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentDescription = "share link",
            modifier = Modifier
                .clickable {
                    try {
                        Intent(Intent.ACTION_SEND).let {
                            context.startActivity(Intent.createChooser(it, story.url))
                        }
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }

                }
                .padding(12.dp)
        )
    }
}

@Composable
fun DefaultAppBar(
    @DrawableRes iconId: Int = R.drawable.ic_arrow_left,
    @StringRes titleId: Int,
    navController: NavController
) {
    Row(modifier = Modifier.padding(4.dp)) {
        Image(
            painter = painterResource(iconId),
            contentDescription = "hie",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .clickable {
                    navController.navigateUp()
                }
                .padding(12.dp)
        )
        Text(
            text = stringResource(id = titleId),
            style = MaterialTheme.typography.h6, modifier = Modifier.padding(0.dp, 9.dp)
        )
    }
}
