package renard.remi.ping.domain.use_case

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidatePasswordUseCaseTest {

    private lateinit var useCase: ValidatePasswordUseCase

    @BeforeEach
    fun setUp() {
        useCase = ValidatePasswordUseCase()
    }

    @Test
    fun `Validate password - Nominal case`() {
        useCase.execute("123456") shouldBe true
        useCase.execute("passwordWithUppercase") shouldBe true
        useCase.execute("passwordWithUppercaseAnd123") shouldBe true
    }

    @Test
    fun `Validate password - Error case`() {
        useCase.execute("123") shouldBe false
        useCase.execute("passwordwithoutuppercase") shouldBe false
    }
}