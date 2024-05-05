package com.singing.audio

import java.io.File

expect suspend fun getFileDuration(file: File): Long
