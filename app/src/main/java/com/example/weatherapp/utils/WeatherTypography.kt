package com.example.weatherapp.utils

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.Typography

val weatherTypography = Typography(
    displayLarge = Typography.displayLarge.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 80.sp),
    displayMedium = Typography.displayMedium.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 60.sp),
    displaySmall = Typography.displaySmall.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 40.sp),
    titleLarge = Typography.titleLarge.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 35.sp),
    titleMedium = Typography.titleMedium.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 32.sp),
    titleSmall = Typography.titleSmall.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 30.sp),
    bodyLarge = Typography.bodyLarge.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 25.sp),
    bodyMedium = Typography.bodyMedium.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 22.sp),
    bodySmall = Typography.bodySmall.copy(fontFamily = appFontFamily, color = Color.White, fontSize = 20.sp),
)