package com.example.attendmestudents.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.attendmestudents.R
import com.example.attendmestudents.model.ClassesModel
import com.example.attendmestudents.navigation.Screens
import com.example.attendmestudents.viewmodel.HomeScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val viewModel: HomeScreenViewModel = viewModel()
    val context = LocalContext.current
    Scaffold(
        topBar = {TopAppBar(
            title = { Text(text = "Attend.Me-Student") },
            actions = {
                IconButton(onClick = {
                    viewModel.signOut()
                    navHostController.navigate(Screens.LoginScreen.route){
                        popUpTo(Screens.LoginScreen.route){
                            inclusive = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "sign out"
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors()
        )}
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            UserInfoCard(Modifier.weight(3f))
            EnrolledClassList(viewModel.enrolledClassesList.value,Modifier.weight(6f))
            JoinClass(onEnrollClick = {param->
                viewModel.classId.value = param
                viewModel.enrollInClass(
                    onSuccess = {
                        viewModel.classId.value = ""
                        viewModel.getAllEnrolledClasses()
                    },
                    onFailure = {
                        viewModel.getAllEnrolledClasses()
                        Toast.makeText(
                            context,
                            "Some error: $it",
                            Toast.LENGTH_LONG,
                        ).show()
                    },
                )
            }, Modifier.weight(1f))
        }
    }
}



@Composable
fun EnrolledClassList(classes: List<ClassesModel>,modifier: Modifier = Modifier) {
    Log.d("@@Home", classes.toString())
LazyColumn(modifier = modifier){
    items(items = classes){classModel ->
        Card(modifier = Modifier
            .fillMaxWidth()){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = classModel.className, modifier = Modifier.padding(10.dp))
                Text(text = classModel.batch, modifier = Modifier.padding(10.dp))
            }
        }
    }
}
}

@Composable
fun UserInfoCard(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(72.dp),
            painter = painterResource(id = R.drawable.baseline_person_24),
            colorFilter = ColorFilter.tint(Color.Green),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "X (TODO)",
            style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Student: (TODO) Department",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
        )
        Text(
            text = "imt_2020086@iiitm.ac.in",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinClass(onEnrollClick: (String) -> Unit, modifier: Modifier = Modifier) {
    var classID by remember { mutableStateOf("") }
    Row(modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)) {
        TextField(
            modifier = Modifier.weight(7f),
            value = classID,
            onValueChange = { classID = it },
            placeholder = {
                Text(
                    text = "Enter 6 character code",
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            })
        Spacer(modifier = Modifier.width(10.dp))
        FilledTonalButton(
            onClick = { onEnrollClick(classID) },
            modifier = Modifier.weight(3f),
            enabled = classID.length == 6
        ) {
            Text(text = "Enroll", maxLines = 1, overflow = TextOverflow.Visible, fontSize = 18.sp)
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navHostController = rememberNavController())
}