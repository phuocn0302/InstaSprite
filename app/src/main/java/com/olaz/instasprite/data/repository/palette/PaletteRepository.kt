package com.olaz.instasprite.data.repository.palette

import com.olaz.instasprite.data.model.PaletteModel

interface PaletteRepository {
    fun getPaletteList(): List<PaletteModel>
}