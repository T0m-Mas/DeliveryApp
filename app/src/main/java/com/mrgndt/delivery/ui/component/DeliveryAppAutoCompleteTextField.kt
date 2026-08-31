package com.mrgndt.delivery.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryAppAutoCompleteTextField(
    list: List<SelectorItem<String>>,
    value: String = "",
    placeholder: String? = null,
    label: String? = null,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    onValueSelected: (String) -> Unit,
    enabled: Boolean = true,
    validatorStatus: ValidatorStatus = ValidatorStatus.OK,
    validatorMessage: String = "",
    onBlur: () -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocus by interactionSource.collectIsFocusedAsState()

    var isExpanded = (isFocus && value.length >= 4 && list.isNotEmpty())

    @Composable
    fun colorBasedOnValidatorStatus(default: Color = Color.Unspecified): Color {
        return when (validatorStatus) {
            ValidatorStatus.OK -> default
            ValidatorStatus.WRONG -> MaterialTheme.colorScheme.error
            ValidatorStatus.WARNING -> MaterialTheme.colorScheme.primary
        }
    }

    var isBlurred: Boolean by remember {
        mutableStateOf(false)
    }

    Column {
        if (enabled) {
            if (label != null) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = label,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {
                    isExpanded = it
                }
            ) {
                ExposedDropdownMenu(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 256.dp)
                            .scrollable(
                                rememberScrollState(),
                                Orientation.Vertical
                            ),
                    expanded = isExpanded,
                    onDismissRequest = { },
                ) {
                    list.forEach {
                        DropdownMenuItem(
                            modifier = Modifier.padding(top = 4.dp),
                            text = {
                                Text(text = it.label)
                            },
                            onClick = {
                                onValueChange(it.label)
                                onValueSelected(it.value)
                            }
                        )
                    }
                }
                BasicTextField(
                    modifier = Modifier
                        .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 1.dp,
                            color =
                                if (isFocus) MaterialTheme.colorScheme.primary
                                else colorBasedOnValidatorStatus(MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (!it.isFocused && isBlurred) onBlur()
                            if (it.isFocused && !isBlurred) isBlurred = true
                        }
                        .padding(
                            vertical = 12.dp,
                            horizontal = 8.dp,
                        ),
                    value = value,
                    readOnly = readOnly,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = singleLine,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    onValueChange = {
                        onValueChange(it)
                    },
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    textStyle = TextStyle(
                        color =
                            if (isFocus)
                                MaterialTheme.colorScheme.primary
                            else
                                colorBasedOnValidatorStatus(MaterialTheme.colorScheme.onSurface),
                        fontSize = 18.sp,
                    ),
                    enabled = true,
                ) {
                    if (value.isEmpty() && placeholder != null && !isFocus) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        it()
                    }
                }
            }
            if (validatorStatus != ValidatorStatus.OK) {
                Row {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = validatorMessage,
                        color = colorBasedOnValidatorStatus(),
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            DeliveryAppTextField(
                label = label,
                value = value,
                readOnly = true,
                onValueChange = {},
                enabled = false
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TextInputAutoCompleteTextFieldPreview() {
    DeliveryAppAutoCompleteTextField(
        value = "text value",
        onValueChange = {},
        list = listOf(
            SelectorItem("tst", "test"),
            SelectorItem("tst", "test"),
            SelectorItem("tst", "test"),
            SelectorItem("tst", "test"),
            SelectorItem("tst", "test"),
            SelectorItem("tst", "test"),
            SelectorItem("tst", "test"),
            SelectorItem("tst", "test"),
        ),
        onValueSelected = { }
    )
}

data class SelectorItem<T>(
    val label: String,
    val value: T,
)
