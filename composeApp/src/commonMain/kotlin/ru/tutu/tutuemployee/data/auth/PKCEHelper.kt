package ru.tutu.tutuemployee.data.auth

import io.ktor.util.*
import kotlin.random.Random

/**
 * Helper для генерации PKCE (Proof Key for Code Exchange) параметров
 * Используется для защиты Authorization Code Flow
 */
object PKCEHelper {

    /**
     * Генерировать code verifier
     * Случайная строка 43-128 символов из набора [A-Z] [a-z] [0-9] - . _ ~
     */
    fun generateCodeVerifier(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('-', '.', '_', '~')
        return (1..128)
            .map { allowedChars.random() }
            .joinToString("")
    }

    /**
     * Генерировать code challenge из code verifier
     * SHA256(code_verifier), закодированный в base64url
     */
    suspend fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.encodeToByteArray()
        val sha256 = sha256(bytes)
        return sha256.encodeBase64().trimEnd('=')
            .replace('+', '-')
            .replace('/', '_')
    }

    /**
     * Простая SHA-256 реализация для KMP
     * Примечание: В production лучше использовать нативные криптографические библиотеки
     */
    private suspend fun sha256(bytes: ByteArray): ByteArray {
        // Для production используйте:
        // - Android: MessageDigest.getInstance("SHA-256")
        // - iOS: CommonCrypto
        // - JS: Web Crypto API
        // Здесь используем Ktor Digest
        return Digest("SHA-256").apply {
            plusAssign(bytes)
        }.build()
    }

    /**
     * Генерировать state для защиты от CSRF атак
     */
    fun generateState(): String {
        return Random.nextBytes(32).encodeBase64()
            .replace('+', '-')
            .replace('/', '_')
            .trimEnd('=')
    }
}
