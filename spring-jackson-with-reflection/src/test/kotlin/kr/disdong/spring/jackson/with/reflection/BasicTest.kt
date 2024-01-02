package kr.disdong.spring.jackson.with.reflection

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BasicTest {

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Nested
    @DisplayName("objectMapper 는 기본적으로 리플렉션을 이용하여 public 필드 혹은 public 형태의 getX 메서드만 접근가능하므로")
    inner class VisibilitySerializeTest {
        @Test
        fun `필드가 public 인 경우에만 정상적으로 직렬화한다`() {
            val user = PublicUser("disdong", 21)
            assertTrue(objectMapper.writeValueAsString(user).contains("""{"name":"disdong","age":21,"nameAndAge":"disdong, 21"}"""))
        }

        @Test
        fun `필드가 private 인 경우 직렬화하지 못한다`() {
            val user = PrivateUser("disdong", 21)
            assertEquals(objectMapper.writeValueAsString(user), "{}")
        }
    }

    @Nested
    @DisplayName("objectMapper 는 리플렉션을 이용하여 역직렬화하므로")
    inner class DeserializeTest {
        @Test
        fun `기본생성자가 없으면 역직렬화를 할 수 없다`() {
            val json = """{"name":"disdong","age":21}"""
            assertThrows(InvalidDefinitionException::class.java) {
                objectMapper.readValue(json, PublicUser::class.java)
            }
        }

        @Test
        fun `기본생성자가 있으면 역직렬화를 할 수 있다`() {
            val json = """{"name":"disdong","age":21}"""
            println(objectMapper.readValue(json, PublicUserWithConstructor::class.java))
        }
    }

    class PublicUser(
        val name: String,
        val age: Int,
    ) {
        fun getNameAndAge(): String {
            return "$name, $age"
        }

        private fun getAgeAndName(): String {
            return "$age, $name"
        }
    }

    class PublicUserWithConstructor(
        val name: String,
        val age: Int,
    ) {
        constructor() : this("", 0)

        fun getNameAndAge(): String {
            return "$name, $age"
        }

        private fun getAgeAndName(): String {
            return "$age, $name"
        }
    }

    class PrivateUser(
        private val name: String,
        private val age: Int,
    )
}
