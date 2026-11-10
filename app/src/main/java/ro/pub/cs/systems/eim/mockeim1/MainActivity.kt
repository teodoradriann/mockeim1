package ro.pub.cs.systems.eim.mockeim1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import ro.pub.cs.systems.eim.mockeim1.ui.theme.Mockeim1Theme

// 2. mecanism de salvare / restaurare daca androidul distruge activitatea
class MainActivity : ComponentActivity() {
    private lateinit var prefs: SharedPreferences
    private var leftCount by mutableIntStateOf(0)
    private var rightCount by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        leftCount = prefs.getInt(LEFT_COUNT_KEY, 0)
        rightCount = prefs.getInt(RIGHT_COUNT_KEY, 0)
        Log.d(TAG, "restored: left=$leftCount, right=$rightCount")

        enableEdgeToEdge()
        setContent {
            Mockeim1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CountersScreen(
                        leftCount = leftCount,
                        rightCount = rightCount,
                        onLeftPress = {
                            leftCount++
                            saveCounts()
                        },
                        onRightPress = {
                            rightCount++
                            saveCounts()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun saveCounts() {
        prefs.edit {
            putInt(LEFT_COUNT_KEY, leftCount)
            putInt(RIGHT_COUNT_KEY, rightCount)
        }
        Log.d(TAG, "saved: left=$leftCount, right=$rightCount")
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val PREFS_NAME = "counters"
        private const val LEFT_COUNT_KEY = "leftCount"
        private const val RIGHT_COUNT_KEY = "rightCount"
    }
}

@Composable
fun CountersScreen(
    leftCount: Int,
    rightCount: Int,
    onLeftPress: () -> Unit,
    onRightPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Counter(
            count = leftCount,
            buttonText = "Press me",
            onPress = onLeftPress,
            modifier = Modifier.weight(1f)
        )
        Counter(
            count = rightCount,
            buttonText = "Press me too",
            onPress = onRightPress,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Counter(
    count: Int,
    buttonText: String,
    onPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = count.toString(),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onPress) {
            Text(buttonText)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountersScreenPreview() {
    Mockeim1Theme {
        CountersScreen(
            leftCount = 0,
            rightCount = 0,
            onLeftPress = {},
            onRightPress = {}
        )
    }
}
