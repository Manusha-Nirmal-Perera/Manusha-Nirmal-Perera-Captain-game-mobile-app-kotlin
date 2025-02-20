package com.company.captaingame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.company.captaingame.ui.theme.CaptainGameTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaptainGameTheme {
                CaptainGame()
            }
        }
    }
}

@Composable
fun CaptainGame() {
    var hp = remember { mutableStateOf(100) }
    var treasuresFound = remember { mutableStateOf(0) }
    var direction = remember { mutableStateOf("North") }
    var stormOrTreasure = remember { mutableStateOf("") }
    var crewEnergy = remember { mutableStateOf(100) }
    var gold = remember { mutableStateOf(0) }
    var repairAvailable = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "HP: ${hp.value}")
        Row{
            Text(text = "Treasures: ${treasuresFound.value}")
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Gold: ${gold.value}")
        }
        Text(text = "Crew Energy: ${crewEnergy.value}")
        Text(text = "Direction: ${direction.value}")
        Text(text = stormOrTreasure.value)
        if (hp.value > 0) {
            Row {
                DirectionButton(
                    "East",
                    hp,
                    crewEnergy,
                    treasuresFound,
                    gold,
                    stormOrTreasure,
                    repairAvailable
                ) {
                    direction.value = "East"
                }

                Spacer(modifier = Modifier.width(16.dp))

                DirectionButton(
                    "South",
                    hp,
                    crewEnergy,
                    treasuresFound,
                    gold,
                    stormOrTreasure,
                    repairAvailable
                ) {
                    direction.value = "South"
                }
            }

            Row {
                DirectionButton(
                    "West",
                    hp,
                    crewEnergy,
                    treasuresFound,
                    gold,
                    stormOrTreasure,
                    repairAvailable
                ) {
                    direction.value = "West"
                }

                Spacer(modifier = Modifier.width(16.dp))

                DirectionButton(
                    "North",
                    hp,
                    crewEnergy,
                    treasuresFound,
                    gold,
                    stormOrTreasure,
                    repairAvailable
                ) {
                    direction.value = "North"
                }
            }
        }


        if (repairAvailable.value && hp.value > 0) {
            Button(onClick = {
                if (gold.value >= 5) {
                    hp.value = 100
                    if(crewEnergy.value >= 90){
                        crewEnergy.value = 100
                    }else {
                        crewEnergy.value += 10
                    }
                    gold.value -= 5
                    repairAvailable.value = false
                } else {
                    stormOrTreasure.value = "Not enough gold to repair!"
                }
            }) {
                Text("Repair Ship (5 Gold)")
            }
        }

        if (hp.value <= 0 || crewEnergy.value <= 0) {
            Text("Game Over! Your ship is lost at sea.")
            Button(onClick = {
                hp.value = 100
                crewEnergy.value = 100
                treasuresFound.value = 0
                gold.value = 0
                stormOrTreasure.value = ""
            }) {
                Text("Restart Game")
            }
        } else {
            Button(onClick = {
                hp.value = 100
                crewEnergy.value = 100
                treasuresFound.value = 0
                gold.value = 0
                stormOrTreasure.value = ""
            }) {
                Text("Restart Game")
            }
        }
    }
}

@Composable
fun DirectionButton(
    direction: String,
    hp: MutableState<Int>,
    crewEnergy: MutableState<Int>,
    treasuresFound: MutableState<Int>,
    gold: MutableState<Int>,
    stormOrTreasure: MutableState<String>,
    repairAvailable: MutableState<Boolean>,
    onDirectionChange: () -> Unit
) {
    Button(onClick = {
        onDirectionChange()
        val event = Random.nextInt(1, 100)
        when {
            event < 40 -> { // Storm
                var damage = Random.nextInt(5, 20)
                if(damage > hp.value){
                    damage = hp.value
                }
                stormOrTreasure.value = "Storm ahead! Lost $damage HP."
                hp.value -= damage
                crewEnergy.value -= 5
            }
            event < 70 -> { // Treasure
                val goldFound = Random.nextInt(1, 10)
                stormOrTreasure.value = "Found a treasure! +$goldFound gold."
                treasuresFound.value++
                gold.value += goldFound
                repairAvailable.value = Random.nextBoolean()
            }
            event < 90 -> { // Floating supplies
                stormOrTreasure.value = "Found floating supplies! HP restored by 10."
                hp.value = (hp.value + 10).coerceAtMost(100)
            }
            else -> { // Sea Monster!
                val damage = Random.nextInt(15, 30)
                stormOrTreasure.value = "A sea monster attacked! Lost $damage HP."
                hp.value -= damage
                crewEnergy.value -= 10
            }
        }
    }) {
        Text("Sail $direction")
    }
}



@Preview(showBackground = true)
@Composable
fun CaptainGamePreview() {
    CaptainGameTheme {
        CaptainGame()
    }
}