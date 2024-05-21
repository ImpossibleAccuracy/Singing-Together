package com.singing.api.domain.repository;

import com.singing.api.domain.model.CategoryInfoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryInfoRepository : JpaRepository<CategoryInfoEntity, String>
