package org.lasalle.recipeapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.lasalle.recipeapp.models.RecipePreview
import org.lasalle.recipeapp.ui.HomeScreenRoute
import org.lasalle.recipeapp.ui.LoginScreenRoute
import org.lasalle.recipeapp.ui.screens.home.components.LoadingOverlay
import org.lasalle.recipeapp.ui.screens.home.components.RecipeCard
import org.lasalle.recipeapp.ui.screens.home.components.RecipeListItem
import org.lasalle.recipeapp.ui.viewmodels.HomeViewModel
import org.lasalle.recipeapp.utils.hideKeyboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val colors = MaterialTheme.colorScheme
    val container = if (isSystemInDarkTheme()) colors.surface else Color.White
    val vm: HomeViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    var showLogoutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(15.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hola",
                        color = colors.onSurfaceVariant,
                    )
                    Text(
                        text = vm.userName,
                        color = colors.onSurface,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(color = colors.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = vm.userName.take(1), color = colors.primary)
                }

                IconButton(onClick = {
                    showLogoutDialog = true
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = colors.primary
                    )
                }
            }
        }

        // Generar receta
        item {
            Spacer(Modifier.height(15.dp))
            Text(
                text = "Crea, cocina, comparte y disfruta",
                color = colors.onSurface
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.ingredients,
                onValueChange = { vm.ingredients = it },
                shape = CircleShape,
                singleLine = true,
                placeholder = { Text("Escribe tus ingredientes...") },
                trailingIcon = {
                    IconButton(onClick = {
                        hideKeyboard(focusManager)
                        vm.generateRecipe() {
                            scope.launch {
                                sheetState.partialExpand()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Generar receta",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(colors.primary)
                                .padding(5.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = container,
                    unfocusedContainerColor = container,
                    disabledContainerColor = container,
                    errorContainerColor = container,
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.primary.copy(alpha = 0.6f),
                    cursorColor = colors.primary,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedPlaceholderColor = colors.onSurfaceVariant,
                    unfocusedPlaceholderColor = colors.onSurfaceVariant
                )
            )
        }

        // Recetas recientes
        item {
            Text(text = "Tus recetas recientes")
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(vm.recentRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            scope.launch {
                                val recipePreview = RecipePreview(
                                    title = recipe.title,
                                    category = recipe.category,
                                    imageUrl = recipe.imageUrl,
                                    ingredients = recipe.ingredients,
                                    instructions = recipe.instructions,
                                    minutes = recipe.minutes,
                                    stars = recipe.stars,
                                    prompt = ""
                                )
                                vm.showModalFromList(recipePreview)
                                if (!sheetState.isVisible) sheetState.show()
                            }
                        }
                    )
                }
            }
        }

        // Ideas rápidas
        item {
            val tags = listOf(
                "Rápidas (10 min)",
                "Pocas calorías",
                "Sin horno",
                "Desayunos"
            )

            Text(
                text = "Ideas rápidas",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tags) { tag ->
                    Text(
                        text = tag,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        color = colors.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        // No sabes que cocinar hoy?
        item {
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(colors.primary.copy(alpha = 0.1f))
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿No sabes que cocinar hoy?",
                        color = colors.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Genera una receta aleatoria",
                        color = colors.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Generar Receta",
                    tint = colors.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        // Todas tus recetas
        item {
            Text(
                text = "Todas tus recetas",
                color = colors.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(15.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                vm.recipes.forEach { recipe ->
                    RecipeListItem(
                        recipe = recipe,
                        onClick = {
                            scope.launch {
                                val preview = RecipePreview(
                                    title = recipe.title,
                                    category = recipe.category,
                                    imageUrl = recipe.imageUrl,
                                    ingredients = recipe.ingredients,
                                    instructions = recipe.instructions,
                                    minutes = recipe.minutes,
                                    stars = recipe.stars,
                                    prompt = ""
                                )
                                vm.showModalFromList(preview)
                                sheetState.partialExpand()
                            }
                        }
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }

    if (vm.isLoading) {
        LoadingOverlay()
    }

    // Modal de receta generada
    if (vm.showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    vm.showSheet = false
                    sheetState.hide()
                }
            },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = vm.generatedRecipe?.imageUrl,
                    contentDescription = vm.generatedRecipe?.title ?: "Sin título",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = vm.generatedRecipe?.title ?: "Sin título",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.onSurface,
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.primary.copy(alpha = 0.15f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = colors.primary,
                    )
                    Text(
                        text = "${vm.generatedRecipe?.stars}",
                        color = colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time Icon",
                        tint = colors.primary,
                    )
                    Text(
                        text = "${vm.generatedRecipe?.minutes} min",
                        color = colors.onSurface,
                    )

                    Text(
                        text = "${vm.generatedRecipe?.category}",
                        color = colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Preparación:",
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val ingredients = vm.generatedRecipe?.ingredients ?: listOf()
                    ingredients.forEach { ingredient ->
                        Text(
                            text = ingredient,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(colors.primary.copy(alpha = 0.1f))
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            color = colors.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Instrucciones:",
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold
                )

                val instructions = vm.generatedRecipe?.instructions ?: listOf()

                instructions.forEachIndexed { index, instruction ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${index + 1}. ",
                            color = colors.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = instruction,
                            color = colors.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                vm.hidemodal()
                            }

                            if (!vm.isGenerated) {
                                vm.saveRecipeInDb()
                                vm.isGenerated = true
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (!vm.isGenerated) "Guardar" else "Cerrar",
                            color = colors.onPrimary
                        )
                    }
                }
            }
        }
    }

    // ALERT DIALOG — confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirmar cierre de sesión") },
            text = { Text("¿Deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        vm.logout()
                        navController.navigate(LoginScreenRoute) {
                            popUpTo(HomeScreenRoute) { inclusive = true }
                        }
                    }
                ) {
                    Text("Cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
