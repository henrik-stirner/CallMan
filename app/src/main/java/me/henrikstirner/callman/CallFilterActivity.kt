package me.henrikstirner.callman

import android.os.Bundle
import android.telecom.Call
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ButtonGroupMenuState
import androidx.compose.material3.ButtonGroupScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.henrikstirner.callman.ui.theme.CallManTheme

class CallFilterActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			CallManTheme {
				this.UI()
			}
		}
	}

	@Preview(showBackground = true)
	@Composable
	fun UIPreview() {
		CallManTheme(darkTheme = true) {
			this.UI()
		}
	}

	@Composable
	private fun UI() {
		Scaffold(
			topBar = { TopBar(onBackButtonClick = { this@CallFilterActivity.finish() }) }
		) { innerPadding ->
			Box(modifier = Modifier.padding(innerPadding)) {
				Box(
					modifier = Modifier
						.padding(horizontal = 32.dp)
						.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					CallFilter()
				}
			}
		}
	}

	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	fun TopBar(onBackButtonClick: () -> Unit) {
		TopAppBar(
			title = { Text("") },
			navigationIcon = {
				IconButton(onClick = onBackButtonClick) {
					Icon(
						imageVector = Icons.Rounded.ArrowBackIosNew,
						contentDescription = "Back"
					)
				}
			}
		)
	}

	@Composable
	fun CallFilter() {
		Column(
			modifier = Modifier
				.fillMaxSize()
		) {
			ListModeToggle { }
			CallerList()
			HCenteredRoundAddButton { }
		}
	}

	@OptIn(ExperimentalMaterial3ExpressiveApi::class)
	@Composable
	fun ListModeToggle(onClick: (selectedIndex: Int) -> Unit) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.horizontalScroll(rememberScrollState()),
			horizontalArrangement = Arrangement.Center
		) {
			val options = listOf("Blacklist", "Whitelist")
			val icons = listOf(Icons.Rounded.Clear, Icons.Rounded.Done)
			val descriptions = listOf("Exclude", "Allow only")

			var selectedIndex by remember { mutableIntStateOf(0) }

			FlowRow(
				Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
				verticalArrangement = Arrangement.spacedBy(2.dp),
			) {
				options.forEachIndexed { index, label ->
					ToggleButton(
						checked = selectedIndex == index,
						onCheckedChange = {
							selectedIndex = index
							onClick(index)
						},
						shapes = when (index) {
							0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
							options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
							else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
						},
						modifier = Modifier.semantics { role = Role.RadioButton },
					) {
						Icon(
							icons[index],
							contentDescription = descriptions[index],
						)
						Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
						Text(label)
					}
				}
			}
		}
	}

	@Composable
	fun CallerList() {
		Surface (
			shape = RoundedCornerShape(32.dp),
			color = Color.White,
			// border = BorderStroke(4.dp, Color.White),
		) {
			Column(
				modifier = Modifier
					.padding(4.dp)
					.fillMaxWidth()
			) {
				CallerListEntry("+49 160 9863 1792")
				CallerListEntry("+49 160 9863 1792")
				CallerListEntry("+49 160 9863 1792")
				CallerListEntry("+49 160 9863 1792")
			}
		}
	}

	@Composable
	fun CallerListEntry(label: String) {
		Surface(
			modifier = Modifier.padding(4.dp),
			shape = CircleShape
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(modifier = Modifier.padding(horizontal = 16.dp), text = label)
				IconButton(onClick = {  }) {
					Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
				}
			}
		}
	}

	@Composable
	fun HCenteredRoundAddButton(onClick: () -> Unit) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical=8.dp),
			horizontalArrangement = Arrangement.Center
		) {
			Button(
				shape = CircleShape,
				modifier = Modifier.size(64.dp),
				onClick = { onClick() }
			) {
				Icon(Icons.Rounded.Add, contentDescription = "Add")
			}
		}
	}
}