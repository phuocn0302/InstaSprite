package com.olaz.instasprite.data.repository.palette

import com.olaz.instasprite.data.model.PaletteModel
import com.olaz.instasprite.data.remote.LoadPaletteService

class PaletteRepositoryImpl(private val service: LoadPaletteService) : PaletteRepository {
    override fun getPaletteList(): List<PaletteModel> {
        return service.getPaletteList()
    }


}