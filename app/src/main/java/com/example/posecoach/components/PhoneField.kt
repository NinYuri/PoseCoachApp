package com.example.posecoach.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorErrorText
import com.example.posecoach.ui.theme.colorWhite

data class Country(
    val code: String,
    val isoCode: String,
    val name: String
)

// Función para obtener bandera
fun getFlag(isoCode: String): String {
    val flagOffset = 0x1F1E6
    val asciiOffset = 0x41
    val firstChar = isoCode[0].code - asciiOffset + flagOffset
    val secondChar = isoCode[1].code - asciiOffset + flagOffset
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}

// Lista de países
val countries = listOf(
    Country("+52", "MX", "México"),
    Country("+1", "US", "Estados Unidos"),
    Country("+57", "CO", "Colombia"),
    Country("+34", "ES", "España"),
    Country("+51", "PE", "Perú"),
    Country("+56", "CL", "Chile"),
    Country("+54", "AR", "Argentina")
)

@Composable
fun PhoneField(
    phone: String,
    onPhoneChange: (String) -> Unit,
    selectedCountry: Country,
    onCountryChange: (Country) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    phoneError: Boolean,
    modifier: Modifier = Modifier
){
    var localExpanded by remember { mutableStateOf(expanded) }

    // Sincronizamos estado local con el externo
    LaunchedEffect(expanded) {
        localExpanded = expanded
    }

    val textField = TextStyle(
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily(Font(R.font.figtree))
    )

    OutlinedTextField(
        value = phone,
        onValueChange = onPhoneChange,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = if(phoneError) colorError else Color.Transparent,
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
                "Número Telefónico",
                style = textField.copy(
                    color = if(phoneError) colorErrorText else Color.Black
                )
            )
        },
        textStyle = textField.copy(
            color = if(phoneError) colorErrorText else Color.Black
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = if(phoneError) colorErrorText else Color.Black,
            unfocusedTextColor = if(phoneError) colorErrorText else Color.Black,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedContainerColor = colorWhite,
            unfocusedContainerColor = colorWhite
        ),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Row(
                modifier = Modifier
                    .padding(start = 11.dp, end = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.phone_wine),
                    contentDescription = "Phone Icon",
                    modifier = Modifier.size(25.dp),
                    tint = if (phoneError) colorError else colorDark
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Selector de País
                Box {
                    Text(
                        text = getFlag(selectedCountry.isoCode) + " " + selectedCountry.code,
                        modifier = Modifier
                            .clickable {
                                localExpanded = true
                                onExpandedChange(true)
                            },
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    DropdownMenu(
                        expanded = localExpanded,
                        onDismissRequest = {
                            localExpanded = false
                            onExpandedChange(false)
                        }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "${getFlag(country.isoCode)} ${country.code}",
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                },
                                onClick = {
                                    onCountryChange(country)
                                    localExpanded = false
                                    onExpandedChange(false)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}