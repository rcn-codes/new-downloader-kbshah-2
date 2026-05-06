package com.example.newdownloader26.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.newdownloader26.R

@Composable
fun LanguageScreen(
    selectedTag: String,
    onSelect: (LanguageOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = remember { languageOptions() }
    val scheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(scheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.language_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.language_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = scheme.onBackground.copy(alpha = 0.55f)
        )
        Spacer(Modifier.height(18.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = options, key = { it.languageTag }) { option ->
                LanguageRow(
                    option = option,
                    selected = option.languageTag == selectedTag,
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}

@Composable
private fun LanguageRow(
    option: LanguageOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val interaction = remember { MutableInteractionSource() }

    val container = if (selected) scheme.primary else scheme.surface
    val content = if (selected) scheme.onPrimary else scheme.onSurface
    val secondary = if (selected) scheme.onPrimary.copy(alpha = 0.85f) else scheme.onSurface.copy(alpha = 0.55f)

    val borderColor = if (selected) Color.Transparent else scheme.onSurface.copy(alpha = 0.08f)
    val shape = RoundedCornerShape(28.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(container)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(option.flagRes),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = scheme.onSurface.copy(alpha = 0.10f),
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option.englishName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                color = content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = option.nativeName,
                style = MaterialTheme.typography.bodyMedium,
                color = secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun languageOptions(): List<LanguageOption> = listOf(
    LanguageOption(englishName = "Arabic", nativeName = "العربية", languageTag = "ar", flagRes = R.drawable.sa),
    LanguageOption(englishName = "Chinese", nativeName = "中国人", languageTag = "zh", flagRes = R.drawable.cn),
    LanguageOption(englishName = "English", nativeName = "English", languageTag = "en", flagRes = R.drawable.gb_eng),
    LanguageOption(englishName = "French", nativeName = "Français", languageTag = "fr", flagRes = R.drawable.fr),
    LanguageOption(englishName = "German", nativeName = "Deutsch", languageTag = "de", flagRes = R.drawable.de),
    LanguageOption(englishName = "Hindi", nativeName = "हिंदी", languageTag = "hi", flagRes = R.drawable.hi),
    LanguageOption(englishName = "Japanese", nativeName = "日本語", languageTag = "ja", flagRes = R.drawable.jp),
    LanguageOption(englishName = "Korean", nativeName = "한국어", languageTag = "ko", flagRes = R.drawable.kr),
    LanguageOption(englishName = "Malay", nativeName = "Bahasa Melayu", languageTag = "ms", flagRes = R.drawable.my),
    LanguageOption(englishName = "Portuguese", nativeName = "Português", languageTag = "pt", flagRes = R.drawable.pt),
    LanguageOption(englishName = "Russian", nativeName = "Русский", languageTag = "ru", flagRes = R.drawable.ru),
    LanguageOption(englishName = "Spanish", nativeName = "Español", languageTag = "es", flagRes = R.drawable.es),
    LanguageOption(englishName = "Turkish", nativeName = "Türkçe", languageTag = "tr", flagRes = R.drawable.tr),
    LanguageOption(englishName = "Urdu", nativeName = "اردو", languageTag = "ur", flagRes = R.drawable.pk)
)

