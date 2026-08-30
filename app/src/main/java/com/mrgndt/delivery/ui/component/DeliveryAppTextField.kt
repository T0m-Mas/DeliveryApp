package com.mrgndt.delivery.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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


@Composable
fun DeliveryAppTextField(
    value: String = "",
    placeholder: String? = null,
    label: String? = null,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    validatorStatus: ValidatorStatus = ValidatorStatus.OK,
    onBlur: () -> Unit = {},
    validatorMessage: String = "",
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocus by interactionSource.collectIsFocusedAsState()

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


    BasicTextField(
        modifier = Modifier
            .onFocusChanged {
                if (enabled && !readOnly) {
                    if (!it.isFocused && isBlurred) onBlur()
                    if (it.isFocused && !isBlurred) isBlurred = true
                }
            },
        value = value,
        readOnly = readOnly,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        textStyle = TextStyle(
            color = if (enabled)
                colorBasedOnValidatorStatus(MaterialTheme.colorScheme.onSurface)
            else
                MaterialTheme.colorScheme.outlineVariant,
            fontSize = 18.sp,
        ),
        enabled = enabled,
        maxLines = maxLines,
        minLines = minLines,
    ) {
        Column {
            if (label != null) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = label,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Row(
                modifier = Modifier
                    .imePadding()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color =
                            if (isFocus && !readOnly) MaterialTheme.colorScheme.primary
                            else colorBasedOnValidatorStatus(MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp,
                        horizontal = 8.dp,
                    )
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
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DeliveryAppTextFieldPreview() {
    DeliveryAppTextField(
        value = "",
        onValueChange = {},
        placeholder = "Hola"
    )
}