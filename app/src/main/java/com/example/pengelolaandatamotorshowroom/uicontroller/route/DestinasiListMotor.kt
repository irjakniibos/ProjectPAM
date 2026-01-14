package com.example.pengelolaandatamotorshowroom.uicontroller.route

object DestinasiListMotor : DestinasiNavigasi {
    override val route = "list_motor"
    const val BRAND_ID = "brandId"
    const val BRAND_NAME = "brandName"
    val routeWithArgs = "$route/{$BRAND_ID}/{$BRAND_NAME}"

    fun createRoute(brandId: Int, brandName: String) = "$route/$brandId/$brandName"
}