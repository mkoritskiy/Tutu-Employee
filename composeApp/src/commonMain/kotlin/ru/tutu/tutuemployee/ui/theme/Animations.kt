package ru.tutu.tutuemployee.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable

/**
 * Стандартные анимации для приложения
 */
object TutuAnimations {

    /**
     * Стандартная длительность анимации (быстрая)
     */
    const val FAST_DURATION = 150

    /**
     * Средняя длительность анимации
     */
    const val MEDIUM_DURATION = 300

    /**
     * Медленная длительность анимации
     */
    const val SLOW_DURATION = 500

    /**
     * Стандартная easing функция
     */
    val standardEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)

    /**
     * Easing для появления элементов
     */
    val enterEasing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)

    /**
     * Easing для исчезновения элементов
     */
    val exitEasing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    /**
     * Стандартный spring spec для анимаций
     */
    val standardSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    /**
     * Анимация появления снизу вверх
     */
    fun slideInFromBottom() = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(MEDIUM_DURATION, easing = enterEasing)
    ) + fadeIn(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Анимация исчезновения сверху вниз
     */
    fun slideOutToBottom() = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(MEDIUM_DURATION, easing = exitEasing)
    ) + fadeOut(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Анимация появления справа налево
     */
    fun slideInFromRight() = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(MEDIUM_DURATION, easing = enterEasing)
    ) + fadeIn(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Анимация исчезновения слева направо
     */
    fun slideOutToRight() = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(MEDIUM_DURATION, easing = exitEasing)
    ) + fadeOut(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Анимация появления слева направо
     */
    fun slideInFromLeft() = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(MEDIUM_DURATION, easing = enterEasing)
    ) + fadeIn(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Анимация исчезновения справа налево
     */
    fun slideOutToLeft() = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(MEDIUM_DURATION, easing = exitEasing)
    ) + fadeOut(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Анимация появления с увеличением (scale)
     */
    fun scaleIn() = scaleIn(
        initialScale = 0.8f,
        animationSpec = tween(MEDIUM_DURATION, easing = enterEasing)
    ) + fadeIn(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Анимация исчезновения с уменьшением (scale)
     */
    fun scaleOut() = scaleOut(
        targetScale = 0.8f,
        animationSpec = tween(MEDIUM_DURATION, easing = exitEasing)
    ) + fadeOut(animationSpec = tween(MEDIUM_DURATION))

    /**
     * Простая анимация появления (fade in)
     */
    fun fadeIn() = fadeIn(
        animationSpec = tween(MEDIUM_DURATION, easing = enterEasing)
    )

    /**
     * Простая анимация исчезновения (fade out)
     */
    fun fadeOut() = fadeOut(
        animationSpec = tween(MEDIUM_DURATION, easing = exitEasing)
    )
}

/**
 * Модификатор для анимации нажатия (bounce effect)
 * Примечание: Используйте Card с onClick для автоматической анимации нажатия
 */

/**
 * Infinite rotation animation
 */
@Composable
fun rememberInfiniteRotation(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    return rotation.value
}

/**
 * Pulse animation (для привлечения внимания)
 */
@Composable
fun rememberPulseAnimation(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    return scale.value
}
