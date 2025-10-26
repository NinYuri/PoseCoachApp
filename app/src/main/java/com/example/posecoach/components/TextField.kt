package com.example.posecoach.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorErrorText
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun GenericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLines: Int = 1
){
    val textField = TextStyle(
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily(Font(R.font.figtree))
    )

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 3.dp,
                    color = if(isError) colorError else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(10.dp),
                    clip = false,
                    ambientColor = Color.Black.copy(alpha = 0.7f),
                    spotColor = Color.Black.copy(alpha = 0.7f)
                ),
            placeholder = {
                Text(
                    placeholder,
                    style = textField.copy(
                        color = if(isError) colorErrorText else Color.Black
                    )
                )
            },
            textStyle = textField.copy(
                color = if(isError) colorErrorText else Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = if(isError) colorErrorText else Color.Black,
                unfocusedTextColor = if(isError) colorErrorText else Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedContainerColor = colorWhite,
                unfocusedContainerColor = colorWhite,
                errorContainerColor = colorWhite,
                errorTextColor = colorErrorText,
                errorCursorColor = colorError,
                errorBorderColor = Color.Transparent,
                errorLeadingIconColor = colorError,
                errorTrailingIconColor = colorError
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError
        )
    }
}