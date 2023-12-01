package com.whatever.raisedragon.common.config

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.stereotype.Component
import org.springframework.test.context.junit.jupiter.SpringExtension

@Disabled
@ExtendWith(SpringExtension::class)
class EncryptionTest {

    @Value("\${jasypt.encryptor.password}")
    lateinit var jasyptEncryptorPassword: String

    @Autowired
    lateinit var configurableEnvironment: ConfigurableEnvironment

    lateinit var encryptor: DefaultLazyEncryptor

    @BeforeEach
    internal fun setUp() {
        check(jasyptEncryptorPassword.isNotBlank()) {
            "실행 매개변수에 jasypt.encryptor.password 를 넣어주세요"
        }
        encryptor = DefaultLazyEncryptor(configurableEnvironment)
    }

    @Test
    fun testForEncryption() {
        val source = "test"
        println("source: $source")
        println("encrypted: ${encryptor.encrypt(source)}")
    }

    @Test
    fun testForDecryption() {
        val source = "test"
        println("source: $source")
        println("decrypted: ${encryptor.decrypt(source)}")
    }
}
