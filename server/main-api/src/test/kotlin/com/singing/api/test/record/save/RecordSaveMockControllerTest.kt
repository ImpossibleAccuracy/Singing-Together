package com.singing.api.test.record.save

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.singing.api.controller.record.RecordSaveController
import com.singing.api.domain.exception.ParsingCancellationException
import com.singing.api.domain.model.TestFile
import com.singing.api.domain.model.TestUser
import com.singing.api.domain.model.UnacceptableTestFile
import com.singing.api.domain.store.TestFileStore
import com.singing.api.domain.store.TestUserStore
import com.singing.api.domain.toDto
import com.singing.api.hooks.authorize
import com.singing.api.service.record.RecordService
import com.singing.api.service.record.data.RecordDataService
import com.singing.api.service.storage.FileStorageService
import com.singing.api.service.storage.StorageCatalog
import com.singing.api.test.AbstractWebTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.multipart
import kotlin.test.assertNotNull


@ExtendWith(SpringExtension::class, MockKExtension::class)
@WebMvcTest(RecordSaveController::class)
class RecordSaveMockControllerTest : AbstractWebTest() {
    companion object {
        @JvmStatic
        fun testUsers() =
            TestUserStore.users
                .map { Arguments.of(it) }
                .stream()

        @JvmStatic
        fun regularFiles() =
            TestFileStore.files
                .map { Arguments.of(it) }
                .stream()

        @JvmStatic
        fun filesWithUsers() =
            TestFileStore.files
                .flatMap { file ->
                    TestUserStore.users.map { file to it }
                }
                .map { Arguments.of(it.first, it.second) }
                .stream()

        @JvmStatic
        fun multiplyFilesWithUsers() =
            mutableListOf<Pair<TestFile, TestFile>>()
                .apply {
                    val items = TestFileStore.files

                    for (i in 0..<items.size - 1) {
                        for (k in i..<items.size) {
                            add(items[i] to items[k])
                        }
                    }
                }
                .flatMap { file ->
                    TestUserStore.users.map { file to it }
                }
                .map { Arguments.of(it.first.first, it.first.second, it.second) }
                .stream()

        @JvmStatic
        fun singleUnacceptableFiles() =
            TestFileStore.unacceptableFiles
                .flatMap { file ->
                    TestUserStore.users.map { file to it }
                }
                .map { Arguments.of(it.first, it.second) }
                .stream()

        @JvmStatic
        fun unacceptableFiles() =
            mutableListOf<Pair<UnacceptableTestFile, UnacceptableTestFile?>>()
                .apply {
                    val items = TestFileStore.unacceptableFiles

                    for (i in 0..<items.size - 1) {
                        add(items[i] to null)

                        for (k in i..<items.size) {
                            add(items[i] to items[k])
                        }
                    }
                }
                .map { Arguments.of(it.first, it.second) }.stream()
    }

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean(relaxUnitFun = true)
    private lateinit var storageService: FileStorageService

    @MockkBean(relaxUnitFun = true)
    private lateinit var recordDataService: RecordDataService

    @MockkBean(relaxUnitFun = true)
    private lateinit var recordService: RecordService


    /// ----------------- NEGATIVE TESTS -----------------

    @ParameterizedTest
    @MethodSource("testUsers")
    fun createNewRecord_noFileSet_fails400(user: TestUser) {
        mockMvc
            .multipart("/record") {
                authorize(user)
            }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @ParameterizedTest
    @MethodSource("singleUnacceptableFiles")
    fun createNewRecord_onlyTrackSpecified_fails400(file: UnacceptableTestFile, user: TestUser) {
        val actualFile = file.get()

        mockMvc
            .multipart("/record") {
                file(
                    MockMultipartFile(
                        "track",
                        null,
                        file.mimeType,
                        actualFile.readBytes()
                    )
                )
            }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @ParameterizedTest
    @MethodSource("regularFiles")
    fun createNewRecord_fileSetAndNoAuth_failsUnauthorized(testFile: TestFile) {
        mockMvc
            .multipart("/record") {
                multipart("voice", testFile)
            }
            .asyncDispatch()
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @ParameterizedTest
    @MethodSource("unacceptableFiles")
    fun createNewRecord_givenUnacceptableFile_fails400(first: UnacceptableTestFile, second: UnacceptableTestFile?) {
        val firstFile = first.get()
        val secondFile = second?.get?.invoke()

        mockMvc
            .multipart("/record") {
                authorize(TestUserStore.AUTH_TOKEN_USER1)

                file(
                    MockMultipartFile(
                        "voice",
                        null,
                        first.mimeType,
                        firstFile.readBytes()
                    )
                )

                if (second != null) {
                    file(
                        MockMultipartFile(
                            "track",
                            null,
                            second.mimeType,
                            secondFile!!.readBytes()
                        )
                    )
                }
            }
            .asyncDispatch()
            .andExpect {
                status { isBadRequest() }
            }
    }

    /// ----------------- POSITIVE TESTS -----------------

    @ParameterizedTest
    @MethodSource("filesWithUsers")
    fun createNewRecord_givenSingleValidFile_allFunctionsCalledAndReturnsSuccess(
        testFile: TestFile,
        user: TestUser
    ) {
        prepareRequest(
            mockMvc = mockMvc,
            firstFile = testFile,
            user = user,
            setup = { scope, _ ->
                val points = scope.entity.points.toList()

                coEvery { storageService.store(scope.multipart, StorageCatalog.Temp) } returns scope.file

                coEvery { recordDataService.buildRecord(scope.file, null) } returns points
                coEvery { recordDataService.getDuration(scope.file) } returns scope.entity.duration!!

                coEvery { storageService.findDocumentOrCreate(scope.file) } returns scope.entity.voiceRecord!!

                coEvery { recordService.save(any()) } returns scope.entity
                coEvery { storageService.delete(scope.file, StorageCatalog.Temp) } returns true
            },
            expected = { scope, _ ->
                val expectedJsonResult = objectMapper.writeValueAsString(scope.entity.toDto())

                status { isOk() }

                content {
                    json(expectedJsonResult)
                }
            },
            verify = { scope, _ ->
                val points = scope.entity.points.toList()

                coVerify { storageService.store(scope.multipart, StorageCatalog.Temp) }

                coVerify { recordDataService.buildRecord(scope.file, null) }
                coVerify { recordDataService.getDuration(scope.file) }

                coVerify { storageService.findDocumentOrCreate(scope.file) }

                coVerify { recordService.save(any()) }
                coVerify { storageService.delete(scope.file, StorageCatalog.Temp) }
            }
        )
    }

    @ParameterizedTest
    @MethodSource("multiplyFilesWithUsers")
    fun createNewRecord_givenMultiplyValidFiles_allFunctionsCalledAndReturnsSuccess(
        firstFile: TestFile,
        secondFile: TestFile,
        user: TestUser
    ) {
        prepareRequest(
            mockMvc = mockMvc,
            firstFile = firstFile,
            secondFile = secondFile,
            user = user,
            setup = { first, second ->
                assertNotNull(second)
                val points = first.entity.points.toList()

                coEvery { storageService.store(first.multipart, StorageCatalog.Temp) } returns first.file
                coEvery { storageService.store(second.multipart, StorageCatalog.Temp) } returns second.file

                coEvery { recordDataService.buildRecord(first.file, second.file) } returns points
                coEvery { recordDataService.getDuration(first.file) } returns first.entity.duration!!
                coEvery { recordDataService.computeAccuracy(points) } returns first.entity.accuracy!!

                coEvery { storageService.findDocumentOrCreate(first.file) } returns first.entity.voiceRecord!!
                coEvery { storageService.findDocumentOrCreate(second.file) } returns second.entity.voiceRecord!!

                coEvery { recordService.save(any()) } returns first.entity

                coEvery { storageService.delete(first.file, StorageCatalog.Temp) } returns true
                coEvery { storageService.delete(second.file, StorageCatalog.Temp) } returns true
            },
            expected = { scope, _ ->
                val expectedJsonResult = objectMapper.writeValueAsString(scope.entity.toDto())

                status { isOk() }

                content {
                    json(expectedJsonResult)
                }
            },
            verify = { first, second ->
                assertNotNull(second)
                val points = first.entity.points.toList()

                coVerify { storageService.store(first.multipart, StorageCatalog.Temp) }
                coVerify { storageService.store(second.multipart, StorageCatalog.Temp) }

                coVerify { recordDataService.buildRecord(first.file, second.file) }
                coVerify { recordDataService.getDuration(first.file) }
                coVerify { recordDataService.computeAccuracy(points) }

                coVerify { storageService.findDocumentOrCreate(first.file) }
                coVerify { storageService.findDocumentOrCreate(second.file) }

                coVerify { recordService.save(any()) }

                coVerify { storageService.delete(first.file, StorageCatalog.Temp) }
                coVerify { storageService.delete(second.file, StorageCatalog.Temp) }
            }
        )
    }

    @ParameterizedTest
    @MethodSource("filesWithUsers")
    fun createNewRecord_givenLongFile_failsWithError(
        testFile: TestFile,
        user: TestUser
    ) {
        prepareRequest(
            mockMvc = mockMvc,
            firstFile = testFile,
            user = user,
            setup = { scope, _ ->
                coEvery { storageService.store(scope.multipart, StorageCatalog.Temp) } returns scope.file
                coEvery { recordDataService.buildRecord(scope.file, null) } throws ParsingCancellationException()
                coEvery { recordDataService.getDuration(scope.file) } returns scope.entity.duration!!
                coEvery { storageService.delete(scope.file, StorageCatalog.Temp) } returns true
            },
            expected = { _, _ ->
                status { isBadRequest() }
            },
            verify = { scope, _ ->
                coVerify { storageService.store(scope.multipart, StorageCatalog.Temp) }
                coVerify { recordDataService.buildRecord(scope.file, null) }
                coVerify { storageService.delete(scope.file, StorageCatalog.Temp) }
            }
        )
    }

    @ParameterizedTest
    @MethodSource("multiplyFilesWithUsers")
    fun createNewRecord_givenValidFileAndThrowsUnknownError_tempFilesDeleted(
        firstFile: TestFile,
        secondFile: TestFile,
        user: TestUser
    ) {
        prepareRequest(
            mockMvc = mockMvc,
            firstFile = firstFile,
            secondFile = secondFile,
            user = user,
            setup = { first, second ->
                assertNotNull(second)

                coEvery { storageService.store(first.multipart, StorageCatalog.Temp) } returns first.file
                coEvery { storageService.store(second.multipart, StorageCatalog.Temp) } returns second.file

                coEvery { recordDataService.buildRecord(first.file, second.file) } throws
                        RuntimeException("Sample mock error")
                coEvery { recordDataService.getDuration(first.file) } returns first.entity.duration!!

                coEvery { storageService.delete(first.file, StorageCatalog.Temp) } returns true
                coEvery { storageService.delete(second.file, StorageCatalog.Temp) } returns true
            },
            expected = { _, _ ->
                status { isInternalServerError() }
            },
            verify = { first, second ->
                assertNotNull(second)

                coVerify { storageService.store(first.multipart, StorageCatalog.Temp) }
                coVerify { storageService.store(second.multipart, StorageCatalog.Temp) }

                coVerify { recordDataService.buildRecord(first.file, second.file) }

                coVerify { storageService.delete(first.file, StorageCatalog.Temp) }
                coVerify { storageService.delete(second.file, StorageCatalog.Temp) }
            }
        )
    }

    private fun prepareRequest(
        mockMvc: MockMvc,
        user: TestUser,
        firstFile: TestFile,
        secondFile: TestFile? = null,
        setup: (RecordSaveRequestScope, RecordSaveRequestScope?) -> Unit,
        expected: MockMvcResultMatchersDsl.(RecordSaveRequestScope, RecordSaveRequestScope?) -> Unit,
        verify: (RecordSaveRequestScope, RecordSaveRequestScope?) -> Unit,
    ) {
        val firstScope = RecordSaveRequestScope.of(firstFile, user, "voice")
        val secondScope = secondFile?.let { RecordSaveRequestScope.of(it, user, "track") }

        setup(firstScope, secondScope)

        mockMvc
            .multipart("/record") {
                authorize(user)

                file(firstScope.multipart)

                if (secondScope != null) {
                    file(secondScope.multipart)
                }
            }
            .asyncDispatch()
            .andExpect {
                expected(firstScope, secondScope)
            }

        verify(firstScope, secondScope)
    }
}
