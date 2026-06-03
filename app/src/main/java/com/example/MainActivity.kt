package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.draw.alpha
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.AppDatabase
import com.example.data.SubTask
import com.example.data.Task
import com.example.data.TaskRepository
import com.example.data.TaskWithSubTasks
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.TaskViewModel
import com.example.ui.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val database = remember { AppDatabase.getDatabase(context) }
            val repository = remember { TaskRepository(database.taskDao()) }
            val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(repository))

            var isDarkTheme by remember { mutableStateOf(false) }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                TaskCandyApp(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = it }
                )
            }
        }
    }
}

// Keep Greeting method to prevent legacy test compilation errors
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

enum class TaskScreen {
    TAREFAS, AGENDA, STATUS, PERFIL
}

const val AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuCo_5SKQL5WYd3GCxQxETVE1zlIyXAa5mypa0kFOkhGXrqMwQ0rpUFnU_q6Bxwe3Lf9G1mZ_bCCVn_bDbPMoVYVlHYbbph52HDjeV-N2B3GSYRDSF0exkuTSCP5HCGT9OOqUzI6jBk41FJHXqvkPOu-hWbeDlYX-4UzGFQZNEfyaRsQSvJTYUXqoebBryHJ5F5OqbuzsQmPtzslkV6VKzLwmC1jhUVphCMzNKcB62GgKxXN5WK59L_refYypQeqJQgfnn83yRPHXJ8"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCandyApp(
    viewModel: TaskViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    var currentScreen by remember { mutableStateOf(TaskScreen.TAREFAS) }
    val tasksList by viewModel.tasksForSelectedDate.collectAsStateWithLifecycle()
    val allTasksEver by viewModel.allTasks.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskWithSubTasks?>(null) }

    // Pre-populate DB if empty
    LaunchedEffect(tasksList, allTasksEver) {
        if (tasksList.isEmpty() && allTasksEver.isEmpty()) {
            viewModel.insertTask(
                title = "Arrumar meu Quarto",
                description = "Criar o hábito de organizar o próprio espaço! Cama e armário arrumados de manhã.",
                category = "CASA",
                difficulty = "CANDY_MEDIUM",
                date = TaskViewModel.getTodayDateString(),
                subtaskTitles = listOf("Esticar lençol da cama", "Organizar tênis e sapatos", "Dobrar as roupas jogadas")
            )
            viewModel.insertTask(
                title = "Estudar Matemática",
                description = "Praticar fração e tabuada para a próxima prova da escola.",
                category = "ESTUDOS",
                difficulty = "CANDY_HARD",
                date = TaskViewModel.getTodayDateString(),
                subtaskTitles = listOf("Ver 1 vídeoaula de frações", "Resolver 5 exercícios práticos")
            )
            viewModel.insertTask(
                title = "Ajudar na Cozinha",
                description = "Tirar o prato e lavar a louça do almoço.",
                category = "CASA",
                difficulty = "CANDY_EASY",
                date = TaskViewModel.getTodayDateString(),
                subtaskTitles = listOf("Lavar a louça acumulada", "Limpar a mesa de jantar")
            )
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding() + 8.dp)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "TaskCandy",
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { currentScreen = TaskScreen.PERFIL }
                    ) {
                        AsyncImage(
                            model = AVATAR_URL,
                            contentDescription = "User profile avatar",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        ambientColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        spotColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    )
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                NavigationBar(
                    containerColor = Color(0xFFF3EDF7),
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.height(72.dp)
                ) {
                    val items = listOf(
                        Triple(TaskScreen.TAREFAS, "Tarefas", Icons.Default.Assignment),
                        Triple(TaskScreen.AGENDA, "Agenda", Icons.Default.CalendarMonth),
                        Triple(TaskScreen.STATUS, "Status", Icons.Default.BarChart),
                        Triple(TaskScreen.PERFIL, "Perfil", Icons.Default.Person)
                    )

                    items.forEach { (screen, label, icon) ->
                        val selected = currentScreen == screen
                        NavigationBarItem(
                            selected = selected,
                            onClick = { currentScreen = screen },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF6750A4),
                                selectedTextColor = Color(0xFF6750A4),
                                indicatorColor = Color(0xFFEADDFF),
                                unselectedIconColor = Color(0xFF49454F),
                                unselectedTextColor = Color(0xFF49454F)
                            ),
                            modifier = Modifier.testTag("nav_${label.lowercase()}")
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentScreen == TaskScreen.TAREFAS || currentScreen == TaskScreen.AGENDA) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color(0xFFD0BCFF),
                    contentColor = Color(0xFF21005D),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color(0xFFD0BCFF).copy(alpha = 0.4f),
                            spotColor = Color(0xFFD0BCFF).copy(alpha = 0.4f)
                        )
                        .testTag("add_task_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Tarefa",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val maxWidth = maxWidth
            val widthModifier = if (maxWidth > 600.dp) {
                Modifier
                    .widthIn(max = 600.dp)
                    .align(Alignment.TopCenter)
            } else {
                Modifier.fillMaxSize()
            }

            Box(modifier = widthModifier) {
                when (currentScreen) {
                    TaskScreen.TAREFAS -> HomeScreen(
                        viewModel = viewModel,
                        tasks = tasksList,
                        allTasksEver = allTasksEver,
                        onEditTask = { taskToEdit = it }
                    )
                    TaskScreen.AGENDA -> AgendaScreen(
                        viewModel = viewModel,
                        tasks = tasksList,
                        selectedDate = selectedDate,
                        onEditTask = { taskToEdit = it }
                    )
                    TaskScreen.STATUS -> StatusScreen(
                        viewModel = viewModel,
                        tasks = tasksList,
                        allTasksEver = allTasksEver
                    )
                    TaskScreen.PERFIL -> ProfileScreen(
                        viewModel = viewModel,
                        allTasksEver = allTasksEver,
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = onThemeToggle
                    )
                }
            }
        }
    }

    // Add dialog
    if (showAddDialog) {
        AddEditTaskDialog(
            viewModel = viewModel,
            selectedDate = selectedDate,
            onDismiss = { showAddDialog = false }
        )
    }

    // Edit dialog
    taskToEdit?.let { taskWithSubs ->
        AddEditTaskDialog(
            viewModel = viewModel,
            selectedDate = selectedDate,
            taskWithSubs = taskWithSubs,
            onDismiss = { taskToEdit = null }
        )
    }
}

// -------------------------------------------------------------
// DYNAMIC XP & LEVEL UTILS
// -------------------------------------------------------------
data class SweetLevel(val level: Int, val title: String, val minXp: Int, val maxXp: Int, val emoji: String)

fun getLevelForXp(xp: Int): SweetLevel {
    val levels = listOf(
        SweetLevel(1, "Novato Doce", 0, 149, "🍯"),
        SweetLevel(2, "Marshmallow Veloz", 150, 399, "🍡"),
        SweetLevel(3, "Foguete de Algodão", 400, 799, "🍬"),
        SweetLevel(4, "Desbravador do Açúcar", 800, 1499, "🍩"),
        SweetLevel(5, "Mestre dos Doces 🏆", 1500, 99999, "🍭")
    )
    return levels.firstOrNull { xp in it.minXp..it.maxXp } ?: levels.last()
}

// -------------------------------------------------------------
// SCREEN 1: HOME (TAREFAS)
// -------------------------------------------------------------
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    tasks: List<TaskWithSubTasks>,
    allTasksEver: List<TaskWithSubTasks>,
    onEditTask: (TaskWithSubTasks) -> Unit
) {
    val context = LocalContext.current
    var selectedCategoryFilter by remember { mutableStateOf("TODAS") }

    val totalXpEver = allTasksEver.filter { it.task.isCompleted }.sumOf { it.task.xpReward }
    val currentSweetLevel = getLevelForXp(totalXpEver)

    val todayCompletedCount = tasks.count { it.task.isCompleted }
    val todayPendingCount = tasks.count { !it.task.isCompleted }
    val todayTotalCount = tasks.size

    val filteredTasks = when (selectedCategoryFilter) {
        "CASA" -> tasks.filter { it.task.category == "CASA" }
        "ESTUDOS" -> tasks.filter { it.task.category == "ESTUDOS" }
        else -> tasks
    }

    // Best pending task for highlight
    val urgentTask = tasks
        .filter { !it.task.isCompleted }
        .minByOrNull {
            when (it.task.difficulty) {
                "CANDY_HARD" -> 1
                "CANDY_MEDIUM" -> 2
                else -> 3
            }
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcoming with user profile badge
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Olá, Doce Criador! 👋",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color(0xFF1D1B20),
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = if (todayPendingCount > 0) {
                            "Você tem $todayPendingCount ${if (todayPendingCount == 1) "tarefa" else "tarefas"} para saborizar hoje."
                        } else if (todayTotalCount > 0) {
                            "Huuum, todas as tarefas de hoje estão feitas! 🍦"
                        } else {
                            "Parabéns! Nenhuma tarefa agendada para hoje. 🧸"
                        },
                        fontSize = 14.sp,
                        color = Color(0xFF49454F),
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // User Avatar Badge matching <div class="w-12 h-12 rounded-full bg-[#EADDFF] flex items-center justify-center text-[#21005D] font-bold border-2 border-[#6750A4]">T</div>
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEADDFF))
                        .border(2.dp, Color(0xFF6750A4), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "T",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF21005D)
                    )
                }
            }
        }

        // Main Progress Card from Vibrant Palette HTML spec
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6750A4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = Color(0xFF6750A4).copy(alpha = 0.25f),
                        spotColor = Color(0xFF6750A4).copy(alpha = 0.25f)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Progresso Diário",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                            val dailyProgressPercentage = if (todayTotalCount > 0) {
                                (todayCompletedCount * 100) / todayTotalCount
                            } else {
                                0
                            }
                            Text(
                                text = "$dailyProgressPercentage%",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Text(
                            text = "$todayCompletedCount de $todayTotalCount concluídas",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    // Styled custom progress bar matching w-full bg-white/20 h-3 rounded-full overflow-hidden
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        val dailyProgressPercentage = if (todayTotalCount > 0) {
                            (todayCompletedCount * 100) / todayTotalCount
                        } else {
                            0
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = dailyProgressPercentage / 100f)
                                .clip(CircleShape)
                                .background(Color(0xFFD0BCFF))
                        )
                    }
                }
            }
        }

        // Level pill
        item {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEADDFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Color(0xFF6750A4).copy(alpha = 0.4f), CircleShape)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Awesome Icon",
                        tint = Color(0xFF21005D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nível ${currentSweetLevel.level}: ${currentSweetLevel.title} ${currentSweetLevel.emoji}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF21005D)
                    )
                }
            }
        }

        // Stats boxes grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Completed Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(115.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                    onClick = { }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Checked",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Concluídas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            text = "$todayCompletedCount",
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Pending Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .weight(1f)
                        .height(115.dp)
                        .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                    onClick = { }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.Default.Pending,
                                contentDescription = "Pending",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pendentes",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Text(
                            text = "$todayPendingCount",
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                // XP Earned Today
                val todayXp = tasks.filter { it.task.isCompleted }.sumOf { it.task.xpReward }
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier
                        .weight(1f)
                        .height(115.dp)
                        .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                    onClick = { }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "XP Today",
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Doce XP",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Text(
                            text = "$todayXp",
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        // Highlight urgent chore
        if (urgentTask != null) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFE8E8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PriorityHigh,
                                    contentDescription = "Urgent",
                                    tint = Color(0xFFE53E3E),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "DESTAQUES URGENTES",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = urgentTask.task.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (urgentTask.task.progressPercentage > 0) {
                                    "Progresso em ${urgentTask.task.progressPercentage}%. Vamos terminar!"
                                } else {
                                    "Prepara os dentes! Essa vale ${urgentTask.task.xpReward} XP. Vamos começar?"
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onEditTask(urgentTask) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = CircleShape,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                            ) {
                                Text(
                                    text = "Começar Já!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Modelos Pré-definidos Rápidos (Quick Preset Templates)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Modelos Pré-definidos ⚡",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1D1B20)
                    )
                    Text(
                        text = "Toque para Agendar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6750A4)
                    )
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    com.example.data.PresetLibrary.presets.forEach { preset ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .width(220.dp)
                                .height(160.dp)
                                .border(1.dp, Color(0xFFCAC4D0), RoundedCornerShape(20.dp)),
                            onClick = {
                                viewModel.insertTask(
                                    title = preset.title,
                                    description = preset.description,
                                    category = preset.category,
                                    difficulty = preset.difficulty,
                                    date = viewModel.selectedDate.value,
                                    subtaskTitles = preset.subtasks
                                )
                                android.widget.Toast.makeText(
                                    context,
                                    "\"${preset.title}\" agendada com sucesso! 🎉",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Emoji in outer circle
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (preset.category == "CASA") Color(0xFFF2F0F4)
                                                    else Color(0xFFEADDFF)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = preset.emoji, fontSize = 16.sp)
                                        }
                                        
                                        // Category chip badge
                                        val isCasa = preset.category == "CASA"
                                        val catLabel = if (isCasa) "CASA" else "ESTUDOS"
                                        val badgeBg = if (isCasa) Color(0xFFF2F0F4) else Color(0xFFEADDFF)
                                        val badgeText = if (isCasa) Color(0xFF49454F) else Color(0xFF21005D)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(badgeBg)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = catLabel,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = badgeText
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = preset.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF1D1B20),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    
                                    Text(
                                        text = preset.description,
                                        fontSize = 11.sp,
                                        color = Color(0xFF49454F),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "+${preset.xpReward} XP",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF6750A4)
                                    )
                                    
                                    // Visual quick-add indicator pill
                                    Row(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color(0xFFF3EDF7))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Agendar",
                                            tint = Color(0xFF6750A4),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "Agendar",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6750A4)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // List Header and Filters
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Suas Tarefas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Category badges scrollable / togglable
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    val filters = listOf(
                        "TODAS" to "Todas",
                        "CASA" to "Casa 🏠",
                        "ESTUDOS" to "Estudos 📚"
                    )
                    filters.forEach { (cat, desc) ->
                        val isSel = selectedCategoryFilter == cat
                        Surface(
                            onClick = { selectedCategoryFilter = cat },
                            color = if (isSel) Color(0xFFE8DEF8) else Color.White,
                            shape = CircleShape,
                            modifier = Modifier
                                .height(36.dp)
                                .border(
                                    1.dp,
                                    if (isSel) Color.Transparent else Color(0xFF79747E),
                                    CircleShape
                                )
                        ) {
                            Text(
                                text = desc,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSel) Color(0xFF1D192B) else Color(0xFF49454F),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Task Cards Row/List
        if (filteredTasks.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🍭",
                        fontSize = 44.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Nenhuma tarefa nesta categoria hoje!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Toque no '+' para adicionar e começar a ganhar doces!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 2.dp)
                    )
                }
            }
        } else {
            items(filteredTasks, key = { it.task.id }) { taskWithSubs ->
                TaskCard(
                    taskWithSubs = taskWithSubs,
                    onToggleComplete = { viewModel.toggleTaskCompletion(taskWithSubs) },
                    onToggleSubtask = { sub -> viewModel.toggleSubTask(sub) },
                    onEdit = { onEditTask(taskWithSubs) },
                    onDelete = { viewModel.deleteTask(taskWithSubs.task) }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// TASK CARD COMPONENT
// -------------------------------------------------------------
@Composable
fun TaskCard(
    taskWithSubs: TaskWithSubTasks,
    onToggleComplete: () -> Unit,
    onToggleSubtask: (SubTask) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val task = taskWithSubs.task
    val subTasks = taskWithSubs.subTasks

    // Dynamic states based on Vibrant Palette HTML
    val isCompleted = task.isCompleted
    val isInProgress = task.progressPercentage > 0 && !isCompleted

    val cardBorderColor = if (isCompleted) {
        Color(0xFFCAC4D0)
    } else if (isInProgress) {
        Color(0xFF6750A4)
    } else {
        Color(0xFFCAC4D0)
    }

    val cardBorderWidth = if (isInProgress) 2.dp else 1.dp
    val cardAlpha = if (isCompleted) 0.7f else 1.0f

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .shadow(
                elevation = if (isInProgress) 4.dp else 1.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .border(cardBorderWidth, cardBorderColor, RoundedCornerShape(18.dp))
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title and Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Graphic symbol decorator (left circle) from HTML spec
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) Color(0xFFE6F4EA)
                            else if (isInProgress) Color(0xFFF0EFFF)
                            else Color.White
                        )
                        .border(
                            width = if (isCompleted || isInProgress) 0.dp else 2.dp,
                            color = if (isCompleted || isInProgress) Color.Transparent else Color(0xFFCAC4D0),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        // Green positive check circle
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color(0xFF1E8E3E),
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (isInProgress) {
                        // Violet progress icon/dots
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "In progress",
                            tint = Color(0xFF6750A4),
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        // Gray centering dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCAC4D0))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title, badge & percentage
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1D1B20),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Vibrant category & state badge layout from HTML
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Badge
                        val isCasa = task.category == "CASA"
                        val badgeBg = if (isCasa) Color(0xFFF2F0F4) else Color(0xFFEADDFF)
                        val badgeText = if (isCasa) Color(0xFF49454F) else Color(0xFF21005D)
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(badgeBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = task.category,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = badgeText
                            )
                        }

                        // Difficulty info
                        val diffLabel = when (task.difficulty) {
                            "CANDY_EASY" -> "Mel 🍯"
                            "CANDY_MEDIUM" -> "Marshmallow 🍡"
                            "CANDY_HARD" -> "Chocolate 🍫"
                            else -> "Mel 🍯"
                        }
                        Text(
                            text = diffLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF49454F)
                        )

                        Text(text = "•", fontSize = 11.sp, color = Color(0xFFCAC4D0))

                        Text(
                            text = "+${task.xpReward} XP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF6750A4)
                        )
                    }
                }

                // Main Checkbox button
                IconButton(
                    onClick = { onToggleComplete() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
                        contentDescription = "Toggle Complete",
                        tint = if (isCompleted) Color(0xFF1E8E3E) else Color(0xFFCAC4D0),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Description
            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.description,
                    fontSize = 13.sp,
                    color = Color(0xFF49454F),
                    maxLines = if (expanded) 10 else 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 52.dp)
                )
            }

            // Progress Bar
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 52.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Custom Track with matching background
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE7E0EC))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = task.progressPercentage / 100f)
                            .clip(CircleShape)
                            .background(if (isCompleted) Color(0xFF1E8E3E) else Color(0xFF6750A4))
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${task.progressPercentage}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isCompleted) Color(0xFF1E8E3E) else Color(0xFF6750A4)
                )
            }

            // Expanded Subtasks and Edit area
            if (expanded) {
                if (subTasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Passos para completar:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF49454F),
                        modifier = Modifier.padding(start = 52.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    subTasks.forEach { subTask ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 44.dp)
                                .heightIn(min = 32.dp)
                                .clickable { onToggleSubtask(subTask) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = subTask.isCompleted,
                                onCheckedChange = { onToggleSubtask(subTask) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF6750A4),
                                    uncheckedColor = Color(0xFFCAC4D0)
                                ),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = subTask.title,
                                fontSize = 13.sp,
                                color = if (subTask.isCompleted) Color(0xFF49454F).copy(alpha = 0.6f) else Color(0xFF1D1B20),
                                textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Edit/Delete buttons
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 52.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { onEdit() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF6750A4))
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Editar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = { onDelete() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFB3261E))
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Excluir", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SCREEN 2: AGENDA
// -------------------------------------------------------------
@Composable
fun AgendaScreen(
    viewModel: TaskViewModel,
    tasks: List<TaskWithSubTasks>,
    selectedDate: String,
    onEditTask: (TaskWithSubTasks) -> Unit
) {
    val context = LocalContext.current
    var selectedWeekDay by remember { mutableStateOf(selectedDate) }

    // Generate week days from today
    val datesOfWeek = remember {
        val list = mutableListOf<String>()
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek) // Start of week (usually Sunday)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        for (i in 0..6) {
            list.add(sdf.format(cal.time))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        list
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .testTag("agenda_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Sua Agenda 📅",
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Organize seus estudos e tarefas durante a semana.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }

        // Calendar scrollable strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            datesOfWeek.forEach { dateStr ->
                val isSel = selectedDate == dateStr
                val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val d = sdfInput.parse(dateStr) ?: Date()

                val dayFormat = SimpleDateFormat("EEE", Locale("pt", "BR"))
                val dayNumFormat = SimpleDateFormat("dd", Locale.getDefault())

                val dayNameStr = dayFormat.format(d).replace(".", "").uppercase()
                val dayNumStr = dayNumFormat.format(d)

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .width(64.dp)
                        .height(84.dp)
                        .clickable { viewModel.setDate(dateStr) }
                        .border(
                            1.dp,
                            if (isSel) Color.Transparent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dayNameStr,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSel) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dayNumStr,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Selected Date Label
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = TaskViewModel.formatDateToDisplay(selectedDate),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        // Today's list in Agenda
        Text(
            text = "Doces deste dia:",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (tasks.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🍃", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Açúcar zerado para este dia!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Adicione uma tarefa no botão '+' para agendar neste dia.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 2.dp)
                        )
                    }
                }
            } else {
                items(tasks, key = { it.task.id }) { taskWithSubs ->
                    TaskCard(
                        taskWithSubs = taskWithSubs,
                        onToggleComplete = { viewModel.toggleTaskCompletion(taskWithSubs) },
                        onToggleSubtask = { sub -> viewModel.toggleSubTask(sub) },
                        onEdit = { onEditTask(taskWithSubs) },
                        onDelete = { viewModel.deleteTask(taskWithSubs.task) }
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SCREEN 3: STATUS & METRICS
// -------------------------------------------------------------
@Composable
fun StatusScreen(
    viewModel: TaskViewModel,
    tasks: List<TaskWithSubTasks>,
    allTasksEver: List<TaskWithSubTasks>
) {
    val todayCompletedCount = tasks.count { it.task.isCompleted }
    val todayTotalCount = tasks.size
    val dailyProgressPercentage = if (todayTotalCount > 0) {
        (todayCompletedCount * 100) / todayTotalCount
    } else {
        0
    }

    val totalXpEver = allTasksEver.filter { it.task.isCompleted }.sumOf { it.task.xpReward }
    val currentSweetLevel = getLevelForXp(totalXpEver)

    val xpNeededForNextLevel = currentSweetLevel.maxXp - totalXpEver
    val xpProgressInLevel = (totalXpEver - currentSweetLevel.minXp).toFloat() / (currentSweetLevel.maxXp - currentSweetLevel.minXp).toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("status_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(
                text = "Status Doces 📊",
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Veja a sua evolução e os doces conquistados!",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }

        // Circle Progress of Today's Goal
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular canvas
                Box(
                    modifier = Modifier.size(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
                    Canvas(modifier = Modifier.size(80.dp)) {
                        drawCircle(
                            color = primaryColor.copy(alpha = 0.15f),
                            radius = size.minDimension / 2f,
                            style = Stroke(width = 8.dp.toPx())
                        )
                        drawArc(
                            color = primaryColor,
                            startAngle = -90f,
                            sweepAngle = 360f * (dailyProgressPercentage / 100f),
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "$dailyProgressPercentage%",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Meta do Dia",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when {
                            dailyProgressPercentage == 100 -> "Nhaac! Incrível! Você comeu todo o seu cronograma hoje! 🍭"
                            dailyProgressPercentage >= 70 -> "Falta um pedacinho de açúcar para bater a meta diária! 🧁"
                            dailyProgressPercentage >= 30 -> "Muito bem! O sabor do progresso já começou! 🍡"
                            todayTotalCount > 0 -> "Faltam apenas $todayCompletedCount/$todayTotalCount tarefas para sua recompensa! 🍯"
                            else -> "Agende tarefas e adoce seu dia! 🍦"
                        },
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Custom chart: "Fluxo de Alegria & Foco"
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Chart",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Fluxo de Alegria & Foco",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Suas tarefas concluídas nos últimos dias",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Bar Chart rendering via Composable
                val recentDays = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SÁB", "DOM")
                // Random completions for mockup chart feel
                val values = listOf(0.4f, 0.7f, 0.55f, 0.9f, 0.35f, 0.65f, dailyProgressPercentage / 100f)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    values.forEachIndexed { idx, value ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .fillMaxHeight(0.85f * value.coerceAtLeast(0.05f))
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
                                            )
                                        )
                                    )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = recentDays[idx],
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // XP & Sweet Level Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Seu Progresso de Nível",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total XP Acumulado:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$totalXpEver XP",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { xpProgressInLevel },
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.background,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(8.dp))
                if (currentSweetLevel.level < 5) {
                    Text(
                        text = "Faltam $xpNeededForNextLevel XP para alcançar o próximo nível! 🍭",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "Parabéns! Você alcançou a patente máxima! Mestre dos Doces 🪐",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SCREEN 4: PROFILE (PERFIL)
// -------------------------------------------------------------
@Composable
fun ProfileScreen(
    viewModel: TaskViewModel,
    allTasksEver: List<TaskWithSubTasks>,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    val totalXpEver = allTasksEver.filter { it.task.isCompleted }.sumOf { it.task.xpReward }
    val currentSweetLevel = getLevelForXp(totalXpEver)

    val houseCompleted = allTasksEver.filter { it.task.category == "CASA" && it.task.isCompleted }.size
    val studyCompleted = allTasksEver.filter { it.task.category == "ESTUDOS" && it.task.isCompleted }.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("profile_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Avatar Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                AsyncImage(
                    model = AVATAR_URL,
                    contentDescription = "Profile Pic",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Doce Criador Julio 🧙‍♂️",
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "julioc.costaf@gmail.com",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }

        // Streak Count flame card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2E6)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFFFA040).copy(alpha = 0.2f), RoundedCornerShape(18.dp))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥",
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Doce Streak Ativo!",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB35300),
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Você está com 3 dias de produtividade seguidos! Não perca o bônus!",
                        color = Color(0xFF803C00),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Side-by-Side Category Completion card
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🏡", fontSize = 26.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Tarefas de Casa", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "$houseCompleted feitas", fontSize = 16.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary)
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📚", fontSize = 26.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Estudos e Foco", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "$studyCompleted feitas", fontSize = 16.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }

        // Unlocked medals/badges
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Conquistas Desbloqueadas 🏅",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            val achievements = listOf(
                Triple("🥇 Primeiro Doce", "Completou a primeira tarefa!", totalXpEver > 0),
                Triple("🧹 Guerreiro da Vassoura", "Fazer 5 tarefas domésticas.", houseCompleted >= 5),
                Triple("📖 Estudioso Supremo", "Fazer 5 sessões de estudos completas.", studyCompleted >= 5),
                Triple("⚡ Estrela Master do Dia", "Chegar a 1500 XP de pontuação acumulada.", totalXpEver >= 1500)
            )

            achievements.forEach { (title, desc, unlocked) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (unlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (unlocked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (unlocked) "🎉" else "🔒",
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (unlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                            Text(
                                text = desc,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        // Settings / Theme Switch
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🌙", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Modo Noturno",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeToggle(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}

// -------------------------------------------------------------
// CREATE / EDIT DIALOG OVERLAY
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskDialog(
    viewModel: TaskViewModel,
    selectedDate: String,
    taskWithSubs: TaskWithSubTasks? = null,
    onDismiss: () -> Unit
) {
    val isEditMode = taskWithSubs != null

    var title by remember { mutableStateOf(taskWithSubs?.task?.title ?: "") }
    var description by remember { mutableStateOf(taskWithSubs?.task?.description ?: "") }
    var category by remember { mutableStateOf(taskWithSubs?.task?.category ?: "CASA") }
    var difficulty by remember { mutableStateOf(taskWithSubs?.task?.difficulty ?: "CANDY_MEDIUM") }
    var progressVal by remember { mutableIntStateOf(taskWithSubs?.task?.progressPercentage ?: 0) }

    // Subtasks to manage during edit/add
    val existingSubtasks = remember {
        mutableStateListOf<SubTask>().apply {
            if (isEditMode && taskWithSubs != null) {
                addAll(taskWithSubs.subTasks)
            }
        }
    }
    val newSubtaskTitles = remember { mutableStateListOf<String>() }
    var currentNewSubtaskText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (isEditMode) "Editar Tarefa Doce ✏️" else "Nova Tarefa Doce 🍭",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Pre-defined Quick Fill templates inside creation dialog
                if (!isEditMode) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = "💡 Sugestões Prontas (Toque para Preencher):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            com.example.data.PresetLibrary.presets.forEach { preset ->
                                Surface(
                                    onClick = {
                                        title = preset.title
                                        description = preset.description
                                        category = preset.category
                                        difficulty = preset.difficulty
                                        newSubtaskTitles.clear()
                                        newSubtaskTitles.addAll(preset.subtasks)
                                    },
                                    color = Color(0xFFEADDFF).copy(alpha = 0.5f),
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .height(32.dp)
                                        .border(1.dp, Color(0xFF6750A4).copy(alpha = 0.25f), CircleShape)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = preset.emoji, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = preset.title,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF21005D)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nome da Tarefa", fontSize = 13.sp) },
                    placeholder = { Text("Ex: Lavar a louça, Estudar história...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("dialog_task_title")
                )

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Instruções ou Detalhes", fontSize = 13.sp) },
                    placeholder = { Text("Escreva os detalhes com carinho...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp)
                )

                // Category Selection
                Text(text = "Categoria Doce", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("CASA" to "🏠 Atividade em Casa", "ESTUDOS" to "📚 Estudos")
                    categories.forEach { (cat, label) ->
                        val isSel = category == cat
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                                .border(
                                    2.dp,
                                    if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { category = cat }
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Difficulty / Candy value
                Text(text = "Dificuldade / Valor de XP", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val difficulties = listOf(
                        Triple("CANDY_EASY", "Mel 🍯", "30 XP"),
                        Triple("CANDY_MEDIUM", "Marshmallow 🍡", "60 XP"),
                        Triple("CANDY_HARD", "Chocolate 🍫", "100 XP")
                    )
                    difficulties.forEach { (diff, label, xp) ->
                        val isSel = difficulty == diff
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)
                                .border(
                                    2.dp,
                                    if (isSel) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { difficulty = diff }
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = xp,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                // SUBTASKS / CHECKLIST SECTION
                Text(text = "Lista de Subtarefas (opcional)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                
                // Existing subtasks togglable/deletable (Edit mode)
                if (existingSubtasks.isNotEmpty()) {
                    FlowSubtasksContainer(
                        subtasks = existingSubtasks,
                        onDeleteSubtask = { sub ->
                            existingSubtasks.remove(sub)
                            // If edit mode and already in DB, delete directly via viewmodel
                            if (isEditMode) {
                                viewModel.deleteSubTaskDirectly(sub)
                            }
                        },
                        onToggleSubtask = { sub ->
                            val idx = existingSubtasks.indexOf(sub)
                            if (idx != -1) {
                                existingSubtasks[idx] = sub.copy(isCompleted = !sub.isCompleted)
                            }
                        }
                    )
                }

                // New pending subtasks list (uncommitted to DB yet)
                if (newSubtaskTitles.isNotEmpty()) {
                    newSubtaskTitles.forEachIndexed { index, subTitle ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "• $subTitle",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { newSubtaskTitles.removeAt(index) }, modifier = Modifier.size(24.dp)) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // Add subtask input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentNewSubtaskText,
                        onValueChange = { currentNewSubtaskText = it },
                        placeholder = { Text("Adicionar subtarefa...", fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {
                            if (currentNewSubtaskText.isNotBlank()) {
                                newSubtaskTitles.add(currentNewSubtaskText)
                                currentNewSubtaskText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = CircleShape,
                        modifier = Modifier.height(44.dp)
                    ) {
                        Text(text = "Add", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // Manual progress slider (only shown if there are NO subtasks)
                if (existingSubtasks.isEmpty() && newSubtaskTitles.isEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Progresso Manual: $progressVal%", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Slider(
                        value = progressVal.toFloat(),
                        onValueChange = { progressVal = it.toInt() },
                        valueRange = 0f..100f,
                        steps = 10,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Dialog Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.height(42.dp)
                    ) {
                        Text(text = "Cancelar", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                if (isEditMode && taskWithSubs != null) {
                                    viewModel.updateTaskDetails(
                                        task = taskWithSubs.task,
                                        newTitle = title,
                                        newDescription = description,
                                        newCategory = category,
                                        newDifficulty = difficulty,
                                        newProgress = progressVal,
                                        existingSubtasksToUpdate = existingSubtasks,
                                        newSubtaskTitlesToAdd = newSubtaskTitles
                                    )
                                } else {
                                    viewModel.insertTask(
                                        title = title,
                                        description = description,
                                        category = category,
                                        difficulty = difficulty,
                                        date = selectedDate,
                                        subtaskTitles = newSubtaskTitles
                                    )
                                }
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = CircleShape,
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("dialog_submit_button")
                    ) {
                        Text(text = if (isEditMode) "Salvar" else "Criar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Helper Container to render existing subtasks cleanly
@Composable
fun FlowSubtasksContainer(
    subtasks: List<SubTask>,
    onDeleteSubtask: (SubTask) -> Unit,
    onToggleSubtask: (SubTask) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        subtasks.forEach { subTask ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = subTask.isCompleted,
                        onCheckedChange = { onToggleSubtask(subTask) },
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = subTask.title,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = { onDeleteSubtask(subTask) }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
