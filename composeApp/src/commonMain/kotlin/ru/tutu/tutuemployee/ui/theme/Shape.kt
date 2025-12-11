package ru.tutu.tutuemployee.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val TutuShapes = Shapes(
    // Малые элементы - чипы, кнопки
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),

    // Средние элементы - карточки
    medium = RoundedCornerShape(12.dp),

    // Крупные элементы - диалоги, модальные окна
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
