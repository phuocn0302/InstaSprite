@file:Suppress("PropertyName", "ClassName", "unused")
package com.olaz.instasprite.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

private interface CatppuccinColors {
    val Rosewater: Color
    val Flamingo: Color
    val Pink: Color
    val Mauve: Color
    val Red: Color
    val Maroon: Color
    val Peach: Color
    val Yellow: Color
    val Green: Color
    val Teal: Color
    val Sky: Color
    val Sapphire: Color
    val Blue: Color
    val Lavender: Color
    val Text: Color
    val Subtext1: Color
    val Subtext0: Color
    val Overlay2: Color
    val Overlay1: Color
    val Overlay0: Color
    val Surface2: Color
    val Surface1: Color
    val Surface0: Color
    val Base: Color
    val Mantle: Color
    val Crust: Color
}

object Catppuccin {
    object Latte : CatppuccinColors {
        override val Rosewater: Color = Color(0xffdc8a78)
        override val Flamingo: Color = Color(0xffdd7878)
        override val Pink: Color = Color(0xffea76cb)
        override val Mauve: Color = Color(0xff8839ef)
        override val Red: Color = Color(0xffd20f39)
        override val Maroon: Color = Color(0xffe64553)
        override val Peach: Color = Color(0xfffe640b)
        override val Yellow: Color = Color(0xffdf8e1d)
        override val Green: Color = Color(0xff40a02b)
        override val Teal: Color = Color(0xff179299)
        override val Sky: Color = Color(0xff04a5e5)
        override val Sapphire: Color = Color(0xff209fb5)
        override val Blue: Color = Color(0xff1e66f5)
        override val Lavender: Color = Color(0xff7287fd)
        override val Text: Color = Color(0xff4c4f69)
        override val Subtext1: Color = Color(0xff5c5f77)
        override val Subtext0: Color = Color(0xff6c6f85)
        override val Overlay2: Color = Color(0xff7c7f93)
        override val Overlay1: Color = Color(0xff8c8fa1)
        override val Overlay0: Color = Color(0xff9ca0b0)
        override val Surface2: Color = Color(0xffacb0be)
        override val Surface1: Color = Color(0xffbcc0cc)
        override val Surface0: Color = Color(0xffccd0da)
        override val Base: Color = Color(0xffeff1f5)
        override val Mantle: Color = Color(0xffe6e9ef)
        override val Crust: Color = Color(0xffdce0e8)
    }

    object Frappe : CatppuccinColors {
        override val Rosewater: Color = Color(0xfff2d5cf)
        override val Flamingo: Color = Color(0xffeebebe)
        override val Pink: Color = Color(0xfff4b8e4)
        override val Mauve: Color = Color(0xffca9ee6)
        override val Red: Color = Color(0xffe78284)
        override val Maroon: Color = Color(0xffea999c)
        override val Peach: Color = Color(0xffef9f76)
        override val Yellow: Color = Color(0xffe5c890)
        override val Green: Color = Color(0xffa6d189)
        override val Teal: Color = Color(0xff81c8be)
        override val Sky: Color = Color(0xff99d1db)
        override val Sapphire: Color = Color(0xff85c1dc)
        override val Blue: Color = Color(0xff8caaee)
        override val Lavender: Color = Color(0xffbabbf1)
        override val Text: Color = Color(0xffc6d0f5)
        override val Subtext1: Color = Color(0xffb5bfe2)
        override val Subtext0: Color = Color(0xffa5adce)
        override val Overlay2: Color = Color(0xff949cbb)
        override val Overlay1: Color = Color(0xff838ba7)
        override val Overlay0: Color = Color(0xff737994)
        override val Surface2: Color = Color(0xff626880)
        override val Surface1: Color = Color(0xff51576d)
        override val Surface0: Color = Color(0xff414559)
        override val Base: Color = Color(0xff303446)
        override val Mantle: Color = Color(0xff292c3c)
        override val Crust: Color = Color(0xff232634)
    }

    object Macchiato : CatppuccinColors {
        override val Rosewater: Color = Color(0xfff4dbd6)
        override val Flamingo: Color = Color(0xfff0c6c6)
        override val Pink: Color = Color(0xfff5bde6)
        override val Mauve: Color = Color(0xffc6a0f6)
        override val Red: Color = Color(0xffed8796)
        override val Maroon: Color = Color(0xffee99a0)
        override val Peach: Color = Color(0xfff5a97f)
        override val Yellow: Color = Color(0xffeed49f)
        override val Green: Color = Color(0xffa6da95)
        override val Teal: Color = Color(0xff8bd5ca)
        override val Sky: Color = Color(0xff91d7e3)
        override val Sapphire: Color = Color(0xff7dc4e4)
        override val Blue: Color = Color(0xff8aadf4)
        override val Lavender: Color = Color(0xffb7bdf8)
        override val Text: Color = Color(0xffcad3f5)
        override val Subtext1: Color = Color(0xffb8c0e0)
        override val Subtext0: Color = Color(0xffa5adcb)
        override val Overlay2: Color = Color(0xff939ab7)
        override val Overlay1: Color = Color(0xff8087a2)
        override val Overlay0: Color = Color(0xff6e738d)
        override val Surface2: Color = Color(0xff5b6078)
        override val Surface1: Color = Color(0xff494d64)
        override val Surface0: Color = Color(0xff363a4f)
        override val Base: Color = Color(0xff24273a)
        override val Mantle: Color = Color(0xff1e2030)
        override val Crust: Color = Color(0xff181926)
    }

    object Mocha : CatppuccinColors {
        override val Rosewater: Color = Color(0xfff5e0dc)
        override val Flamingo: Color = Color(0xfff2cdcd)
        override val Pink: Color = Color(0xfff5c2e7)
        override val Mauve: Color = Color(0xffcba6f7)
        override val Red: Color = Color(0xfff38ba8)
        override val Maroon: Color = Color(0xffeba0ac)
        override val Peach: Color = Color(0xfffab387)
        override val Yellow: Color = Color(0xfff9e2af)
        override val Green: Color = Color(0xffa6e3a1)
        override val Teal: Color = Color(0xff94e2d5)
        override val Sky: Color = Color(0xff89dceb)
        override val Sapphire: Color = Color(0xff74c7ec)
        override val Blue: Color = Color(0xff89b4fa)
        override val Lavender: Color = Color(0xffb4befe)
        override val Text: Color = Color(0xffcdd6f4)
        override val Subtext1: Color = Color(0xffbac2de)
        override val Subtext0: Color = Color(0xffa6adc8)
        override val Overlay2: Color = Color(0xff9399b2)
        override val Overlay1: Color = Color(0xff7f849c)
        override val Overlay0: Color = Color(0xff6c7086)
        override val Surface2: Color = Color(0xff585b70)
        override val Surface1: Color = Color(0xff45475a)
        override val Surface0: Color = Color(0xff313244)
        override val Base: Color = Color(0xff1e1e2e)
        override val Mantle: Color = Color(0xff181825)
        override val Crust: Color = Color(0xff11111b)
    }

    var CurrentPalette = Mocha
}

object CatppuccinUI {
    var CurrentPalette = Catppuccin.CurrentPalette

    val BackgroundColor = CurrentPalette.Base
    val BackgroundColorDarker = CurrentPalette.Crust

    val Foreground0Color = CurrentPalette.Surface0
    val Foreground1Color = CurrentPalette.Surface1
    val Foreground2Color = CurrentPalette.Surface2

    val SelectedColor = CurrentPalette.Mauve

    val TextColorLight = CurrentPalette.Text
    val Subtext0Color = CurrentPalette.Overlay1
    val Subtext1Color = CurrentPalette.Overlay2
    val TextColorDark = CurrentPalette.Mantle

    val AccentButtonColor = CurrentPalette.Green
    val DismissButtonColor = CurrentPalette.Red

    val TopBarColor = CurrentPalette.Mantle
    val BottomBarColor = CurrentPalette.Mantle

    val DropDownMenuColor = CurrentPalette.Mantle
    val DialogColor = CurrentPalette.Mantle

    val CanvasChecker1Color = Color(0xFF808080)
    val CanvasChecker2Color = Color(0xFFC0C0C0)

    object OutlineTextFieldColors {
        @Composable
        fun colors() =
            OutlinedTextFieldDefaults.colors(

                // Container
                focusedContainerColor = BackgroundColor,
                disabledContainerColor = BackgroundColor,
                unfocusedContainerColor = BackgroundColor,
                errorContainerColor = BackgroundColor,


                // Text Colors
                focusedTextColor = TextColorLight,
                unfocusedTextColor = TextColorLight,
                disabledTextColor = TextColorLight,
                errorTextColor = CurrentPalette.Red,

                // Cursor
                cursorColor = TextColorLight,
                errorCursorColor = CurrentPalette.Red,

                // Indicator (border)
                focusedBorderColor = Foreground2Color,
                unfocusedBorderColor = Foreground2Color,
                disabledBorderColor = Foreground2Color,
                errorBorderColor = CurrentPalette.Red,

                // Leading Icons
                focusedLeadingIconColor = CurrentPalette.Green,
                unfocusedLeadingIconColor = CurrentPalette.Green,
                disabledLeadingIconColor = CurrentPalette.Green,
                errorLeadingIconColor = CurrentPalette.Red,

                // Trailing Icons
                focusedTrailingIconColor = CurrentPalette.Blue,
                unfocusedTrailingIconColor = CurrentPalette.Blue,
                disabledTrailingIconColor = CurrentPalette.Blue,
                errorTrailingIconColor = CurrentPalette.Red,

                // Labels
                focusedLabelColor = SelectedColor,
                unfocusedLabelColor = SelectedColor,
                disabledLabelColor = SelectedColor,
                errorLabelColor = CurrentPalette.Red,

                // Placeholders
                focusedPlaceholderColor = Subtext1Color,
                unfocusedPlaceholderColor = Subtext1Color,
                disabledPlaceholderColor = Subtext1Color,
                errorPlaceholderColor = CurrentPalette.Red,

                // Supporting Text
                focusedSupportingTextColor = Subtext1Color,
                unfocusedSupportingTextColor = Subtext1Color,
                disabledSupportingTextColor = Subtext1Color,
                errorSupportingTextColor = CurrentPalette.Red,

                // Prefix
                focusedPrefixColor = Subtext1Color,
                unfocusedPrefixColor = Subtext1Color,
                disabledPrefixColor = Subtext1Color,
                errorPrefixColor = CurrentPalette.Red,

                // Suffix
                focusedSuffixColor = Subtext1Color,
                unfocusedSuffixColor = Subtext1Color,
                disabledSuffixColor = Subtext1Color,
                errorSuffixColor = CurrentPalette.Red,
        )
    }
}