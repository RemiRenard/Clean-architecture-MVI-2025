package renard.remi.ping.domain.use_case

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidateUsernameUseCaseTest {

    private lateinit var useCase: ValidateUsernameUseCase

    @BeforeEach
    fun setUp() {
        useCase = ValidateUsernameUseCase()
    }

    @Test
    fun `validate username - Nominal case`() {
        useCase.execute("My username") shouldBe true
    }

    @Test
    fun `validate username - Error case`() {
        useCase.execute(null) shouldBe false
        useCase.execute("") shouldBe false
        useCase.execute("My") shouldBe false
    }
}