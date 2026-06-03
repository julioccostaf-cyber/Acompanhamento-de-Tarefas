package com.example.data

data class TaskPreset(
    val title: String,
    val description: String,
    val category: String, // "CASA" or "ESTUDOS"
    val difficulty: String, // "CANDY_EASY", "CANDY_MEDIUM", "CANDY_HARD"
    val xpReward: Int,
    val emoji: String,
    val subtasks: List<String> = emptyList()
)

object PresetLibrary {
    val presets = listOf(
        TaskPreset(
            title = "Arrumar a Cama",
            description = "Esticar os lençóis e organizar os travesseiros para começar o dia bem!",
            category = "CASA",
            difficulty = "CANDY_EASY",
            xpReward = 30,
            emoji = "🛏️",
            subtasks = listOf("Esticar lençóis", "Arrastar cobertas", "Organizar travesseiros")
        ),
        TaskPreset(
            title = "Lavar a Louça",
            description = "Lavar, secar e guardar os pratos e talheres utilizados.",
            category = "CASA",
            difficulty = "CANDY_MEDIUM",
            xpReward = 60,
            emoji = "🍽️",
            subtasks = listOf("Ensaboar copos e pratos", "Enxaguar a louça", "Secar e guardar nos armários")
        ),
        TaskPreset(
            title = "Estudar 15 Minutos",
            description = "Revisar matéria do dia ou fazer anotações importantes sem distrações.",
            category = "ESTUDOS",
            difficulty = "CANDY_EASY",
            xpReward = 30,
            emoji = "📚",
            subtasks = listOf("Desativar notificações", "Ler material por 15 min", "Anotar 3 ideias chaves")
        ),
        TaskPreset(
            title = "Fazer Dever de Casa",
            description = "Completar os exercícios solicitados hoje na sala de aula.",
            category = "ESTUDOS",
            difficulty = "CANDY_HARD",
            xpReward = 100,
            emoji = "📝",
            subtasks = listOf("Fazer exercícios listados", "Revisar soluções", "Guardar tudo na mochila")
        ),
        TaskPreset(
            title = "Organizar o Quarto",
            description = "Colocar as coisas nos devidos lugares e varrer o chão do quarto.",
            category = "CASA",
            difficulty = "CANDY_HARD",
            xpReward = 100,
            emoji = "🧹",
            subtasks = listOf("Guardar roupas jogadas", "Organizar a mesa de estudos", "Varrer ou espanar a poeira")
        ),
        TaskPreset(
            title = "Passear com o Pet",
            description = "Caminhar ao ar livre por 15-20 minutos com seu pet favorito.",
            category = "CASA",
            difficulty = "CANDY_MEDIUM",
            xpReward = 60,
            emoji = "🐶",
            subtasks = listOf("Pegar sacola higiênica e coleira", "Passear ao ar livre", "Trocar água fresca na volta")
        ),
        TaskPreset(
            title = "Beber Água (2 Litros)",
            description = "Meta diária saudável para manter o foco e hidratação perfeitos.",
            category = "CASA",
            difficulty = "CANDY_EASY",
            xpReward = 30,
            emoji = "💧",
            subtasks = listOf("Beber copo d'água pela manhã", "Copos d'água tarde", "Copo d'água à noite")
        ),
        TaskPreset(
            title = "Leitura de 15 Páginas",
            description = "Leitura tranquila de um livro estimulante para clarear as ideias.",
            category = "ESTUDOS",
            difficulty = "CANDY_MEDIUM",
            xpReward = 60,
            emoji = "📖",
            subtasks = listOf("Escolher local aconchegante", "Ler 15 páginas", "Registrar progresso")
        )
    )
}
